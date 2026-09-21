package com.cotenoire.service;

import com.cotenoire.dto.*;
import com.cotenoire.entity.*;
import com.cotenoire.enums.*;
import com.cotenoire.exception.*;
import com.cotenoire.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private static final BigDecimal STD = BigDecimal.valueOf(6), EXP = BigDecimal.valueOf(12), FREE = BigDecimal.valueOf(90);
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {

                    Payment payment = paymentRepository
                            .findByOrderId(order.getId())
                            .orElseThrow(() ->
                                    new OrderException(
                                            "Paiement introuvable pour la commande : "
                                                    + order.getId()
                                    )
                            );

                    return response(order, payment);
                })
                .toList();
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest req) {
        if (req.paymentMethod() != PaymentMethod.CASH_ON_DELIVERY)
            throw new OrderException("Seul le paiement à la livraison est disponible.");
        Customer c = new Customer();
        c.setFirstName(req.customer().firstName());
        c.setLastName(req.customer().lastName());
        c.setEmail(req.customer().email());
        c.setPhone(req.customer().phone());
        c.setAddress(req.customer().address());
        c.setCity(req.customer().city());
        c.setPostalCode(req.customer().postalCode());
        c = customerRepository.save(c);
        Order o = new Order();
        o.setCustomer(c);
        o.setStatus(OrderStatus.PENDING);
        o.setDeliveryMethod(req.deliveryMethod());
        o.setCurrency("TND");
        o.setCreatedAt(LocalDateTime.now());
        o.setGiftMessage(req.giftMessage());
        o.setOrderNumber("CN-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        BigDecimal sub = BigDecimal.ZERO;
        for (OrderItemRequest x : req.items()) {
            Product p = productRepository.findById(x.productId()).orElseThrow(() -> new ProductNotFoundException("Produit introuvable : " + x.productId()));
            if (!p.getActive()) throw new OrderException("Produit indisponible : " + p.getName());
            if (p.getStock() < x.quantity()) throw new OrderException("Stock insuffisant pour : " + p.getName());
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(x.quantity()));
            OrderItem i = new OrderItem();
            i.setProduct(p);
            i.setQuantity(x.quantity());
            i.setUnitPrice(p.getPrice());
            i.setTotalPrice(line);
            o.addItem(i);
            sub = sub.add(line);
            p.setStock(p.getStock() - x.quantity());
            productRepository.save(p);
        }
        BigDecimal fee = req.deliveryMethod() == DeliveryMethod.EXPRESS ? EXP : (sub.compareTo(FREE) >= 0 ? BigDecimal.ZERO : STD);
        o.setSubtotal(sub);
        o.setDeliveryFee(fee);
        o.setTotal(sub.add(fee));
        o = orderRepository.save(o);
        Payment p = new Payment();
        p.setOrder(o);
        p.setMethod(PaymentMethod.CASH_ON_DELIVERY);
        p.setStatus(PaymentStatus.PENDING);
        p.setAmount(o.getTotal());
        p.setCurrency("TND");
        p.setCreatedAt(LocalDateTime.now());
        p = paymentRepository.save(p);
        return response(o, p);
    }

    @Transactional(readOnly = true)
    public OrderResponse find(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new OrderException("Commande introuvable : " + id));
        return response(o, paymentRepository.findByOrderId(id).orElseThrow(() -> new OrderException("Paiement introuvable.")));
    }

    @Transactional(readOnly = true)
    public OrderResponse findNumber(String n) {
        Order o = orderRepository.findByOrderNumber(n).orElseThrow(() -> new OrderException("Commande introuvable : " + n));
        return response(o, paymentRepository.findByOrderId(o.getId()).orElseThrow(() -> new OrderException("Paiement introuvable.")));
    }

    @Transactional
    public OrderResponse status(Long id, OrderStatus newStatus) {

        Order o = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderException("Commande introuvable : " + id)
                );

        OrderStatus currentStatus = o.getStatus();


        if (currentStatus == OrderStatus.DELIVERED) {
            throw new OrderException(
                    "La commande est déjà livrée. " +
                            "Il n'est plus possible de modifier son statut."
            );
        }

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new OrderException(
                    "La commande est annulée. " +
                            "Il n'est plus possible de modifier son statut."
            );
        }


        if (newStatus == OrderStatus.CANCELLED) {

            o.setStatus(OrderStatus.CANCELLED);

            o = orderRepository.save(o);

            Payment payment = paymentRepository
                    .findByOrderId(id)
                    .orElseThrow(() ->
                            new OrderException("Paiement introuvable.")
                    );

            return response(o, payment);
        }

        OrderStatus expectedNextStatus;

        switch (currentStatus) {

            case PENDING:
                expectedNextStatus = OrderStatus.CONFIRMED;
                break;

            case CONFIRMED:
                expectedNextStatus = OrderStatus.PROCESSING;
                break;

            case PROCESSING:
                expectedNextStatus = OrderStatus.SHIPPED;
                break;

            case SHIPPED:
                expectedNextStatus = OrderStatus.DELIVERED;
                break;

            case DELIVERED:
            case CANCELLED:
                expectedNextStatus = null;
                break;

            default:
                throw new OrderException(
                        "Statut actuel de commande invalide : "
                                + currentStatus
                );
        }

        if (newStatus != expectedNextStatus) {

            throw new OrderException(
                    "Transition de statut non autorisée : "
                            + currentStatus
                            + " → "
                            + newStatus
                            + ". La commande doit avancer "
                            + "uniquement vers l'étape suivante."
            );
        }

        o.setStatus(newStatus);

        o = orderRepository.save(o);

        Payment payment = paymentRepository
                .findByOrderId(id)
                .orElseThrow(() ->
                        new OrderException("Paiement introuvable.")
                );

        return response(o, payment);
    }

    private OrderResponse response(Order o, Payment p) {

        Customer c = o.getCustomer();

        CustomerResponse customer =
                new CustomerResponse(
                        c.getId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getAddress(),
                        c.getCity(),
                        c.getPostalCode()
                );

        var items =
                o.getItems()
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        "/api/products/" + item.getProduct().getId() + "/image",
                                        item.getQuantity(),
                                        item.getUnitPrice(),
                                        item.getTotalPrice()
                                )
                        )
                        .toList();

        return new OrderResponse(
                o.getId(),
                o.getOrderNumber(),
                o.getStatus(),
                p.getMethod(),
                p.getStatus(),
                o.getDeliveryMethod(),
                o.getSubtotal(),
                o.getDeliveryFee(),
                o.getTotal(),
                o.getCurrency(),
                o.getCreatedAt(),
                customer,
                items
        );
    }
}
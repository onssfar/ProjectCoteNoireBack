package com.cotenoire.service;

import com.cotenoire.entity.Payment;
import com.cotenoire.enums.*;
import com.cotenoire.exception.OrderException;
import com.cotenoire.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository ;
    }

    @Transactional
    public Payment markPaid(Long id) {
        Payment p = paymentRepository.findById(id).orElseThrow(() -> new OrderException("Paiement introuvable : " + id));
        if (p.getStatus() == PaymentStatus.PAID) return p;
        p.setStatus(PaymentStatus.PAID);
        p.setPaidAt(LocalDateTime.now());
        if (p.getOrder().getStatus() != OrderStatus.CANCELLED) {
            p.getOrder().setStatus(OrderStatus.DELIVERED);
            orderRepository.save(p.getOrder());
        }
        return paymentRepository.save(p);
    }
}

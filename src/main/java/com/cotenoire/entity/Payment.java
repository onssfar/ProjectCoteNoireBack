package com.cotenoire.entity;

import com.cotenoire.enums.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal amount;
    @Column(nullable = false, length = 3)
    private String currency = "TND";
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order v) {
        order = v;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod v) {
        method = v;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus v) {
        status = v;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal v) {
        amount = v;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String v) {
        currency = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime v) {
        paidAt = v;
    }
}

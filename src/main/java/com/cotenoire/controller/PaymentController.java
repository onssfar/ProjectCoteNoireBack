package com.cotenoire.controller;

import com.cotenoire.entity.Payment;
import com.cotenoire.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PatchMapping("/{id}/paid")
    public Payment paid(@PathVariable Long id) {
        return paymentService.markPaid(id);
    }
}

package com.marketplace.marketplacepaymentservice.controller;

import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.repository.PaymentRepository;
import com.marketplace.marketplacepaymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
    @PostMapping
    public Payment processPayment(@RequestBody PaymentRequest request) {
        return paymentService.paymentProcesses(request);
    }
}

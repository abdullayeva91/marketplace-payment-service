package com.marketplace.marketplacepaymentservice.controller;

import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.repository.PaymentRepository;
import com.marketplace.marketplacepaymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentRequest request) {
        // Bu metod çağırıldıqda PaymentService daxilində həm bazaya yazılacaq,
        // həm də statusa uyğun olaraq Notification göndəriləcək.
        Payment payment = paymentService.paymentProcesses(request);
        return ResponseEntity.ok(payment);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}

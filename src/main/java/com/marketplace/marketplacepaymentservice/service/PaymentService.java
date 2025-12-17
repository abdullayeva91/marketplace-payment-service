package com.marketplace.marketplacepaymentservice.service;

import com.marketplace.marketplacepaymentservice.client.OrderClient;
import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.enums.PaymentStatus;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
 @Autowired
 private PaymentRepository paymentRepository;
 private final OrderClient orderClient;

    public PaymentService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }


    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    public Payment paymentProcesses(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());


        if (request.getCardNumber().startsWith("123")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return paymentRepository.save(payment);
    }
}

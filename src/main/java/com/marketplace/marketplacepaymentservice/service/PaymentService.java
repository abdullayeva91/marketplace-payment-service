package com.marketplace.marketplacepaymentservice.service;

import com.marketplace.marketplacepaymentservice.client.NotificationClient;
import com.marketplace.marketplacepaymentservice.client.OrderClient;
import com.marketplace.marketplacepaymentservice.dto.NotificationRequest;
import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.enums.PaymentStatus;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.repository.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final NotificationClient notificationClient;

    public PaymentService(PaymentRepository paymentRepository, OrderClient orderClient, NotificationClient notificationClient) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
        this.notificationClient = notificationClient;
    }

    public Payment paymentProcesses(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setUserEmail(request.getUserEmail());

        if (request.getCardNumber().startsWith("123")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {

            updateOrderAfterPayment(savedPayment.getOrderId(), "PAID");

            sendPaymentNotification(savedPayment);
        }

        return savedPayment;
    }

    @CircuitBreaker(name = "order-service", fallbackMethod = "orderStatusFallback")
    public void updateOrderAfterPayment(Long id, String status) {
        orderClient.updateOrderStatus(id, status, "ADMIN");
    }

    @CircuitBreaker(name = "notification-service", fallbackMethod = "notificationFallback")
    public void sendPaymentNotification(Payment payment) {
        NotificationRequest notification = new NotificationRequest();
        notification.setTo(payment.getUserEmail());
        notification.setSubject("Ödəniş #" + payment.getOrderId());
        notification.setMessage("Hörmətli müştəri, ödənişiniz uğurla tamamlandı. Status: " + payment.getStatus());

        notificationClient.sendEmail(notification);
    }

    public void orderStatusFallback(Long id, String status, Throwable t) {
        System.err.println("Fallback: Order servisi əlçatmazdır. ID: " + id + ". Xəta: " + t.getMessage());
    }

    public void notificationFallback(Payment payment, Throwable t) {
        System.err.println("Fallback: Bildiriş servisi əlçatmazdır. Mail: " + payment.getUserEmail() + ". Xəta: " + t.getMessage());
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
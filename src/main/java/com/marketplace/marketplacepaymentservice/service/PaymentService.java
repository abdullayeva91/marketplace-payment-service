package com.marketplace.marketplacepaymentservice.service;

import com.marketplace.marketplacepaymentservice.client.NotificationClient;
import com.marketplace.marketplacepaymentservice.client.OrderClient;
import com.marketplace.marketplacepaymentservice.dto.NotificationRequest;
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
    private final NotificationClient notificationClient;

    public PaymentService(OrderClient orderClient, NotificationClient notificationClient) {
        this.orderClient = orderClient;
        this.notificationClient = notificationClient;
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

        Payment savedPayment = paymentRepository.save(payment);


        try {
            NotificationRequest notification = new NotificationRequest();
            notification.setTo("l.s2015@mail.ru");

            if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
                notification.setSubject("Ödəniş Uğurludur! #" + savedPayment.getOrderId());
                notification.setMessage("Hörmətli müştəri, " + savedPayment.getAmount() + " AZN ödənişiniz uğurla tamamlandı. Transaction ID: " + savedPayment.getTransactionId());
            } else {
                notification.setSubject("Ödəniş Uğursuz Oldu! #" + savedPayment.getOrderId());
                notification.setMessage("Təəssüf ki, ödəniş tamamlanmadı. Zəhmət olmasa kart məlumatlarını yoxlayın.");
            }

            notificationClient.sendEmail(notification);
        } catch (Exception e) {
            System.err.println("Ödəniş bildirişi göndərilmədi: " + e.getMessage());
        }

        return savedPayment;
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

}
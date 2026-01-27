package com.marketplace.marketplacepaymentservice;

import com.marketplace.marketplacepaymentservice.client.NotificationClient;
import com.marketplace.marketplacepaymentservice.client.OrderClient;
import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.enums.PaymentStatus;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.repository.PaymentRepository;
import com.marketplace.marketplacepaymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(Extension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderClient orderClient;
    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private PaymentService paymentService;
    @Test
    void shouldProcessPaymentSuccessfully() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setAmount(new BigDecimal("100.0"));
        request.setCardNumber("12345678"); // 123 ilə başladığı üçün SUCCESS olacaq

        Payment mockSaved = new Payment();
        mockSaved.setOrderId(1L);
        mockSaved.setStatus(PaymentStatus.SUCCESS);
        mockSaved.setAmount(new BigDecimal("100.0"));

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockSaved);

        Payment result = paymentService.paymentProcesses(request);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        verify(notificationClient, atLeastOnce()).sendEmail(any());
    }
    @Test
    void shouldFailPayment_WhenCardDoesNotStartWith123() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setAmount(new BigDecimal("50.0"));
        request.setCardNumber("44445555");

        Payment mockSavedPayment = new Payment();
        mockSavedPayment.setStatus(PaymentStatus.FAILED);
        mockSavedPayment.setAmount(new BigDecimal("50.0"));

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockSavedPayment);

        paymentService.paymentProcesses(request);

        verify(notificationClient, times(1)).sendEmail(any());
    }
}
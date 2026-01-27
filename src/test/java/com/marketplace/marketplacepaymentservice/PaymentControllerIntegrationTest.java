package com.marketplace.marketplacepaymentservice;

import com.marketplace.marketplacepaymentservice.MarketplacePaymentServiceApplication;
import com.marketplace.marketplacepaymentservice.controller.PaymentController;
import com.marketplace.marketplacepaymentservice.dto.PaymentRequest;
import com.marketplace.marketplacepaymentservice.enums.PaymentStatus;
import com.marketplace.marketplacepaymentservice.model.Payment;
import com.marketplace.marketplacepaymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MarketplacePaymentServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void shouldProcessPaymentSuccessfully() throws Exception {
        // Data hazırlığı
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setAmount(new BigDecimal("99.99"));

        Payment mockPayment = new Payment();
        mockPayment.setId(100L);
        mockPayment.setStatus(PaymentStatus.valueOf("SUCCESS"));

        // 4. İndi paymentService mütləq yaradılıb, null ola bilməz!
        when(paymentService.paymentProcesses(any(PaymentRequest.class))).thenReturn(mockPayment);

        // İcra et
        mockMvc.perform(post("/api/payments/process")
                        .header("X-Auth-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnForbidden_WhenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .header("X-Auth-User-Role", "USER")) // USER rolu ilə giririk
                .andExpect(status().isForbidden()); // 403 gözləyirik
    }

    @Test
    void shouldReturnUnauthorized_WhenNoUserHeader() throws Exception {
        mockMvc.perform(post("/api/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized()); // 401 gözləyirik
    }
}
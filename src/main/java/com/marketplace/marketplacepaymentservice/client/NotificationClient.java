package com.marketplace.marketplacepaymentservice.client;

import com.marketplace.marketplacepaymentservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8083")
public interface NotificationClient {
    @PostMapping("/api/notifications/send")
    void sendEmail(@RequestBody NotificationRequest request);
}

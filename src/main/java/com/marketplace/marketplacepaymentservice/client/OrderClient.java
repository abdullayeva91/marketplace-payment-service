package com.marketplace.marketplacepaymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "http://localhost:8084")
public interface OrderClient {
    @PostMapping("/api/orders/{id}/update-status")
    void updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status,
            @RequestHeader("X-Auth-User-Role") String role
    );
}

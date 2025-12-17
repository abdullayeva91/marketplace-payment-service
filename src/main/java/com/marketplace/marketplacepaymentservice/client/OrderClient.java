package com.marketplace.marketplacepaymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "http://localhost:8084/api/orders")
public interface OrderClient {
    @PostMapping("/{id}/status")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}

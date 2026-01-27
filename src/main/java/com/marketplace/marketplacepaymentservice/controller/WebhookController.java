package com.marketplace.marketplacepaymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @PostMapping("/{paymentId}")
    public ResponseEntity<String> webhook(@PathVariable String paymentId,@RequestBody Map<String, Object> payload) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Object amount = payload.get("amount");
        Object status = payload.get("status");
        Object currency = payload.get("currency");
        System.out.println("\n--- WEBHOOK DAXİL OLDU ---");
        System.out.println("ID: " + paymentId);
        System.out.println("Məbləğ: " + (amount != null ? amount : "Məlum deyil") + " " + (currency != null ? currency : ""));
        System.out.println("Status: " + (status != null ? status : "Gözləmədə"));
        System.out.println("Sistem vaxtı: " + currentTime);
        System.out.println("---------------------------\n");

        return ResponseEntity.ok("Webhook uğurla emal edildi");
    }
}

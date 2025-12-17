package com.marketplace.marketplacepaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MarketplacePaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplacePaymentServiceApplication.class, args);
    }

}

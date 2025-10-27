package com.thangcayEP.ElectricVehicles.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;

    @Value("${PAYOS_API_KEY}")
    private String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;
    @Bean
    public PayOS payOS() {
        System.out.println("client: "+clientId +"/api: "+ apiKey +"/ sum: "+ checksumKey);
        return new PayOS(clientId, apiKey, checksumKey);
    }
}

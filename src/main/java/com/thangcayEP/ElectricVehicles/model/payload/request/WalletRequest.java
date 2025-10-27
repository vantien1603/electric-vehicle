package com.thangcayEP.ElectricVehicles.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WalletRequest {
    private Long userId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime createdAt;
}

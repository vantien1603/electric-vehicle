package com.thangcayEP.ElectricVehicles.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletTransactionResponse {
    private Long id;
    private String type;
    private BigDecimal amount;
    private String status;
    private String externalId;
    private LocalDateTime createdAt;
}

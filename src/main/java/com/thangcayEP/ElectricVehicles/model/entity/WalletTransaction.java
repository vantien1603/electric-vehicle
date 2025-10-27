package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets_transaction")
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private String type;   // DEPOSIT, WITHDRAW, PAYMENT, REFUND
    private BigDecimal amount;
    private String status; // PENDING, SUCCESS, FAILED
    private String externalId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

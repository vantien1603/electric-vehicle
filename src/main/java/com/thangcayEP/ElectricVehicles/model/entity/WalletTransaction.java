package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private String type;   // DEPOSIT, WITHDRAW, PAYMENT, REFUND
    private BigDecimal amount;
    private String status; // PENDING, SUCCESS, FAILED

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

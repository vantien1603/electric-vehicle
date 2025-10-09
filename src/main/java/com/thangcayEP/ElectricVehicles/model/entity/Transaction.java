package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(optional = false)
    @JoinColumn(name = "listing_id")
    private News news;

    private BigDecimal price;
    private BigDecimal commissionFee;
    private BigDecimal netAmount;

    private String status; // PENDING, PAID, COMPLETED, CANCELLED
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

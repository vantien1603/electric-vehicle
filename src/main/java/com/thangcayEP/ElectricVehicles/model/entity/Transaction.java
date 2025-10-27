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
    @JoinColumn(name = "new_id")
    private News news;

    private BigDecimal price;
    private BigDecimal commissionFee;
    private BigDecimal netAmount;

    private String status; // PENDING, PAID, DELIVERED, CANCELLED

    // 🔹 Thông tin giao hàng (lưu trực tiếp trong đơn)
    private String recipientName;     // Họ và tên người nhận
    private String recipientPhone;    // SĐT người nhận
    private String address;           // Số nhà, đường
    private String ward;              // Phường/Xã
    private String district;          // Quận/Huyện
    private String city;              // Tỉnh/Thành phố
    private String note;              // Ghi chú cho người bán (nếu có)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

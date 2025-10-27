package com.thangcayEP.ElectricVehicles.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private Long buyerId;
    private Long sellerId;
    private Long newsId;

    private BigDecimal price;
    private BigDecimal commissionFee;
    private BigDecimal netAmount;

    private String recipientName;     // Họ và tên người nhận
    private String recipientPhone;    // SĐT người nhận
    private String address;           // Số nhà, đường
    private String ward;              // Phường/Xã
    private String district;          // Quận/Huyện
    private String city;              // Tỉnh/Thành phố
    private String note;              // Ghi chú cho ngư
}

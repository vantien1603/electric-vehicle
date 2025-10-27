package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponse {
    private Long id;
//    private UserDto buyer;
//    private UserDto seller;
    private NewsResponse news ;
    private BigDecimal price;
    private BigDecimal commissionFee;
    private BigDecimal netAmount;
    private String recipientName;     // Họ và tên người nhận
    private String recipientPhone;    // SĐT người nhận
    private String address;           // Số nhà, đường
    private String ward;              // Phường/Xã
    private String district;          // Quận/Huyện
    private String city;              // Tỉnh/Thành phố
    private String note;
}

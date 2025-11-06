package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
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
public class TransactionResponse {
    private Long id;
//    private UserDto buyer;
//    private UserDto seller;
    private NewsResponse news ;
    private BigDecimal price;
    private BigDecimal commissionFee;
    private BigDecimal netAmount;
    private String status;
    private String recipientName;
    private String recipientPhone;
    private String address;
    private String ward;
    private String district;
    private String city;
    private String note;
    private LocalDateTime createdAt;
}

package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private UserDto user;
    private String description;
    private BigDecimal price;
    private String status; // APPROVED, SOLD, HIDDEN, DELETED
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private Long vehicleMileage;
    private String vehicleBatteryCapacity;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
}

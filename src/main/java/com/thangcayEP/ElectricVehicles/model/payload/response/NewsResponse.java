package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.entity.Categories;
import com.thangcayEP.ElectricVehicles.model.payload.dto.CategoryDto;
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
    private String title;
    private String description;
    private BigDecimal price;
    private String status; // APPROVED, SOLD, HIDDEN, DELETED
    private CategoryDto category;
    private String contactPhone;
    private String vehicleStatus;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private String color;
    private String vehicleBatteryCapacity;
    private String location;
    private Long topSpeed;
    private Long distanceTraveled;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
}

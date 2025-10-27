package com.thangcayEP.ElectricVehicles.model.payload.dto;

import com.thangcayEP.ElectricVehicles.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private Long id;
    private UserDto userDto;
    private String title;
    private String description;
    private BigDecimal price;
    private String status; // APPROVED, SOLD, HIDDEN, DELETED
    private String contactPhone;
    private Long categoryId;
    private String vehicleStatus;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private String color;
    private String vehicleBatteryCapacity;
    private Long topSpeed;
    private Long distanceTraveled;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
}

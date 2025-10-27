package com.thangcayEP.ElectricVehicles.model.payload.request;

import com.thangcayEP.ElectricVehicles.model.entity.Categories;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class NewsRequest {
//    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String contactPhone;
    private String vehicleStatus;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private String location;
    private String color;
    private String vehicleBatteryCapacity;
    private Long topSpeed;
    private Long distanceTraveled;
    private List<String> imageUrls;
    private List<MultipartFile> files;
}

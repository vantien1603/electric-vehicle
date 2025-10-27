package com.thangcayEP.ElectricVehicles.model.payload.dto;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String name;
}

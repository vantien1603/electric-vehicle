package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.payload.dto.NewsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private NewsDto news;
    private LocalDateTime createdAt;
}

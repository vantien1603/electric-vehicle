package com.thangcayEP.ElectricVehicles.model.payload.dto;

import com.thangcayEP.ElectricVehicles.model.payload.response.NewsNotificationResponse;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class NotificationsDto {
    private Long id;
    private String message;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    private UserDto user;
    private NewsNotificationResponse news;
}

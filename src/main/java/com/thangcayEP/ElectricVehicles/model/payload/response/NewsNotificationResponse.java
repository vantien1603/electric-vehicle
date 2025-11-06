package com.thangcayEP.ElectricVehicles.model.payload.response;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import lombok.*;


import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsNotificationResponse {
    private Long id;
    private UserDto user;
    private List<String> imageUrls;
}

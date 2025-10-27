package com.thangcayEP.ElectricVehicles.model.payload.request;

import com.thangcayEP.ElectricVehicles.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private MultipartFile file;
    private String avatarUrl;
    private String address;
    private String phone;
}

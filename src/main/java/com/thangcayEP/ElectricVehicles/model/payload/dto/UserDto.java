package com.thangcayEP.ElectricVehicles.model.payload.dto;

import com.thangcayEP.ElectricVehicles.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String avatarUrl;
    private String address;
    private String phone;
    private Role role;
    private LocalDateTime createAt;
}

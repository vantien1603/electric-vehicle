package com.thangcayEP.ElectricVehicles.model.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long roleId;
}

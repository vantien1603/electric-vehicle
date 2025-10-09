package com.thangcayEP.ElectricVehicles.model.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyDto {
    @NotEmpty(message = "Email can not be empty")
    private String email;
    @NotEmpty(message = "Verification code can not be empty")
    private String verificationCode;
}

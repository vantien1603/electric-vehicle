package com.thangcayEP.ElectricVehicles.model.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String newPassword;
    @NotEmpty(message = "Token cannot be empty")
    private String token;
}

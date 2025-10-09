package com.thangcayEP.ElectricVehicles.model.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetPasswordRequest {
    @NotEmpty(message = "Email can not be empty")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    private String password;
}

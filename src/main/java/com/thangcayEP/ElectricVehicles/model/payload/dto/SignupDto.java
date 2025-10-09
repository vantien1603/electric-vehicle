package com.thangcayEP.ElectricVehicles.model.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    @Email(regexp = ".+@.+\\..+", message = "Email is invalid!")
    private String email;
    @NotEmpty(message = "Fullname can not be empty")
    private String name;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}

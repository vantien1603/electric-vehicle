package com.thangcayEP.ElectricVehicles.model.payload.request;

import lombok.*;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

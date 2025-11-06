package com.thangcayEP.ElectricVehicles.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsApproveRequest {
    String status;
    String reason;
}

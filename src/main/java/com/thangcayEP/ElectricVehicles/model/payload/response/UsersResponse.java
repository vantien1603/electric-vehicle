package com.thangcayEP.ElectricVehicles.model.payload.response;

import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersResponse {
    private List<UserDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}

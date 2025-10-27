package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.ChangePasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.UpdateUserRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.UsersResponse;

public interface UserService {
    UserDto getProfile(String email);
    UserDto updateUser (Long id, UpdateUserRequest updateUserRequest);
    void deleteUser (Long id);
    UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir);
    void changePassword (Long id, ChangePasswordRequest changePasswordRequest);
}

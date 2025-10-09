package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.ChangePasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.UsersResponse;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ApiException(HttpStatus.BAD_REQUEST,"Could not found user with this email"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest changePasswordRequest) {

    }
}

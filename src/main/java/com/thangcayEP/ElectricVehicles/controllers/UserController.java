package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.ChangePasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.UpdateUserRequest;
import com.thangcayEP.ElectricVehicles.services.UserService;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @SecurityRequirement(name = "Bearer Authentication")

    @PostMapping("/profile")
    public ResponseEntity<?> viewProfile (@AuthenticationPrincipal CustomUserDetails userDetails){
        UserDto userDto = userService.getProfile(userDetails.getUsername());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser (@AuthenticationPrincipal CustomUserDetails userDetails, UpdateUserRequest updateUserRequest){
        UserDto userDto = userService.updateUser(userDetails.getId(), updateUserRequest);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword (@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(customUserDetails.getId(), changePasswordRequest);
        return new ResponseEntity<>("Change password successfully", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.deleteUser(userDetails.getId());
        return new ResponseEntity<>("Delete user succesfully", HttpStatus.OK);
    }

}

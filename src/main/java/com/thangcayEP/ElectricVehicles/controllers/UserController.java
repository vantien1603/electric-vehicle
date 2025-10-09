package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import com.thangcayEP.ElectricVehicles.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @SecurityRequirement(name = "Bearer Authentication")

    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile (@AuthenticationPrincipal UserDetails userDetails){
        System.out.println(userDetails.getUsername());
        UserDto userDto = userService.getProfile(userDetails.getUsername());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

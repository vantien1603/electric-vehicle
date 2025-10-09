package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.dto.EmailVerifyDto;
import com.thangcayEP.ElectricVehicles.model.payload.dto.LoginDto;
import com.thangcayEP.ElectricVehicles.model.payload.dto.SignupDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewPasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.AuthenticationResponse;
import com.thangcayEP.ElectricVehicles.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/no-auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDto loginDto) {
        AuthenticationResponse token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = {"/signup"})
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDto signupDto) {
        String response = authService.signup(signupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/verify"})
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody EmailVerifyDto emailVerifyDto) {
        String response = authService.verifyEmailCode(emailVerifyDto.getEmail(), emailVerifyDto.getVerificationCode());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/resend")
    public ResponseEntity<String> resendCode(@RequestParam("email") String email) {
        String response = authService.resendVerificationCode(email);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email){
        String response = authService.forgotPassword(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest){
        String response = authService.resetPassword(newPasswordRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}

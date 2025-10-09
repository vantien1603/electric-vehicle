package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.dto.LoginDto;
import com.thangcayEP.ElectricVehicles.model.payload.dto.SignupDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewPasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.SetPasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse login(LoginDto loginDto);
    String signup(SignupDto signupDto);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    String verifyEmailCode(String email, String code);
    String resendVerificationCode(String email);
    String forgotPassword(String email);
    String resetPassword(NewPasswordRequest newPasswordRequest);
}

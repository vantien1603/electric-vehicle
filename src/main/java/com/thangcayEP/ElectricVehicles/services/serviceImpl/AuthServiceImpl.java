package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.Role;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.dto.LoginDto;
import com.thangcayEP.ElectricVehicles.model.payload.dto.SignupDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewPasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.SetPasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.AuthenticationResponse;
import com.thangcayEP.ElectricVehicles.repositories.AccessTokenRepository;
import com.thangcayEP.ElectricVehicles.repositories.RefreshTokenRepository;
import com.thangcayEP.ElectricVehicles.repositories.RoleRepository;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.security.JwtTokenProvider;
import com.thangcayEP.ElectricVehicles.services.AuthService;
import com.thangcayEP.ElectricVehicles.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private AccessTokenRepository accessToken;
    private RefreshTokenRepository refreshTokenRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private EmailVerificationService emailVerificationService;
    private WalletService walletService;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, WalletService walletService, AccessTokenRepository accessToken, RefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository, ModelMapper modelMapper, EmailVerificationService emailVerificationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accessToken = accessToken;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User not found"));
        if (!user.isEmailVerified()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Email is not verified. Please verify your email.");
        }
        if (user.getStatus().equals("DELETED")){
            throw new ApiException(HttpStatus.FORBIDDEN, "Your account has been deleted.");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(authentication, user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, user);

//        String fullName = user.getName();

//        revokeRefreshToken(accessToken);
//        RefreshToken savedRefreshToken = saveUserRefreshToken(refreshToken);
//
//        revokeAllUserAccessTokens(user);
//        saveUserAccessToken(user, accessToken, savedRefreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
//                .fullName(fullName)
                .build();
    }



    @Override
    public String signup(SignupDto signupDto) {
//        if (userRepository.existsByEmail(signupDto.getEmail().toString())) {
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
//        }
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Role userRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        user.setRole(userRole);
        user.setEmailVerified(false);
        user.setAvatarUrl("default");
        user.setCreateAt(LocalDateTime.now());
        emailVerificationService.sendVerificationCode(user);

        User user1 = userRepository.save(user);

        walletService.createWallet(user.getId(),user.getEmail());

        return "User registered successfully! Please check your email for the verification code.";
    }


    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return null;
    }

    @Override
    public String verifyEmailCode(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with this email"));
        if(user.getVerificationCode()==null || !user.getVerificationCode().equals(code)) throw new ApiException(HttpStatus.BAD_REQUEST, "Verification code is invalid");
        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Verification code has expired.");
        }
        user.setStatus("ACTIVATED");
        user.setUpdateAt(LocalDateTime.now());
        user.setEmailVerified(true);
        user.setVerificationCode(null); // Clear the code after verification
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);
        return "Email verified successfully!";
    }

    @Override
    public String resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "User not found with this email"));
        if (user.isEmailVerified()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Email is already verified.");
        }
        emailVerificationService.sendVerificationCode(user);
        return "Email verification code has been resent";
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "User not fount with this email"));
        emailVerificationService.sendPasswordResetToken(user);
        return "Password reset token sent to your email";
    }

    @Override
    public String resetPassword(NewPasswordRequest newPasswordRequest) {
        User user = userRepository.findByEmail(newPasswordRequest.getEmail()).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "User not fount with this email"));
        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password reset token has expired.");
        }
        if(!newPasswordRequest.getToken().equals(user.getResetPasswordToken())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Token reset password is not correct");
        }
        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        return "Password reset successfully!";
    }
}

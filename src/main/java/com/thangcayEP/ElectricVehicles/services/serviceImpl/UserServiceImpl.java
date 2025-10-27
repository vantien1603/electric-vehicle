package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.AccessToken;
import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.RefreshToken;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.dto.UserDto;
import com.thangcayEP.ElectricVehicles.model.payload.request.ChangePasswordRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.UpdateUserRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.UsersResponse;
import com.thangcayEP.ElectricVehicles.repositories.*;
import com.thangcayEP.ElectricVehicles.services.UserService;
import com.thangcayEP.ElectricVehicles.services.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    WalletService walletService;
    NewsRepository newsRepository;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;
    AccessTokenRepository accessTokenRepository;
    RefreshTokenRepository refreshTokenRepository;
    CloudinaryService cloudinaryService;

    public UserServiceImpl(CloudinaryService cloudinaryService, RefreshTokenRepository refreshTokenRepository, AccessTokenRepository accessTokenRepository, PasswordEncoder passwordEncoder, NewsRepository newsRepository, WalletService walletService, UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.walletService = walletService;
        this.newsRepository = newsRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public UserDto getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Could not found user with this email"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Could not found user with id: " + id));
        if (!updateUserRequest.getName().isEmpty() && !updateUserRequest.getName().equals(user.getName())) {
            user.setName(updateUserRequest.getName());
        }
        if (!updateUserRequest.getAddress().isEmpty() && !updateUserRequest.getAddress().equals(user.getAddress())) {
            user.setAddress(updateUserRequest.getAddress());
        }
        if (!updateUserRequest.getPhone().isEmpty() && !updateUserRequest.getPhone().equals(user.getPhone())) {
            user.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getFile() != null) {
            try {
                String url = cloudinaryService.uploadFile(updateUserRequest.getFile());
                if(!url.equals(user.getAvatarUrl())){
                    user.setAvatarUrl(url);
                }
            } catch (IOException e) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
//            user.setAvatarUrl(updateUserRequest.getAvatarUrl());
        }
        User user1 = userRepository.save(user);
        return modelMapper.map(user1, UserDto.class);
    }

    @Override
    public UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cant not find user with id: " + id));
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(),user.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current password does not correct");
        }
        if (!changePasswordRequest.getConfirmPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public void revokeRefreshToken(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken);
        if (token != null) {
            RefreshToken refreshToken = token.getRefreshToken();
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    public void revokeAllUserAccessTokens(User user) {
        var validUserTokens = accessTokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(accessToken -> {
            accessToken.setRevoked(true);
            accessToken.setExpired(true);
        });
        accessTokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cound not found user with id: " + id));
        user.setStatus("DELETED");
        userRepository.save(user);
        List<News> news = newsRepository.findByUserIdNotDeletedList(id);
        news.forEach(n -> {
            n.setStatus("DELETED");
            newsRepository.save(n);
        });
        revokeAllUserAccessTokens(user);
        var token = accessTokenRepository.findAllValidTokensByUser(id);
        if (!token.isEmpty()) {
            token.forEach((e) -> {
                revokeRefreshToken(e.getToken());
            });
        }
    }
}

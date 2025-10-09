package com.thangcayEP.ElectricVehicles.utils;

import com.thangcayEP.ElectricVehicles.model.entity.AccessToken;
import com.thangcayEP.ElectricVehicles.repositories.AccessTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.prefs.BackingStoreException;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final AccessTokenRepository accessTokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            AccessToken accessToken = accessTokenRepository.findByToken(token);
//            accessTokenRepository.findByToken(token).ifPresent(authToken -> {
//                authToken.setRevoked(true);
//                authTokenRepository.save(authToken);
//            });
            if (accessToken!=null) accessToken.setRevoked(true);
            accessTokenRepository.save(accessToken);
        }
    }
}


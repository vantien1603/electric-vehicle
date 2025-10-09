package com.thangcayEP.ElectricVehicles.security;


import com.thangcayEP.ElectricVehicles.repositories.AccessTokenRepository;
import com.thangcayEP.ElectricVehicles.repositories.RefreshTokenRepository;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            String username = jwtTokenProvider.getUsernameFromJwt(jwt);
            // Giả sử JwtTokenProvider hoặc một cơ chế khác cung cấp userId từ token
            String userIdStr = jwtTokenProvider.getUserIdFromJwt(jwt);
            Long userId = Long.parseLong(userIdStr);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Tải UserDetails từ service (bạn có thể chuyển đổi sang CustomUserDetails nếu cần)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // Giả sử bạn có thể xây dựng CustomUserDetails từ userDetails và userId:
                CustomUserDetails customUserDetails = new CustomUserDetails(
                        userId,
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                // Sử dụng WebAuthenticationDetailsSource nếu cần để lưu các thông tin về request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Lưu Authentication vào SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

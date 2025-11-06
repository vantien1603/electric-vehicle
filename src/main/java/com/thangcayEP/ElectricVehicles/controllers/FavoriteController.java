package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.response.ListFavoriteResponse;
import com.thangcayEP.ElectricVehicles.services.FavoriteService;
import com.thangcayEP.ElectricVehicles.utils.AppConstants;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/v1/favorite")
@SecurityRequirement(name = "Bearer Authentication")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getFavoriteByUserId(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                 @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        ListFavoriteResponse response = favoriteService.getFavoriteByUser(customUserDetails.getId(), pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{newsId}")
    public ResponseEntity<?> addToFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable Long newsId) {
        favoriteService.addNewsToFavorite(customUserDetails.getId(), newsId);
        return ResponseEntity.ok("Added news to wishlist");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{newsId}")
    public ResponseEntity<?> removeFromFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @PathVariable Long newsId) {
        favoriteService.removeNewsFromFavorite(customUserDetails.getId(), newsId);
        return ResponseEntity.ok("Removed news from wishlist");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        favoriteService.clearWishlist(customUserDetails.getId());
        return ResponseEntity.ok("Clear wishlist");
    }
}

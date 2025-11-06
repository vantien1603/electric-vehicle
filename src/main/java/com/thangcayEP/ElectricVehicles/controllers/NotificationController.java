package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.dto.NotificationsDto;
import com.thangcayEP.ElectricVehicles.services.NotificationService;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<?> getNotiByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<NotificationsDto> res = notificationService.getUserNotifications(customUserDetails.getId());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead (@PathVariable Long id){
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/read-all")
    public ResponseEntity<?> markAllRead (@PathVariable Long userId){
        notificationService.markAllRead(userId);
        return ResponseEntity.ok().build();
    }
}

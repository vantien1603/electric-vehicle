package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.services.UserService;
import com.thangcayEP.ElectricVehicles.services.WalletService;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    WalletService walletService;
    UserService userService;
    ModelMapper modelMapper;

    public WalletController(WalletService walletService, UserService userService, ModelMapper modelMapper) {
        this.walletService = walletService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal CustomUserDetails userDetails,
                          @RequestParam BigDecimal amount) {
        return walletService.createDepositLink(userDetails.getId(), amount);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody Map<String, Object> payload) {
        String externalId = String.valueOf(payload.get("orderCode"));
        String status = String.valueOf(payload.get("status"));
        walletService.handleWebhook(externalId, status);
        return ResponseEntity.ok("Webhook processed successfully");
    }

    @GetMapping("/balance")
    public BigDecimal getBalance(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return walletService.getBalance(userDetails.getId());
    }
}

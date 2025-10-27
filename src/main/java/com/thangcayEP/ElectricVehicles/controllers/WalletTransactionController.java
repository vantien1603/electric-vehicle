package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.response.ListWalletTransactionResponse;
import com.thangcayEP.ElectricVehicles.services.WalletTransactionService;
import com.thangcayEP.ElectricVehicles.utils.AppConstants;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet-transaction")
public class WalletTransactionController {
    @Autowired
    WalletTransactionService walletTransactionService;

    @GetMapping
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {
        ListWalletTransactionResponse response = walletTransactionService.getWalletHistory(customUserDetails.getId(), pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

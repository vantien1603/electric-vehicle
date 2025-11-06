package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.request.TransactionRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListTransactionResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.TransactionResponse;
import com.thangcayEP.ElectricVehicles.services.TransactionService;
import com.thangcayEP.ElectricVehicles.utils.AppConstants;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<?> createTrans(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createOrder(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/buyer")
    public ResponseEntity<?> getMyBuyTransaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
                                              @RequestParam(value = "status", required = false) String status) {
        ListTransactionResponse response = transactionService.getForBuyer(userDetails.getId(), pageNo, pageSize, sortBy, sortDir, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/seller")
    public ResponseEntity<?> getMySellTransaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
                                              @RequestParam(value = "status", required = false) String status) {
        ListTransactionResponse response = transactionService.getForSeller(userDetails.getId(), pageNo, pageSize, sortBy, sortDir, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

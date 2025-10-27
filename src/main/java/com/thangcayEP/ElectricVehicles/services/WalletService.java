package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.entity.Wallet;
import com.thangcayEP.ElectricVehicles.model.payload.request.TransactionRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.WalletRequest;

import java.math.BigDecimal;
import java.util.Map;

public interface WalletService {
    void createWallet(Long userId, String email);

    String createDepositLink(Long userId, BigDecimal amount);

    BigDecimal getBalance(Long userId);

    Wallet getWalletByUser(Long userId);

    void withdraw(WalletRequest request);

    void handleWebhook(String externalId, String status);

}

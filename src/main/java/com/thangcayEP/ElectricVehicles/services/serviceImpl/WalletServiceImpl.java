package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.entity.Wallet;
import com.thangcayEP.ElectricVehicles.model.entity.WalletTransaction;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.request.WalletRequest;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.repositories.WalletRepository;
import com.thangcayEP.ElectricVehicles.repositories.WalletTransactionRepository;
import com.thangcayEP.ElectricVehicles.services.WalletService;
import jakarta.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private PayOS payOS;
    private WalletRepository walletRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private UserRepository userRepository;
    private RestTemplate restTemplate;

    public WalletServiceImpl(PayOS payOS, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, RestTemplate restTemplate, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.restTemplate = restTemplate;
        this.payOS = payOS;

    }

    @Override
    public void createWallet(Long userId, String email) {
        if (walletRepository.existsByUserId(userId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Wallet already exist for user:" + userId);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not found user with id: " + userId));
        Wallet wallet = new Wallet();
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setPaypalAccountEmail(email);
        walletRepository.save(wallet);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() -> walletRepository.save(Wallet.builder()
                        .user(user)
                        .balance(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .build()));
        return wallet.getBalance();
    }

    @Override
    public String createDepositLink(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() -> walletRepository.save(Wallet.builder()
                        .user(user)
                        .balance(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .build()));

        WalletTransaction transaction = walletTransactionRepository.save(WalletTransaction.builder()
                .wallet(wallet)
                .type("DEPOSIT")
                .amount(amount)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build());

        try {
            CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                    .orderCode(transaction.getId())
                    .amount(amount.longValue())
                    .description("Deposit for user " + userId)
                    .returnUrl("https://your-frontend.com/deposit/success")
                    .cancelUrl("https://your-frontend.com/deposit/cancel")
                    .build();

            CreatePaymentLinkResponse response = payOS.paymentRequests().create(paymentData);

            transaction.setExternalId(String.valueOf(response.getOrderCode()));
            walletTransactionRepository.save(transaction);

            return response.getCheckoutUrl();


        } catch (Exception e) {
            transaction.setStatus("FAILED");
            walletTransactionRepository.save(transaction);
            throw new ApiException(HttpStatus.BAD_REQUEST, "Failed to create PayOS link: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleWebhook(String externalId, String status) {
        WalletTransaction tx = walletTransactionRepository.findByExternalId(externalId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transaction not found"));

        if (status.equalsIgnoreCase("PAID") && tx.getStatus().equals("PENDING")) {
            Wallet wallet = tx.getWallet();
            wallet.setBalance(wallet.getBalance().add(tx.getAmount()));
            walletRepository.save(wallet);

            tx.setStatus("SUCCESS");
            walletTransactionRepository.save(tx);
        }
    }

    @Override
    public Wallet getWalletByUser(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Wallet not found"));
    }

//    @Override
//    public List<WalletTransaction> getTransactions(Long walletId) {
//
//        return walletTransactionRepository.findByWalletId(walletId);
//    }

    @Override
    @Transactional
    public void withdraw(WalletRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Wallet not found"));

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        walletTransactionRepository.save(WalletTransaction.builder()
                .wallet(wallet)
                .type("WITHDRAW")
                .amount(request.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build());
    }

}

package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.request.TransactionRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListTransactionResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.TransactionResponse;

import java.math.BigDecimal;

public interface TransactionService {
    TransactionResponse createOrder (TransactionRequest transactionRequest);
    ListTransactionResponse getByUser (Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
}

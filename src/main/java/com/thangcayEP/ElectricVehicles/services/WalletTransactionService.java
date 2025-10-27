package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.entity.WalletTransaction;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListWalletTransactionResponse;

import java.awt.print.Pageable;

public interface WalletTransactionService {
     ListWalletTransactionResponse getWalletHistory(Long walletId, int pageNo, int pageSize, String sortBy, String sortDir);
}

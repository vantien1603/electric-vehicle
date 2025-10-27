package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.entity.Wallet;
import com.thangcayEP.ElectricVehicles.model.entity.WalletTransaction;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListWalletTransactionResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.WalletTransactionResponse;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.repositories.WalletRepository;
import com.thangcayEP.ElectricVehicles.repositories.WalletTransactionRepository;
import com.thangcayEP.ElectricVehicles.services.WalletTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {
    WalletTransactionRepository walletTransactionRepository;
    WalletRepository walletRepository;
    ModelMapper modelMapper;
    UserRepository userRepository;

    public WalletTransactionServiceImpl(UserRepository userRepository, WalletTransactionRepository walletTransactionRepository, WalletRepository walletRepository, ModelMapper modelMapper) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ListWalletTransactionResponse getWalletHistory(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "Can not found user with id: " +userId));
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "Can not found wallet for user with id: " + userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<WalletTransaction> list = walletTransactionRepository.findByWallet(wallet,pageable);
        List<WalletTransactionResponse> content = list.stream().map(bt -> modelMapper.map(bt, WalletTransactionResponse.class)).collect(Collectors.toList());
        ListWalletTransactionResponse response = new ListWalletTransactionResponse();
        response.setContent(content);
        response.setPageNo(list.getNumber());
        response.setPageSize(list.getSize());
        response.setTotalElements(list.getTotalElements());
        response.setTotalPages(list.getTotalPages());
        response.setLast(list.isLast());
        return response;
    }
}

package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.Transaction;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.entity.Wallet;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.request.TransactionRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.FavoriteResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListTransactionResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.TransactionResponse;
import com.thangcayEP.ElectricVehicles.repositories.*;
import com.thangcayEP.ElectricVehicles.services.NotificationService;
import com.thangcayEP.ElectricVehicles.services.TransactionService;
import com.thangcayEP.ElectricVehicles.services.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private UserRepository userRepository;
    private NewsRepository newsRepository;
    private TransactionRepository transactionRepository;
    private NewsImageRepository newsImageRepository;
    private NotificationService notificationService;
    private WalletService walletService;
    private WalletRepository walletRepository;
    private ModelMapper modelMapper;

    public TransactionServiceImpl(WalletRepository walletRepository, WalletService walletService, NotificationService notificationService, NewsImageRepository newsImageRepository, UserRepository userRepository, NewsRepository newsRepository, TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
        this.newsImageRepository = newsImageRepository;
        this.notificationService = notificationService;
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    @Override
    public TransactionResponse createOrder(TransactionRequest transactionRequest) {
        User user = userRepository.findById(transactionRequest.getBuyerId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find user with id: " + transactionRequest.getBuyerId()));
        News news = newsRepository.findByIdNotDeleted(transactionRequest.getNewsId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find news with id: " + transactionRequest.getNewsId()));
        if (!news.getStatus().equals("APPROVED")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "News is no longer available");
        }
        Transaction transaction = new Transaction();
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setNews(news);
        transaction.setBuyer(user);
        transaction.setSeller(news.getUser());
        transaction.setAddress(transactionRequest.getAddress());
        transaction.setNote(transactionRequest.getNote());
        transaction.setPrice(news.getPrice());
        transaction.setRecipientName(transactionRequest.getRecipientName());
        transaction.setRecipientPhone(transactionRequest.getRecipientPhone());
        transaction.setCity(transactionRequest.getCity());
        transaction.setDistrict(transactionRequest.getDistrict());
        transaction.setWard(transaction.getWard());
        BigDecimal balance = walletService.getBalance(user.getId());
        if(balance.compareTo(news.getPrice())>0){
            transaction.setStatus("PAID");
            news.setStatus("SOLD");
            Wallet wallet =  walletService.getWalletByUser(user.getId());
            wallet.setBalance(balance.subtract(news.getPrice()));
            walletRepository.save(wallet);
        } else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient balance, please top up.");
        }
        newsRepository.save(news);
        notificationService.createNotification(news.getUser(), "Đơn hàng của bạn đã được mua bởi " + user.getName() + ". Hãy chuẩn bị hàng và vận chuyển nhé.", news);
        transactionRepository.save(transaction);
        TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);
        return response;
    }

    @Override
    public ListTransactionResponse getForBuyer(Long userId, int pageNo, int pageSize, String sortBy, String sortDir, String status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find user with id: " + userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> page = transactionRepository.findByBuyerId(userId,status, pageable);
        List<TransactionResponse> content = page.stream().map(transaction -> {
            TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);

            if (response.getNews() != null) {
                Long newsId = response.getNews().getId();
                List<String> imageUrls = newsImageRepository.findImageUrlByListingId(newsId);
                response.getNews().setImageUrls(imageUrls);
            }

            return response;
        }).collect(Collectors.toList());
        ListTransactionResponse response = new ListTransactionResponse();
        response.setContent(content);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }

    @Override
    public ListTransactionResponse getForSeller(Long userId, int pageNo, int pageSize, String sortBy, String sortDir, String status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find user with id: " + userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> page = transactionRepository.findBySellerId(userId,status, pageable);
        List<TransactionResponse> content = page.stream().map(transaction -> {
            TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);

            if (response.getNews() != null) {
                Long newsId = response.getNews().getId();
                List<String> imageUrls = newsImageRepository.findImageUrlByListingId(newsId);
                response.getNews().setImageUrls(imageUrls);
            }

            return response;
        }).collect(Collectors.toList());
        ListTransactionResponse response = new ListTransactionResponse();
        response.setContent(content);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }
}

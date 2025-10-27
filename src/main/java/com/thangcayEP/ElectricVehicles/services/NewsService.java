package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;

import java.math.BigDecimal;

public interface NewsService {
    NewsResponse createNews (Long userId, NewsRequest newsRequest);
    NewsResponse updateNews (Long id, NewsRequest newsRequest);
    String deleteNews (Long id);
    NewsResponse getNewsById (Long id);
    ListNewsResponse getAllNews (int pageNo, int pageSize, String sortBy, String sortDir,String keyWord, Long categoryId, String vehicleStatus,  BigDecimal maxPrice, String location);
    ListNewsResponse getNewsByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
}

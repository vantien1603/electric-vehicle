package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;

public interface NewsService {
    NewsResponse createNews (NewsRequest newsRequest);
    NewsResponse updateNews (Long id, NewsRequest newsRequest);
    String deleteNews (Long id);
    NewsResponse getNewsById (Long id);
    ListNewsResponse getAllNews (int pageNo, int pageSize, String sortBy, String sortDir);
    ListNewsResponse getNewsByUser(int pageNo, int pageSize, String sortBy, String sortDir);
}

package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.response.ListFavoriteResponse;

public interface FavoriteService {
    ListFavoriteResponse getFavoriteByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    public String toggleFavorite(Long userId, Long newsId);
}

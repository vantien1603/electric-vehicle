package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.payload.response.ListFavoriteResponse;

public interface FavoriteService {
    ListFavoriteResponse getFavoriteByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    public void addNewsToFavorite(Long userId, Long newsId);
    public void removeNewsFromFavorite(Long userId, Long newsId);
    void clearWishlist(Long userId);
}

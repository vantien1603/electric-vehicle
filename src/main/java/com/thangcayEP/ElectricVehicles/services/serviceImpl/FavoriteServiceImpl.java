package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.Favorite;
import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.response.FavoriteResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListFavoriteResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.repositories.FavoriteRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsImageRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsRepository;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.services.FavoriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private UserRepository userRepository;
    private NewsRepository newsRepository;
    private FavoriteRepository favoriteRepository;
    private NewsImageRepository newsImageRepository;
    private ModelMapper modelMapper;

    public FavoriteServiceImpl(ModelMapper modelMapper, NewsImageRepository newsImageRepository, UserRepository userRepository, NewsRepository newsRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.favoriteRepository = favoriteRepository;
        this.newsImageRepository = newsImageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ListFavoriteResponse getFavoriteByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        Page<Favorite> list = favoriteRepository.findFavoritesByUserId(userId, pageable);
        List<FavoriteResponse> content = list.stream()
                .map(bt -> {
                    FavoriteResponse res = modelMapper.map(bt, FavoriteResponse.class);
                    res.getNews().setImageUrls(
                            newsImageRepository.findImageUrlByListingId(res.getNews().getId())
                    );
                    return res;
                })
                .collect(Collectors.toList());
        ListFavoriteResponse response = new ListFavoriteResponse();
        response.setContent(content);
        response.setPageNo(list.getNumber());
        response.setPageSize(list.getSize());
        response.setTotalElements(list.getTotalElements());
        response.setTotalPages(list.getTotalPages());
        return response;
    }

    @Override
    public void addNewsToFavorite(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        News news = newsRepository.findByIdNotDeleted(newsId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "News not found with id: " + newsId));
        favoriteRepository.findByUserIdAndNewsId(userId, newsId)
                .ifPresent(w -> {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Already exists in wishlist");
                });
        Favorite newFav = new Favorite();
        newFav.setUser(user);
        newFav.setNews(news);
        newFav.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(newFav);
    }

    @Override
    public void removeNewsFromFavorite(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "News not found with id: " + newsId));
        Favorite existing = favoriteRepository.findByUserIdAndNewsId(userId, newsId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Already removed from wishlist"));
        favoriteRepository.delete(existing);
    }

    @Override
    public void clearWishlist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        favoriteRepository.clearFavorite(userId);
    }
}

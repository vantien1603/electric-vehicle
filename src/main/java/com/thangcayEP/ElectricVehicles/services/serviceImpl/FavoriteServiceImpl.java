package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.Favorite;
import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.response.FavoriteResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListFavoriteResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.repositories.FavoriteRepository;
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
    private ModelMapper modelMapper;

    public FavoriteServiceImpl(ModelMapper modelMapper, UserRepository userRepository, NewsRepository newsRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public ListFavoriteResponse getFavoriteByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        Page<Favorite> list = favoriteRepository.findActiveFavoritesByUserId(userId, pageable);
        List<FavoriteResponse> content = list.stream().map(bt -> modelMapper.map(bt, FavoriteResponse.class)).collect(Collectors.toList());
        ListFavoriteResponse response = new ListFavoriteResponse();
        response.setContent(content);
        response.setPageNo(list.getNumber());
        response.setPageSize(list.getSize());
        response.setTotalElements(list.getTotalElements());
        response.setTotalPages(list.getTotalPages());
        return response;
    }

    @Override
    public String toggleFavorite(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        News news = newsRepository.findByIdNotDeleted(newsId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "News not found with id: " + newsId));
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndNewsId(userId, newsId);

        if (existing.isPresent()) {
            Favorite fav = existing.get();
            if ("ACTIVE".equals(fav.getStatus())) {
                fav.setStatus("DELETED");
                favoriteRepository.save(fav);
                return "Removed from favorites";
            } else {
                fav.setStatus("ACTIVE");
                favoriteRepository.save(fav);
                return "Added back to favorites";
            }
        } else {
            Favorite newFav = new Favorite();
            newFav.setUser(user);
            newFav.setNews(news);
            newFav.setStatus("ACTIVE");
            newFav.setCreatedAt(LocalDateTime.now());
            favoriteRepository.save(newFav);
            return "Added to favorites";
        }
    }
}

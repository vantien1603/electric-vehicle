package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.cloudinary.Api;
import com.thangcayEP.ElectricVehicles.model.entity.Categories;
import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.NewsImage;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.repositories.CategoriesRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsImageRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsRepository;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.services.NewsService;
import jakarta.persistence.criteria.Join;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private UserRepository userRepository;
    private NewsRepository newsRepository;
    private ModelMapper modelMapper;
    private NewsImageRepository newsImageRepository;
    private CategoriesRepository categoriesRepository;
    private CloudinaryService cloudinaryService;

    public NewsServiceImpl(CloudinaryService cloudinaryService, CategoriesRepository categoriesRepository, UserRepository userRepository, NewsRepository newsRepository, ModelMapper modelMapper, NewsImageRepository newsImageRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
        this.newsImageRepository = newsImageRepository;
        this.cloudinaryService = cloudinaryService;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public NewsResponse createNews(Long userId, NewsRequest newsRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find user with this id"));
        News news = new News();
        news.setUser(user);
        news.setTitle(newsRequest.getTitle());
        news.setDescription(newsRequest.getDescription());
        news.setPrice(newsRequest.getPrice());
        news.setCreatedAt(LocalDateTime.now());
        news.setStatus("PENDING");
        Categories cate = categoriesRepository.findById(newsRequest.getCategoryId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Can not find category with this id"));
        news.setCategory(cate);
        if (cate.getId() == 1) {
            news.setColor(null);
            news.setVehicleStatus(null);
            news.setVehicleBrand(null);
            news.setVehicleModel(null);
            news.setTopSpeed(null);
            news.setDistanceTraveled(null);
            news.setVehicleYear(null);
            news.setVehicleBatteryCapacity(null);
        }
        news.setColor(newsRequest.getColor());
        news.setContactPhone(newsRequest.getContactPhone());
        news.setVehicleStatus(newsRequest.getVehicleStatus());
        news.setVehicleBrand(newsRequest.getVehicleBrand());
        news.setVehicleModel(newsRequest.getVehicleModel());
        news.setTopSpeed(newsRequest.getTopSpeed());
        news.setDistanceTraveled(newsRequest.getDistanceTraveled());
        news.setVehicleYear(newsRequest.getVehicleYear());
        news.setLocation(newsRequest.getLocation());
        news.setVehicleBatteryCapacity(newsRequest.getVehicleBatteryCapacity());
        News news1 = newsRepository.save(news);
        List<String> imageUrls = new ArrayList<>();

        if (newsRequest.getFiles() != null || newsRequest.getFiles().isEmpty()) {
            for (MultipartFile file : newsRequest.getFiles()) {
                NewsImage img = new NewsImage();
                String url = null;
                try {
                    url = cloudinaryService.uploadFile(file);
                } catch (IOException e) {
                    throw new ApiException(HttpStatus.BAD_GATEWAY, e.getMessage());
                }
                img.setNews(news1);
                img.setImageUrl(url);
                img.setCreatedAt(LocalDateTime.now());
                newsImageRepository.save(img);
                imageUrls.add(url);
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "The news must be include images");
        }
        NewsResponse response = modelMapper.map(news1, NewsResponse.class);
        response.setImageUrls(imageUrls);
        return response;
    }

    @Override
    public NewsResponse updateNews(Long id, NewsRequest newsRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "News not found with id " + id));

        news.setDescription(newsRequest.getDescription());
        news.setPrice(newsRequest.getPrice());
        news.setVehicleBrand(newsRequest.getVehicleBrand());
        news.setVehicleModel(newsRequest.getVehicleModel());
        news.setVehicleYear(newsRequest.getVehicleYear());
        news.setVehicleBatteryCapacity(newsRequest.getVehicleBatteryCapacity());

        List<NewsImage> currentImages = newsImageRepository.findByNewsIdAndNotDeleted(id);

        List<String> keepUrls = newsRequest.getImageUrls() != null ? newsRequest.getImageUrls() : List.of();

        for (NewsImage img : currentImages) {
            if (!keepUrls.contains(img.getImageUrl())) {
                img.setDeletedAt(LocalDateTime.now());
                newsImageRepository.save(img);
            }
        }

        if (newsRequest.getFiles() != null) {
            for (MultipartFile file : newsRequest.getFiles()) {

                NewsImage img = new NewsImage();
                String url = null;
                try {
                    url = cloudinaryService.uploadFile(file);
                } catch (IOException e) {
                    throw new ApiException(HttpStatus.BAD_GATEWAY, e.getMessage());
                }
                img.setNews(news);
                img.setImageUrl(url);
                img.setCreatedAt(LocalDateTime.now());
                newsImageRepository.save(img);
            }
        }
        newsRepository.save(news);
        NewsResponse response = modelMapper.map(news, NewsResponse.class);
        response.setImageUrls(
                newsImageRepository.findImageUrlByListingId(id));
        return response;
    }

    @Override
    public String deleteNews(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "News not found with id:" + id));
        if (news.getStatus().equals("DELETED")) throw new ApiException(HttpStatus.BAD_REQUEST, "News already deleted");
        news.setStatus("DELETED");
        return "News deleted successfuly";
    }

    @Override
    public NewsResponse getNewsById(Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "News not found with id: " + id);
        }
        NewsResponse response = modelMapper.map(news.get(), NewsResponse.class);
        response.setImageUrls(newsImageRepository.findImageUrlByListingId(news.get().getId()));
        return response;
    }

    @Override
    public ListNewsResponse getAllNews(int pageNo, int pageSize, String sortBy, String sortDir, String keyWord, Long categoryId, String vehicleStatus, BigDecimal maxPrice, String location) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<News> spec = Specification.where(null);

        if (vehicleStatus != null && !vehicleStatus.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                return cb.like(cb.lower(root.get("vehicleStatus")), "%" + vehicleStatus.toLowerCase() + "%");
            });
        }

        if (categoryId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), categoryId));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
            });
        }

        if (keyWord != null && !keyWord.isEmpty()) {
            String keywordLower = "%" + keyWord.toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), keywordLower),
                    cb.like(cb.lower(root.get("description")), keywordLower),
                    cb.like(cb.lower(root.get("vehicleBrand")), keywordLower),
                    cb.like(cb.lower(root.get("vehicleModel")), keywordLower)
            ));
        }


        Page<NewsResponse> news = newsRepository.findAll(spec, pageable).map(news1 -> {
            NewsResponse response = modelMapper.map(news1, NewsResponse.class);
            response.setImageUrls(newsImageRepository.findImageUrlByListingId(news1.getId()));
            return response;
        });

        List<NewsResponse> listOfNews = news.getContent();

        List<NewsResponse> content = listOfNews.stream().map(bt -> modelMapper.map(bt, NewsResponse.class)).collect(Collectors.toList());
        ListNewsResponse response = new ListNewsResponse();
        response.setContent(content);
        response.setPageNo(news.getNumber());
        response.setPageSize(news.getSize());
        response.setTotalElements(news.getTotalElements());
        response.setTotalPages(news.getTotalPages());
        return response;
    }

    @Override
    public ListNewsResponse getNewsByUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<NewsResponse> news = newsRepository.findByUserIdNotDeleted(userId, pageable).map(news1 -> {
            NewsResponse response = modelMapper.map(news1, NewsResponse.class);
            response.setImageUrls(newsImageRepository.findImageUrlByListingId(news1.getId()));
            return response;
        });

        List<NewsResponse> listOfNews = news.getContent();

        List<NewsResponse> content = listOfNews.stream().map(bt -> modelMapper.map(bt, NewsResponse.class)).collect(Collectors.toList());
        ListNewsResponse response = new ListNewsResponse();
        response.setContent(content);
        response.setPageNo(news.getNumber());
        response.setPageSize(news.getSize());
        response.setTotalElements(news.getTotalElements());
        response.setTotalPages(news.getTotalPages());
        response.setLast(news.isLast());
        return response;
    }

}

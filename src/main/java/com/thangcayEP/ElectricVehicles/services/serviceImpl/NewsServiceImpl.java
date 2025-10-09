package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.NewsImage;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.exception.ApiException;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.repositories.NewsImageRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsRepository;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.services.NewsService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private UserRepository userRepository;
    private NewsRepository newsRepository;
    private ModelMapper modelMapper;
    private NewsImageRepository newsImageRepository;
    private CloudinaryService cloudinaryService;

    public NewsServiceImpl(CloudinaryService cloudinaryService, UserRepository userRepository, NewsRepository newsRepository, ModelMapper modelMapper, NewsImageRepository newsImageRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
        this.newsImageRepository = newsImageRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        User user = userRepository.findById(newsRequest.getUserId()).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Can not find user with this id"));
        News news = new News();
        news.setUser(user);
        news.setDescription(newsRequest.getDescription());
        news.setPrice(newsRequest.getPrice());
        news.setCreatedAt(LocalDateTime.now());
        news.setStatus("PENDING");
        news.setVehicleType(newsRequest.getVehicleType());
        news.setVehicleBrand(newsRequest.getVehicleBrand());
        news.setVehicleModel(newsRequest.getVehicleModel());
        news.setVehicleMileage(newsRequest.getVehicleMileage());
        news.setVehicleYear(newsRequest.getVehicleYear());
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
        return null;
    }

    @Override
    public String deleteNews(Long id) {
        return "";
    }

    @Override
    public NewsResponse getNewsById(Long id) {
        return null;
    }

    @Override
    public ListNewsResponse getAllNews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<NewsResponse> news = newsRepository.findAll(pageable).map(news1 -> {
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
    public ListNewsResponse getNewsByUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

}

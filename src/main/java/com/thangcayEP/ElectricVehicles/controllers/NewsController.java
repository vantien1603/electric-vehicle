package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.services.NewsService;
import com.thangcayEP.ElectricVehicles.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<?> getAllNews(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        ListNewsResponse response = newsService.getAllNews(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<?> createNews (@Valid @ModelAttribute NewsRequest newsRequest){
        NewsResponse response = newsService.createNews(newsRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}

package com.thangcayEP.ElectricVehicles.controllers;

import com.thangcayEP.ElectricVehicles.model.payload.request.NewsApproveRequest;
import com.thangcayEP.ElectricVehicles.model.payload.request.NewsRequest;
import com.thangcayEP.ElectricVehicles.model.payload.response.ListNewsResponse;
import com.thangcayEP.ElectricVehicles.model.payload.response.NewsResponse;
import com.thangcayEP.ElectricVehicles.services.NewsService;
import com.thangcayEP.ElectricVehicles.utils.AppConstants;
import com.thangcayEP.ElectricVehicles.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<?> getAllNews(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
                                        @RequestParam(value = "keyWord", required = false) String keyWord,
                                        @RequestParam(value = "vehicleStatus", required = false) String vehicleStatus,
                                        @RequestParam(value = "category", required = false) Long categoryId,
                                        @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                        @RequestParam(value = "location", required = false) String location,
                                        @RequestParam(value = "status", required = false) String status) {
        ListNewsResponse response = newsService.getAllNews(pageNo, pageSize, sortBy, sortDir, keyWord, categoryId, vehicleStatus, maxPrice, location, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<?> createNews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @Valid @ModelAttribute NewsRequest newsRequest) {
        System.out.println(userDetails.getId());
        NewsResponse response = newsService.createNews(userDetails.getId(), newsRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        NewsResponse response = newsService.getNewsById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Long userId,
                                       @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        ListNewsResponse response = newsService.getNewsByUser(userId, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute NewsRequest newsRequest) {
        NewsResponse response = newsService.updateNews(id, newsRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        String response = newsService.deleteNews(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestBody NewsApproveRequest newsApproveRequest) {
        String response = newsService.approve(id, newsApproveRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getNewsStatistics() {
        Map<String, Long> stats = newsService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}

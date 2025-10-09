package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface NewsImageRepository extends JpaRepository<NewsImage, Long> {
    @Query("SELECT ni.imageUrl FROM NewsImage ni WHERE ni.news.id = :id")
    List<String> findImageUrlByListingId (Long id);
}

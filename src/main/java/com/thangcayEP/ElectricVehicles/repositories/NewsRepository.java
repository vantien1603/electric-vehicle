package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface NewsRepository extends JpaRepository<News, Long> {

}

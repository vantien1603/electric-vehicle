package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    @Query("SELECT n FROM News n WHERE n.user.id = :id AND n.status <> 'DELETED'")
    Page<News> findByUserIdNotDeleted(@Param("id") Long id, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.user.id = :id AND n.status <> 'DELETED'")
    List<News> findByUserIdNotDeletedList(@Param("id") Long id);

//    @Query("SELECT n FROM News n WHERE n.status <> 'DELETED'")
    Page<News> findAll(Specification<News> spec, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.id = :id AND n.status <> 'DELETED'")
    Optional<News> findByIdNotDeleted (@Param("id") Long id);

    long countByStatus (String status);
}

package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
//    @Query("SELECT ni.news FROM Favorite ni WHERE ni.user.id=:id and ni.status !='DELETED'")
//    Page<Favorite> findNewsByUserAndStatusNotDeleted (@Param("id") Long id, Pageable pageable);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.status = 'ACTIVE'")
    Page<Favorite> findActiveFavoritesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.news.id = :newsId AND f.status = 'ACTIVE'")
    Optional<Favorite> findByUserIdAndNewsId(@Param("userId") Long userId, @Param("newsId") Long newsId);
}

package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    @Query("SELECT n FROM Notifications n WHERE n.user.id = :id ORDER BY n.createdAt DESC")
    List<Notifications> findByUserIdOrderByCreatedAtDesc(@Param("id") Long id);
    Long countByUserIdAndIsReadFalse(Long userId);
    @Modifying
    @Query("UPDATE Notifications n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllAsReadByUserId(@Param("userId") Long userId);

}

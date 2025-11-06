package com.thangcayEP.ElectricVehicles.services;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.Notifications;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.payload.dto.NotificationsDto;

import java.util.List;

public interface NotificationService {
    Notifications createNotification(User user, String message, News news);
    List<NotificationsDto> getUserNotifications(Long userId);
    Long countUnread(Long userId);
    void markAsRead(Long id);
    void markAllRead(Long userId);
}

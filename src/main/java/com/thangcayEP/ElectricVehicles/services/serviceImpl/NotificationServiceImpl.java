package com.thangcayEP.ElectricVehicles.services.serviceImpl;

import com.thangcayEP.ElectricVehicles.model.entity.News;
import com.thangcayEP.ElectricVehicles.model.entity.Notifications;
import com.thangcayEP.ElectricVehicles.model.entity.User;
import com.thangcayEP.ElectricVehicles.model.payload.dto.NotificationsDto;
import com.thangcayEP.ElectricVehicles.repositories.NewsImageRepository;
import com.thangcayEP.ElectricVehicles.repositories.NewsRepository;
import com.thangcayEP.ElectricVehicles.repositories.NotificationRepository;
import com.thangcayEP.ElectricVehicles.repositories.UserRepository;
import com.thangcayEP.ElectricVehicles.services.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private UserRepository userRepository;
    private NewsRepository newsRepository;
    private NotificationRepository notificationRepository;
    private SimpMessagingTemplate messagingTemplate;
    private NewsImageRepository newsImageRepository;
    private ModelMapper modelMapper;

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate, NewsImageRepository newsImageRepository, UserRepository userRepository, NewsRepository newsRepository, NotificationRepository notificationRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.notificationRepository = notificationRepository;
        this.newsImageRepository = newsImageRepository;
        this.messagingTemplate = messagingTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public Notifications createNotification(User user, String message, News news) {
        Notifications notification = Notifications.builder()
                .user(user)
                .message(message)
                .news(news)
                .isRead(false)
                .build();
        Notifications saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/notifications",
                modelMapper.map(saved, NotificationsDto.class)
        );

        return saved;
    }

    @Override
    public List<NotificationsDto> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(nt -> {
                    NotificationsDto dto = modelMapper.map(nt, NotificationsDto.class);
                    dto.getNews().setImageUrls(newsImageRepository.findImageUrlByListingId(nt.getNews().getId()));
                    return dto;
                })
                .toList();
    }

    @Override
    public Long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    @Override
    public void markAllRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

}

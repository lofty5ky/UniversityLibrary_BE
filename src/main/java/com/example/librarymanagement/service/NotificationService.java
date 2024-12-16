package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.NotificationRequestDTO;
import com.example.librarymanagement.dto.response.NotificationResponseDTO;
import com.example.librarymanagement.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface NotificationService {

    Page<NotificationResponseDTO> findAllNotifications(Pageable pageable);

    Optional<NotificationResponseDTO> findById(Long notificationId);

    NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO);

    void deleteNotificationById(Long notificationId);

    List<NotificationResponseDTO> getAllNotificationsByUser(Long userId);

    Page<NotificationResponseDTO> searchNotifications(String userName, String email, NotificationType notificationType,
                                                      Timestamp startDate, Timestamp endDate, Pageable pageable);
}

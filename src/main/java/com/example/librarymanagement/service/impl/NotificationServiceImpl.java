package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.NotificationRequestDTO;
import com.example.librarymanagement.dto.response.NotificationResponseDTO;
import com.example.librarymanagement.enums.NotificationType;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.Notification;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.NotificationRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.NotificationService;
import com.example.librarymanagement.specification.NotificationSpecification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Override
    public Page<NotificationResponseDTO> findAllNotifications(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        return notifications.map(this::convertToResponseDTO);
    }

    @Override
    public Optional<NotificationResponseDTO> findById(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        return notification.map(this::convertToResponseDTO);
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        User user = userRepository.findById(notificationRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .title(notificationRequestDTO.getTitle())
                .notificationType(notificationRequestDTO.getType())
                .message(notificationRequestDTO.getMessage())
                .sentAt(new Timestamp(System.currentTimeMillis()))
                .isSend(true)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // (Logic gửi email)
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("Thư Viện - Trường đại học Nguyễn Tất Thành <ductinnguyen251@gmail.com>");
            helper.setTo(savedNotification.getUser().getEmail());
            helper.setSubject(savedNotification.getTitle());
            helper.setText(savedNotification.getMessage());

            mailSender.send(mimeMessage);

            log.info("Email has been sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return convertToResponseDTO(savedNotification);
    }

    @Override
    public void deleteNotificationById(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        return notifications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponseDTO> searchNotifications(String userName, String email, NotificationType notificationType,
                                                             Timestamp startDate, Timestamp endDate, Pageable pageable) {
        Specification<Notification> spec = NotificationSpecification.
                findByUserNameOrEmailAndType(userName, email, startDate, endDate, notificationType);
        Page<Notification> pageResult = notificationRepository.findAll(spec, pageable);
        return pageResult.map(this::convertToResponseDTO);
    }

    private NotificationResponseDTO convertToResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .notificationId(notification.getNotificationId())
                .userName(notification.getUser().getName())
                .email(notification.getUser().getEmail())
                .title(notification.getTitle())
                .type(notification.getNotificationType())
                .message(notification.getMessage())
                .sentAt(notification.getSentAt())
                .isSend(notification.getIsSend())
                .build();
    }
}


package com.example.librarymanagement.dto.response;

import com.example.librarymanagement.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO implements Serializable {

    @JsonProperty("notification_id")
    private Long notificationId;

    @JsonProperty("user_name")
    private String userName;

    private String email;

    private String title;

    private NotificationType type;

    private String message;

    @JsonProperty("sent_at")
    private Timestamp sentAt;

    @JsonProperty("is_send")
    private Boolean isSend;
}

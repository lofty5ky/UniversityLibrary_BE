package com.example.librarymanagement.dto.request;

import com.example.librarymanagement.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO implements Serializable {

    @NotNull(message = "User ID cannot be null")
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    private String title;

    private NotificationType type;
}

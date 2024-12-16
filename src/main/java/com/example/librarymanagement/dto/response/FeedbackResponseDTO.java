package com.example.librarymanagement.dto.response;

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
public class FeedbackResponseDTO implements Serializable {

    private Long id;

    @JsonProperty("user_name")
    private String userName;

    private String email;

    private String message;

    private String response;

    @JsonProperty("is_response")
    private Boolean isResponse;

    @JsonProperty("submitted_at")
    private Timestamp submittedAt;

    @JsonProperty("response_at")
    private Timestamp responseAt;
}

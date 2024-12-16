package com.example.librarymanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestDTO implements Serializable {

    @JsonProperty("user_id")
    private Long userId;

    private String message;

    private String response;
}

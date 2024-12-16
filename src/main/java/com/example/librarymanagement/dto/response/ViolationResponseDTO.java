package com.example.librarymanagement.dto.response;

import com.example.librarymanagement.enums.ViolationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationResponseDTO implements Serializable {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String userName;

    private String email;

    private String description;

    private LocalDate violationDate;

    private Boolean resolved;

    private LocalDate resolveAt;

    private ViolationType violationType;
}

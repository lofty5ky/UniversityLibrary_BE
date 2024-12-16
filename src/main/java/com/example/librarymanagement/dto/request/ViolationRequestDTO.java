package com.example.librarymanagement.dto.request;

import com.example.librarymanagement.enums.ViolationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ViolationRequestDTO implements Serializable {
    @NotNull(message = "User ID cannot be null")
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Violation date cannot be null")
    @JsonProperty("violation_date")
    private LocalDate violationDate;

    @NotNull(message = "Violation Type cannot be null")
    @JsonProperty("violation_type")
    private ViolationType violationType;

    private Boolean resolved;

}

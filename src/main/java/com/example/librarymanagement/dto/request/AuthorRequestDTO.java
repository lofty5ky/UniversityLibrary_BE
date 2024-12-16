package com.example.librarymanagement.dto.request;

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
public class AuthorRequestDTO implements Serializable {

    @NotBlank(message = "Author name cannot be blank")
    private String name;

    @NotBlank(message = "Biography cannot be blank")
    private String biography;

    @NotNull(message = "Birth date cannot be null")
    @JsonProperty("birth_date")
    private LocalDate birthDate;  // Định dạng ngày tháng năm
}
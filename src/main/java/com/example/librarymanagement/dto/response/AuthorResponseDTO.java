package com.example.librarymanagement.dto.response;

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
public class AuthorResponseDTO implements Serializable {
    private Long authorId;

    private String name;

    private String biography;

    @JsonProperty("birth_date")
    private LocalDate birthDate;
}
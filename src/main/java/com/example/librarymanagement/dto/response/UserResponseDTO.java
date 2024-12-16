package com.example.librarymanagement.dto.response;

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
public class UserResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String roleName;

    private int status;
}
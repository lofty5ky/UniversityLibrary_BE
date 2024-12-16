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
public class PublisherResponseDTO implements Serializable {
    private Long publisherId;

    private String name;

    private String address;

    @JsonProperty("contact_info")
    private String contactInfo;
}
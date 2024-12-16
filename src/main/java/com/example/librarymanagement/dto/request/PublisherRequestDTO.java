package com.example.librarymanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherRequestDTO implements Serializable {

    @NotBlank(message = "Publisher name cannot be blank")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Contact information cannot be blank")
    @Size(min = 10, max = 10)
    @JsonProperty("contact_info")
    private String contactInfo;  // Số điện thoại của nhà xuất bản
}
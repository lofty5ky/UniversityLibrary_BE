package com.example.librarymanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO implements Serializable {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Title cannot be blank")
    @JsonProperty("book_summary")
    private String bookSummary;

    @NotNull(message = "Publisher ID cannot be null")
    @JsonProperty("publisher_id")
    private Long publisherId;

    @NotNull(message = "Category ID cannot be null")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull(message = "Publication year cannot be null")
    @JsonProperty("publication_year")
    private Integer publicationYear;

    @NotNull(message = "Total quantity cannot be null")
    @JsonProperty("total_quantity")
    private Integer totalQuantity;

    private String image;

    @NotNull(message = "Author IDs cannot be null")
    @JsonProperty("author_ids")
    private List<Long> authorIds;
}
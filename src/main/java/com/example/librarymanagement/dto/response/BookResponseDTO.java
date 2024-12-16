package com.example.librarymanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO implements Serializable {

    private Long id;

    private String title;

    @JsonProperty("book_summary")
    private String bookSummary;

    @JsonProperty("publisher_name")
    private String publisherName;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("publication_year")
    private Integer publicationYear;

    @JsonProperty("total_quantity")
    private Integer totalQuantity;

    private String image;

    private Set<String> authors;

    @JsonProperty("author_biography")
    private List<String> authorBiography;

    @JsonProperty("available_quantity")
    private Integer availableQuantity;
}
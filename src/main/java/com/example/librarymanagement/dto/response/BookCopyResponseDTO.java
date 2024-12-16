package com.example.librarymanagement.dto.response;

import com.example.librarymanagement.enums.BookCondition;
import com.example.librarymanagement.enums.BookStatus;
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
public class BookCopyResponseDTO implements Serializable {
    @JsonProperty("copy_id")
    private Long copyId;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("book_image")
    private String bookImage;

    private BookCondition condition;

    private BookStatus status;
}
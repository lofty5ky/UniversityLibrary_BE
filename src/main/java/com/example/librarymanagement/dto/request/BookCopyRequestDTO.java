package com.example.librarymanagement.dto.request;

import com.example.librarymanagement.enums.BookCondition;
import com.example.librarymanagement.enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyRequestDTO implements Serializable {

    @NotNull
    @JsonProperty("book_id")
    Long bookId;

    @NotNull(message = "Condition cannot be null")
    private BookCondition condition;

    @NotNull(message = "Status cannot be null")
    private BookStatus status;
}
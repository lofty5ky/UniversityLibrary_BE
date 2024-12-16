package com.example.librarymanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BorrowRecordRequestDTO implements Serializable {

    @NotNull(message = "User ID cannot be null")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "Book ID cannot be null")
    @JsonProperty("book_id")
    private Long bookId;

    @NotNull(message = "Borrow date cannot be null")
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @NotNull(message = "Due date cannot be null")
    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("copy_id")
    private Long copyId;
}
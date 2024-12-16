package com.example.librarymanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecordResponseDTO {
    @JsonProperty("record_id")
    private Long recordId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    private String email;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("copy_id")
    private Long copyId;

    @JsonProperty("book_condition")
    private String bookCondition;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("return_date")
    private LocalDate returnDate;

    private String status;
}

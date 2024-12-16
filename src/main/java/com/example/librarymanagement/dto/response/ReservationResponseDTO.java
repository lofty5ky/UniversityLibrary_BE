package com.example.librarymanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO implements Serializable {
    private Long id;

    @JsonProperty("user_name")
    private String userName;

    private String email;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("approved_copy_id")
    private Long approvedCopyId;

    @JsonProperty("reservation_date")
    private LocalDateTime reservationDate;

    private String status;
}

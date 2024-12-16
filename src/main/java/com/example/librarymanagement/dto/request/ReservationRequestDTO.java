package com.example.librarymanagement.dto.request;

import com.example.librarymanagement.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO implements Serializable {

    @NotNull(message = "User ID cannot be null")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "Book ID cannot be null")
    @JsonProperty("book_id")
    private Long bookId;

    @NotNull(message = "Reservation date cannot be null")
    @JsonProperty("reservation_date")
    private LocalDateTime reservationDate;

    private ReservationStatus status;

    @JsonProperty("approved_copy_id")
    private Long approvedCopyId;
}

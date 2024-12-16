package com.example.librarymanagement.dto.request;

import com.example.librarymanagement.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationUpdateRequestDTO implements Serializable {
    private ReservationStatus status;

    @JsonProperty("approved_copy_id")
    private Long approvedCopyId;
}

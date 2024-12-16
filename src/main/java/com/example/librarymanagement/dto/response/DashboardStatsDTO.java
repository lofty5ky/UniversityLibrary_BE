package com.example.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO implements Serializable {

    private Long totalUsers;

    private Long totalBooks;

    private Long totalBorrows;

    private Long totalReservations;

    private Long totalViolations;
}

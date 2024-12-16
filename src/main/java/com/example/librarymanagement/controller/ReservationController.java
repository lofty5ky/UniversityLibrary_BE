package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.ReservationRequestDTO;
import com.example.librarymanagement.dto.request.ReservationUpdateRequestDTO;
import com.example.librarymanagement.dto.response.ReservationResponseDTO;
import com.example.librarymanagement.enums.ReservationStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationResponseDTO>> getAllReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ReservationResponseDTO> reservations =
                    reservationService.findAllReservations(status, startDate, endDate,
                            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationId")));
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long reservationId) {
        try {
            ReservationResponseDTO reservation = reservationService.findById(reservationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
            return ResponseEntity.ok(reservation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequest) {
        try {
            ReservationResponseDTO response = reservationService.createReservation(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật yêu cầu mượn sách
    @PutMapping("/{reservationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationUpdateRequestDTO reservationRequest) {
        try {
            ReservationResponseDTO response = reservationService.updateReservation(reservationId, reservationRequest);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa yêu cầu mượn sách
    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        try {
            reservationService.deleteReservationById(reservationId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Hủy yêu cầu mượn sách
    @PutMapping("/{reservationId}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<ReservationResponseDTO>> getReservationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ReservationResponseDTO> reservations = reservationService.findReservationsByUserId(userId,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationId")));
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

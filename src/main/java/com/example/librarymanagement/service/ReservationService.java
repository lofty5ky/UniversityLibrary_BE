package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.ReservationRequestDTO;
import com.example.librarymanagement.dto.request.ReservationUpdateRequestDTO;
import com.example.librarymanagement.dto.response.ReservationResponseDTO;
import com.example.librarymanagement.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationService {

    // Tạo yêu cầu mượn sách mới
    ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequest);

    // Lấy thông tin yêu cầu mượn sách theo ID
    Optional<ReservationResponseDTO> findById(Long reservationId);

    // Lấy tất cả yêu cầu mượn sách có phân trang, hỗ trợ tìm kiếm theo status và date
    Page<ReservationResponseDTO> findAllReservations(ReservationStatus status, LocalDate startDate, LocalDate endDate,
                                                     Pageable pageable);

    // Lấy tất cả yêu cầu mượn sách của người dùng theo userId
    Page<ReservationResponseDTO> findReservationsByUserId(Long userId, Pageable pageable);

    // Cập nhật yêu cầu mượn sách
    ReservationResponseDTO updateReservation(Long reservationId, ReservationUpdateRequestDTO reservationRequest);

    // Xóa yêu cầu mượn sách
    void deleteReservationById(Long reservationId);

    // Hủy yêu cầu mượn sách
    void cancelReservation(Long reservationId);
}

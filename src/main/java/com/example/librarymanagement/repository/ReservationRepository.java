package com.example.librarymanagement.repository;

import com.example.librarymanagement.enums.ReservationStatus;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.Reservation;
import com.example.librarymanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    Page<Reservation> findByUserUserId(Long userId, Pageable pageable);

    boolean existsByUserAndBookAndStatus(User user, Book book, ReservationStatus status);

    long countByUserAndStatus(User user, ReservationStatus status);

    // Tìm tất cả các yêu cầu đặt sách theo trạng thái
    List<Reservation> findByStatus(ReservationStatus status);

    // Tìm tất cả các yêu cầu đặt sách của một sách
    List<Reservation> findByBookBookId(Long bookId);
}
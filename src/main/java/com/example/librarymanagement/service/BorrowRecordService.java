package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.response.BorrowRecordResponseDTO;
import com.example.librarymanagement.enums.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BorrowRecordService {

    Page<BorrowRecordResponseDTO> findBorrowRecords(Long userId, String userName, String email, BorrowStatus status, Long copyId,
                                                    LocalDate startDate, LocalDate endDate, Pageable pageable);

    public void updateBorrowStatusToReturned(Long borrowRecordId);

    void deleteBorrowRecordById(Long borrowRecordId);

    long countBorrowRecords();

    long countUnreturnedBooks();

    List<Map<String, Object>> getBorrowedByMonth();

    List<Map<String, Object>> getTop5BorrowedBooks();
}

package com.example.librarymanagement.controller;


import com.example.librarymanagement.dto.response.BorrowRecordResponseDTO;
import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.BorrowRecordService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<BorrowRecordResponseDTO>> searchBorrowRecords(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) BorrowStatus status,
            @RequestParam(required = false) Long copyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BorrowRecordResponseDTO> borrowRecords = borrowRecordService.findBorrowRecords(
                userId, userName, email, status, copyId, startDate, endDate,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordId")));
        return ResponseEntity.ok(borrowRecords);
    }

    @PutMapping("/{borrowRecordId}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBorrowStatusToReturned(@PathVariable Long borrowRecordId) {
        try {
            borrowRecordService.updateBorrowStatusToReturned(borrowRecordId);
            return ResponseEntity.ok("Cập nhật trạng thái mượn sách thành công!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Borrow record not found!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred!");
        }
    }

    @DeleteMapping("/{borrowRecordId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBorrowRecord(@PathVariable Long borrowRecordId) {
        try {
            borrowRecordService.deleteBorrowRecordById(borrowRecordId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> countBorrowRecords() {
        try {
            long count = borrowRecordService.countBorrowRecords();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/total-unreturned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> countUnreturnedBorrowRecords() {
        try {
            long count = borrowRecordService.countUnreturnedBooks();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/borrowed-by-month")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getBorrowedByMonth() {
        try {
            List<Map<String, Object>> borrowedByMonth = borrowRecordService.getBorrowedByMonth();
            return ResponseEntity.ok(borrowedByMonth);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top-borrowed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getTopBorrowedBooks() {
        try {
            List<Map<String, Object>> topBooks = borrowRecordService.getTop5BorrowedBooks();
            return ResponseEntity.ok(topBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


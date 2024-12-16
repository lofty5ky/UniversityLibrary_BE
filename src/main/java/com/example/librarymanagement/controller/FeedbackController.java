package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.FeedbackRequestDTO;
import com.example.librarymanagement.dto.response.FeedbackResponseDTO;
import com.example.librarymanagement.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDTO>> searchFeedbacks(
            @RequestParam(required = false) Boolean isResponse,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp startTimestamp = startDate != null ? new Timestamp(dateFormat.parse(startDate).getTime()) : null;
            Timestamp endTimestamp = endDate != null ? new Timestamp(dateFormat.parse(endDate).getTime()) : null;

            Page<FeedbackResponseDTO> feedbacks = feedbackService.searchFeedbacks(
                    isResponse, email, startTimestamp, endTimestamp,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "feedbackId"))
            );
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Lấy tất cả phản hồi với phân trang
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDTO>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<FeedbackResponseDTO> feedbacks = feedbackService.getAllFeedbacks(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "feedbackId")));
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Lấy phản hồi theo ID
    @GetMapping("/{feedbackId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(@PathVariable Long feedbackId) {
        try {
            FeedbackResponseDTO feedback = feedbackService.getFeedbackById(feedbackId)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới phản hồi
    @PostMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Valid @RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        try {
            FeedbackResponseDTO feedback = feedbackService.createFeedback(feedbackRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Cập nhật phản hồi
    @PutMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(@PathVariable Long feedbackId,
                                                              @Valid @RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        try {
            FeedbackResponseDTO feedback = feedbackService.updateFeedback(feedbackId, feedbackRequestDTO);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Xóa phản hồi
    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long feedbackId) {
        try {
            feedbackService.deleteFeedback(feedbackId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy tất cả phản hồi của một người dùng
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacksByUserId(@PathVariable Long userId) {
        try {
            List<FeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksByUserId(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

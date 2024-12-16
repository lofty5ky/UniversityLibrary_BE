package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.FeedbackRequestDTO;
import com.example.librarymanagement.dto.response.FeedbackResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    Page<FeedbackResponseDTO> searchFeedbacks(Boolean isResponse, String email,
                                              Timestamp startDate, Timestamp endDate, Pageable pageable);

    // Tạo mới một phản hồi
    FeedbackResponseDTO createFeedback(FeedbackRequestDTO feedbackRequestDTO);

    // Lấy tất cả phản hồi với phân trang
    Page<FeedbackResponseDTO> getAllFeedbacks(Pageable pageable);

    // Lấy phản hồi theo ID
    Optional<FeedbackResponseDTO> getFeedbackById(Long feedbackId);

    // Lấy tất cả phản hồi của một người dùng
    List<FeedbackResponseDTO> getFeedbacksByUserId(Long userId);

    // Cập nhật phản hồi của admin
    FeedbackResponseDTO updateFeedback(Long feedbackId, FeedbackRequestDTO feedbackRequestDTO);

    // Xóa phản hồi theo ID
    void deleteFeedback(Long feedbackId);
}
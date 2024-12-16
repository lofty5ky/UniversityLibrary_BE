package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.FeedbackRequestDTO;
import com.example.librarymanagement.dto.response.FeedbackResponseDTO;
import com.example.librarymanagement.model.Feedback;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.FeedbackRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.FeedbackService;
import com.example.librarymanagement.specification.FeedbackSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Override
    public Page<FeedbackResponseDTO> searchFeedbacks(Boolean isResponse, String userName,
                                                     Timestamp startDate, Timestamp endDate, Pageable pageable) {
        Specification<Feedback> specification = FeedbackSpecification.findByCriteria(isResponse, userName, startDate, endDate);

        Page<Feedback> feedbackPage = feedbackRepository.findAll(specification, pageable);

        return feedbackPage.map(this::convertToResponseDTO);
    }

    @Override
    public FeedbackResponseDTO createFeedback(FeedbackRequestDTO feedbackRequestDTO) {
        User user = userRepository.findById(feedbackRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = Feedback.builder()
                .user(user)
                .message(feedbackRequestDTO.getMessage())
                .isResponse(false)
                .submittedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return convertToResponseDTO(savedFeedback);
    }

    @Override
    public Page<FeedbackResponseDTO> getAllFeedbacks(Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findAll(pageable);
        return feedbacks.map(this::convertToResponseDTO);
    }

    @Override
    public Optional<FeedbackResponseDTO> getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .map(this::convertToResponseDTO);
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbacksByUserId(Long userId) {
        List<Feedback> feedbacks = feedbackRepository.findByUserUserIdOrderByFeedbackIdDesc(userId);
        return feedbacks.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackResponseDTO updateFeedback(Long feedbackId, FeedbackRequestDTO feedbackRequestDTO) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setResponseMessage(feedbackRequestDTO.getResponse());
        feedback.setResponse(true);
        feedback.setResponseAt(new Timestamp(System.currentTimeMillis()));

        Feedback updatedFeedback = feedbackRepository.save(feedback);

        return convertToResponseDTO(updatedFeedback);
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    // Helper method to convert Feedback entity to FeedbackResponseDTO
    private FeedbackResponseDTO convertToResponseDTO(Feedback feedback) {
        return FeedbackResponseDTO.builder()
                .id(feedback.getFeedbackId())
                .userName(feedback.getUser().getName())
                .email(feedback.getUser().getEmail())
                .message(feedback.getMessage())
                .response(feedback.getResponseMessage())
                .isResponse(feedback.isResponse())
                .submittedAt(feedback.getSubmittedAt())
                .responseAt(feedback.getResponseAt())
                .build();
    }
}

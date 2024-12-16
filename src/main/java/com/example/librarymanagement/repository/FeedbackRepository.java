package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
//    List<Feedback> findByUserUserId(Long userId);

    List<Feedback> findByUserUserIdOrderByFeedbackIdDesc(Long userId);
}

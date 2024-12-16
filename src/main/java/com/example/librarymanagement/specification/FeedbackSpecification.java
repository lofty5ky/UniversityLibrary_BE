package com.example.librarymanagement.specification;

import com.example.librarymanagement.model.Feedback;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FeedbackSpecification {

    // Phương thức tìm kiếm với nhiều điều kiện
    public static Specification<Feedback> findByCriteria(Boolean isResponse, String email, Timestamp startDate, Timestamp endDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Kiểm tra trạng thái isResponse
            if (isResponse != null) {
                predicates.add(criteriaBuilder.equal(root.get("isResponse"), isResponse));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("email")), "%" + email.toLowerCase() + "%"));
            }

            // Kiểm tra thời gian từ
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("submittedAt"), startDate));
            }

            // Kiểm tra thời gian đến
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("submittedAt"), endDate));
            }

            // Trả về kết quả với tất cả điều kiện
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

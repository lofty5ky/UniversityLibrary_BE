package com.example.librarymanagement.specification;

import com.example.librarymanagement.enums.ViolationType;
import com.example.librarymanagement.model.Violation;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViolationSpecification {

    public static Specification<Violation> findByCriteria(
            Boolean resolved, ViolationType violationType, LocalDate startDate, LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Kiểm tra trạng thái đã giải quyết
            if (resolved != null) {
                predicates.add(criteriaBuilder.equal(root.get("resolved"), resolved));
            }

            // Kiểm tra loại vi phạm
            if (violationType != null) {
                predicates.add(criteriaBuilder.equal(root.get("violationType"), violationType));
            }

            // Kiểm tra ngày vi phạm
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("violationDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("violationDate"), endDate));
            }

            // Trả về tất cả các predicate kết hợp với nhau
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
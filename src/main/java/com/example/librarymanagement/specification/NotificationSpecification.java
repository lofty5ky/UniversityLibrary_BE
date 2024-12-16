package com.example.librarymanagement.specification;

import com.example.librarymanagement.enums.NotificationType;
import com.example.librarymanagement.model.Notification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<Notification> findByUserNameOrEmailAndType(
            String userName, String email, Timestamp startDate, Timestamp endDate, NotificationType notificationType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userName != null && !userName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("user").get("userName"), "%" + userName + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("user").get("email"), "%" + email + "%"));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sentAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sentAt"), endDate));
            }

            if (notificationType != null) {
                predicates.add(criteriaBuilder.equal(root.get("notificationType"), notificationType));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

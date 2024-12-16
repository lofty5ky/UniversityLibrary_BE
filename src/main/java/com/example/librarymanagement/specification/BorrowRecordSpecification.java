package com.example.librarymanagement.specification;

import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.model.BorrowRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BorrowRecordSpecification {

    public static Specification<BorrowRecord> byUserId(Long userId) {
        return (root, query, cb) -> userId != null ? cb.equal(root.get("user").get("userId"), userId) : null;
    }

    public static Specification<BorrowRecord> byUserName(String userName) {
        return (root, query, cb) ->
                (userName != null && !userName.isEmpty())
                        ? cb.like(cb.lower(root.get("user").get("name")), "%" + userName.toLowerCase() + "%")
                        : cb.conjunction();
    }

    public static Specification<BorrowRecord> byEmail(String email) {
        return (root, query, cb) ->
                (email != null && !email.isEmpty())
                        ? cb.like(cb.lower(root.get("user").get("email")), "%" + email.toLowerCase() + "%")
                        : cb.conjunction();
    }

    public static Specification<BorrowRecord> byStatus(BorrowStatus status) {
        return (root, query, cb) -> status != null ? cb.equal(root.get("status"), status) : null;
    }

    public static Specification<BorrowRecord> byCopyId(Long copyId) {
        return (root, query, cb) -> copyId != null ? cb.equal(root.get("copy").get("copyId"), copyId) : null;
    }

    public static Specification<BorrowRecord> byBorrowDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> (startDate != null && endDate != null)
                ? cb.between(root.get("borrowDate"), startDate, endDate)
                : null;
    }
}
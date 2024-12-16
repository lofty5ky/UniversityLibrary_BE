package com.example.librarymanagement.specification;

import com.example.librarymanagement.model.BookCopy;
import org.springframework.data.jpa.domain.Specification;

public class BookCopySpecification {
    public static Specification<BookCopy> byCondition(String condition) {
        return (root, query, cb) -> condition != null ? cb.equal(root.get("bookCondition"), condition) : null;
    }

    public static Specification<BookCopy> byStatus(String status) {
        return (root, query, cb) -> status != null ? cb.equal(root.get("status"), status) : null;
    }

    public static Specification<BookCopy> byBookId(Long bookId) {
        return (root, query, cb) -> bookId != null ? cb.equal(root.get("book").get("bookId"), bookId) : null;
    }

    public static Specification<BookCopy> byCopyId(Long copyId) {
        return (root, query, cb) -> copyId != null ? cb.equal(root.get("copyId"), copyId) : null;
    }
}

package com.example.librarymanagement.specification;

import com.example.librarymanagement.enums.CardStatus;
import com.example.librarymanagement.model.LibraryCard;
import org.springframework.data.jpa.domain.Specification;

public class LibraryCardSpecification {

    public static Specification<LibraryCard> byUserName(String userName) {
        return (root, query, cb) -> userName != null
                ? cb.like(cb.lower(root.get("user").get("name")), "%" + userName.toLowerCase() + "%")
                : null;
    }

    public static Specification<LibraryCard> byUserEmail(String email) {
        return (root, query, cb) -> email != null
                ? cb.equal(cb.lower(root.get("user").get("email")), email.toLowerCase())
                : null;
    }

    public static Specification<LibraryCard> byStatus(CardStatus status) {
        return (root, query, cb) -> status != null ? cb.equal(root.get("status"), status) : null;
    }
}
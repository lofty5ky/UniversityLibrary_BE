package com.example.librarymanagement.specification;

import com.example.librarymanagement.model.Author;
import com.example.librarymanagement.model.Book;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> byTitle(String title) {
        return (root, query, cb) -> title != null ? cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%") : null;
    }

    public static Specification<Book> byAuthorName(String authorName) {
        return (root, query, cb) -> {
            if (authorName == null) {
                return null;
            }
            Join<Book, Author> authors = root.join("authors", JoinType.INNER);
            return cb.like(cb.lower(authors.get("name")), "%" + authorName.toLowerCase() + "%");
        };
    }

    public static Specification<Book> byPublicationYear(Integer publicationYear) {
        return (root, query, cb) -> publicationYear != null ? cb.equal(root.get("publicationYear"), publicationYear) : null;
    }

    public static Specification<Book> byCategoryId(Long categoryId) {
        return (root, query, cb) -> categoryId != null ? cb.equal(root.get("category").get("id"), categoryId) : null;
    }
}

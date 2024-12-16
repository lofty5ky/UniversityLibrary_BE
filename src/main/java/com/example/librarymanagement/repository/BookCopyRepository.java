package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long>, JpaSpecificationExecutor<BookCopy> {
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.bookId = :bookId AND bc.status = 'AVAILABLE'")
    int countAvailableCopiesByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(c) FROM BookCopy c")
    long countTotalBookCopies();
}
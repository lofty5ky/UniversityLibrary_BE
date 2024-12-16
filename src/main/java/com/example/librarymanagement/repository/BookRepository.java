package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT COUNT(b) FROM Book b")
    long countTotalBooks();

    @Query("SELECT a.name FROM Author a JOIN a.books b WHERE b.bookId = :bookId")
    List<String> findAuthorsByBookId(@Param("bookId") Long bookId);

    // Tìm sách có số lượng lớn hơn một giá trị nhất định
    List<Book> findByTotalQuantityGreaterThan(Integer quantity);
}
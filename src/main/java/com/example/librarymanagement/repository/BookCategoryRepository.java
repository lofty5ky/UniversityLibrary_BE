package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    // Tìm danh mục sách theo tên
    Optional<BookCategory> findByName(String name);
}
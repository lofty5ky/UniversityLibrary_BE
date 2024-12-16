package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.BookCategoryRequestDTO;
import com.example.librarymanagement.dto.response.BookCategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookCategoryService {
    BookCategoryResponseDTO createBookCategory(BookCategoryRequestDTO categoryRequest);

    // Tìm danh mục sách theo ID
    Optional<BookCategoryResponseDTO> findCategoryById(Long categoryId);

    // Tìm tất cả danh mục sách
    Page<BookCategoryResponseDTO> findAllCategories(Pageable pageable);

    List<BookCategoryResponseDTO> findAllCategoriesForUsers();

    // Cập nhật danh mục sách
    BookCategoryResponseDTO updateBookCategory(Long categoryId, BookCategoryRequestDTO categoryRequest);

    // Xóa danh mục sách theo ID
    void deleteBookCategoryById(Long categoryId);
}

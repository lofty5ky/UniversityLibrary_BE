package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.BookCategoryRequestDTO;
import com.example.librarymanagement.dto.response.BookCategoryResponseDTO;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.BookCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-categories")
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    // Lấy tất cả danh mục sách
    @GetMapping()
    public ResponseEntity<Page<BookCategoryResponseDTO>> getAllBookCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<BookCategoryResponseDTO> categories = bookCategoryService.findAllCategories(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "categoryId")));
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/public")
    public ResponseEntity<List<BookCategoryResponseDTO>> getAllBookCategoriesForUsers() {
        try {
            List<BookCategoryResponseDTO> categories = bookCategoryService.findAllCategoriesForUsers();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Lấy danh mục sách theo ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<BookCategoryResponseDTO> getBookCategoryById(@PathVariable Long categoryId) {
        try {
            BookCategoryResponseDTO category = bookCategoryService.findCategoryById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tạo danh mục sách mới
    @PostMapping()
    public ResponseEntity<?> createBookCategory(@Valid @RequestBody BookCategoryRequestDTO categoryRequest) {
        try {
            BookCategoryResponseDTO category = bookCategoryService.createBookCategory(categoryRequest);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật danh mục sách
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateBookCategory(@PathVariable Long categoryId,
                                                @Valid @RequestBody BookCategoryRequestDTO categoryRequest) {
        try {
            BookCategoryResponseDTO category = bookCategoryService.updateBookCategory(categoryId, categoryRequest);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa danh mục sách
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteBookCategory(@PathVariable Long categoryId) {
        try {
            bookCategoryService.deleteBookCategoryById(categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
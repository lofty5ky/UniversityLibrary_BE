package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.BookCategoryRequestDTO;
import com.example.librarymanagement.dto.response.BookCategoryResponseDTO;
import com.example.librarymanagement.model.BookCategory;
import com.example.librarymanagement.repository.BookCategoryRepository;
import com.example.librarymanagement.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;

    @Override
    public BookCategoryResponseDTO createBookCategory(BookCategoryRequestDTO categoryRequest) {
        if (bookCategoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            throw new RuntimeException("Tên thể loại này đã được tạo");
        }

        BookCategory category = BookCategory.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();

        BookCategory savedCategory = bookCategoryRepository.save(category);

        return convertToResponseDTO(savedCategory);
    }

    @Override
    public Optional<BookCategoryResponseDTO> findCategoryById(Long categoryId) {
        return bookCategoryRepository.findById(categoryId)
                .map(this::convertToResponseDTO);
    }

    @Override
    public Page<BookCategoryResponseDTO> findAllCategories(Pageable pageable) {
        Page<BookCategory> categories = bookCategoryRepository.findAll(pageable);
        return categories.map(this::convertToResponseDTO);
    }

    @Override
    public List<BookCategoryResponseDTO> findAllCategoriesForUsers() {
        List<BookCategory> categories = bookCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "categoryId"));
        return categories.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookCategoryResponseDTO updateBookCategory(Long categoryId, BookCategoryRequestDTO categoryRequest) {
        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));

        if (!category.getName().equals(categoryRequest.getName())) {
            if (bookCategoryRepository.findByName(categoryRequest.getName()).isPresent()) {
                throw new RuntimeException("Tên thể loại này đã tồn tại");
            }
        }
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        BookCategory updatedCategory = bookCategoryRepository.save(category);

        return convertToResponseDTO(updatedCategory);
    }

    @Override
    public void deleteBookCategoryById(Long categoryId) {
        bookCategoryRepository.deleteById(categoryId);
    }

    private BookCategoryResponseDTO convertToResponseDTO(BookCategory category) {
        return BookCategoryResponseDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}

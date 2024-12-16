package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.AuthorRequestDTO;
import com.example.librarymanagement.dto.response.AuthorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    // Thêm tác giả mới
    AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO);

    // Tìm tác giả theo ID
    Optional<AuthorResponseDTO> findById(Long authorId);

    // Tìm tất cả tác giả
    Page<AuthorResponseDTO> findAllAuthors(Pageable pageable);

    // Tìm tác giả theo tên (không phân biệt chữ hoa/chữ thường)
    List<AuthorResponseDTO> findByName(String name);

    AuthorResponseDTO updateAuthor(Long authorId, AuthorRequestDTO authorRequestDTO);

    // Xóa tác giả theo ID
    void deleteAuthorById(Long authorId);
}
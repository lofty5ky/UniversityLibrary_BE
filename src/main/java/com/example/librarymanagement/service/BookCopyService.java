package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.BookCopyRequestDTO;
import com.example.librarymanagement.dto.response.BookCopyResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCopyService {
    Page<BookCopyResponseDTO> findBookCopies(String condition, String status, Long bookId, Long copyId, Pageable pageable);

    BookCopyResponseDTO createBookCopy(BookCopyRequestDTO bookCopyRequestDTO);

    BookCopyResponseDTO getBookCopyById(Long copyId);

    BookCopyResponseDTO updateBookCopy(Long copyId, BookCopyRequestDTO bookCopyRequestDTO);

    void deleteBookCopyById(Long copyId);

    void createBookCopiesForNewBook(Long bookId, int totalQuantity);

    long getTotalBookCopiesCount();
}
package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.BookRequestDTO;
import com.example.librarymanagement.dto.response.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Page<BookResponseDTO> findBooks(String title, String authorName,
                                    Integer publicationYear, Long categoryId, Pageable pageable);


    Page<BookResponseDTO> findAllBooks(Pageable pageable);

    Optional<BookResponseDTO> findBookById(Long bookId);

    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);

    BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO);

    void deleteBookById(Long bookId);

    long getTotalBooksCount();
}
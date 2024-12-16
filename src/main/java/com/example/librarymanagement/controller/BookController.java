package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.BookRequestDTO;
import com.example.librarymanagement.dto.response.BookResponseDTO;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookController {
    private final BookService bookService;
    private final FileStorageService fileStorageService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BookResponseDTO> books = bookService.findBooks(title, authorName, publicationYear, categoryId,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"bookId")));
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<BookResponseDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BookResponseDTO> books = bookService.findAllBooks(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookId")));
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {
        try {
            BookResponseDTO book = bookService.findBookById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tạo sách mới
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> createBook(
            @RequestPart("book") String bookJson,
            @RequestPart("image") MultipartFile imageFile) {
        try {
            String imageUrl = fileStorageService.storeFile(imageFile);
            ObjectMapper objectMapper = new ObjectMapper();
            BookRequestDTO bookRequest = objectMapper.readValue(bookJson, BookRequestDTO.class);

            bookRequest.setImage(imageUrl);

            BookResponseDTO book = bookService.createBook(bookRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(value = "/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long bookId,
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BookRequestDTO bookRequest = objectMapper.readValue(bookJson, BookRequestDTO.class);

            if (imageFile != null && !imageFile.isEmpty()) {
                String newImageUrl = fileStorageService.storeFile(imageFile);
                bookRequest.setImage(newImageUrl);
            }

            BookResponseDTO updatedBook = bookService.updateBook(bookId, bookRequest);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        try {
            bookService.deleteBookById(bookId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalBooksCount() {
        try {
            long count = bookService.getTotalBooksCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error while counting books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.BookCopyRequestDTO;
import com.example.librarymanagement.dto.response.BookCopyResponseDTO;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.BookCopyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-copies")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BookCopyResponseDTO>> searchBookCopies(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long copyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BookCopyResponseDTO> copies = bookCopyService.findBookCopies(condition, status, bookId, copyId,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "copyId")));
            return ResponseEntity.ok(copies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookCopyResponseDTO> createBookCopy(@Valid @RequestBody BookCopyRequestDTO bookCopyRequestDTO) {
        try {
            BookCopyResponseDTO response = bookCopyService.createBookCopy(bookCopyRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{copyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookCopyResponseDTO> getBookCopyById(@PathVariable Long copyId) {
        try {
            BookCopyResponseDTO response = bookCopyService.getBookCopyById(copyId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{copyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookCopyResponseDTO> updateBookCopy(
            @PathVariable Long copyId,
            @Valid @RequestBody BookCopyRequestDTO bookCopyRequestDTO) {
        try {
            BookCopyResponseDTO response = bookCopyService.updateBookCopy(copyId, bookCopyRequestDTO);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{copyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long copyId) {
        try {
            bookCopyService.deleteBookCopyById(copyId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalBookCopiesCount() {
        try {
            long count = bookCopyService.getTotalBookCopiesCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


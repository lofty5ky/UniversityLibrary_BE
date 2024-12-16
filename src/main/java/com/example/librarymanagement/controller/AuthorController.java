package com.example.librarymanagement.controller;


import com.example.librarymanagement.dto.request.AuthorRequestDTO;
import com.example.librarymanagement.dto.response.AuthorResponseDTO;
import com.example.librarymanagement.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    // Tìm tất cả tác giả
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<AuthorResponseDTO> authors = authorService.findAllAuthors(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "authorId")));
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long authorId) {
        try {
            Optional<AuthorResponseDTO> author = authorService.findById(authorId);
            if (author.isPresent()) {
                return ResponseEntity.ok(author.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tìm tác giả theo tên
    @GetMapping("/search")
    public ResponseEntity<List<AuthorResponseDTO>> getAuthorsByName(@RequestParam String name) {
        try {
            List<AuthorResponseDTO> authors = authorService.findByName(name);
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tạo mới tác giả
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO authorRequestDTO) {
        try {
            AuthorResponseDTO author = authorService.createAuthor(authorRequestDTO);
            return ResponseEntity.ok(author);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long authorId, @Valid @RequestBody AuthorRequestDTO authorRequestDTO) {
        try {
            AuthorResponseDTO updatedAuthor = authorService.updateAuthor(authorId, authorRequestDTO);
            return ResponseEntity.ok(updatedAuthor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Xóa tác giả theo ID
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        try {
            authorService.deleteAuthorById(authorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.LibraryCardRequestDTO;
import com.example.librarymanagement.dto.response.LibraryCardResponseDTO;
import com.example.librarymanagement.enums.CardStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.LibraryCardService;
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
@RequestMapping("/api/library-cards")
public class LibraryCardController {

    private final LibraryCardService libraryCardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> searchLibraryCards(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<LibraryCardResponseDTO> cards = libraryCardService.findLibraryCards(
                    userName, email, status, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "cardId")));
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> createLibraryCardRequest(@Valid @RequestBody LibraryCardRequestDTO libraryCardRequestDTO) {
        try {
            LibraryCardResponseDTO response = libraryCardService.createLibraryCardRequest(libraryCardRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cardId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveLibraryCardRequest(@PathVariable Long cardId) {
        try {
            LibraryCardResponseDTO response = libraryCardService.approveLibraryCardRequest(cardId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cardId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectLibraryCardRequest(@PathVariable Long cardId) {
        try {
            libraryCardService.rejectLibraryCardRequest(cardId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cardId}/renew")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> renewLibraryCard(@PathVariable Long cardId) {
        try {
            LibraryCardResponseDTO response = libraryCardService.renewLibraryCard(cardId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cardId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockLibraryCard(@PathVariable Long cardId) {
        try {
            LibraryCardResponseDTO response = libraryCardService.lockLibraryCard(cardId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cardId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unlockLibraryCard(@PathVariable Long cardId) {
        try {
            LibraryCardResponseDTO response = libraryCardService.unlockLibraryCard(cardId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


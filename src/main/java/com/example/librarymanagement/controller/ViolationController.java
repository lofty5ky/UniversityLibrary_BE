package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.ViolationRequestDTO;
import com.example.librarymanagement.dto.response.ViolationResponseDTO;
import com.example.librarymanagement.enums.ViolationType;
import com.example.librarymanagement.service.ViolationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/violations")
public class ViolationController {

    private final ViolationService violationService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ViolationResponseDTO>> getAllViolations(
            @RequestParam(required = false) Boolean resolved,
            @RequestParam(required = false) ViolationType violationType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ViolationResponseDTO> violations =
                    violationService.findAllViolations(resolved, violationType, startDate, endDate,
                            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "violationId")));
            return ResponseEntity.ok(violations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ViolationResponseDTO> createViolation(@Valid @RequestBody ViolationRequestDTO violationRequestDTO) {
        try {
            ViolationResponseDTO violation = violationService.createViolation(violationRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(violation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{violationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ViolationResponseDTO> updateViolation(@PathVariable Long violationId,
                                                                @Valid @RequestBody ViolationRequestDTO violationRequestDTO) {
        try {
            ViolationResponseDTO violation = violationService.updateViolation(violationId, violationRequestDTO);
            return ResponseEntity.ok(violation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{violationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ViolationResponseDTO> getViolationById(@PathVariable Long violationId) {
        try {
            ViolationResponseDTO violation = violationService.findById(violationId)
                    .orElseThrow(() -> new RuntimeException("Violation not found"));
            return ResponseEntity.ok(violation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<ViolationResponseDTO>> getViolationsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ViolationResponseDTO> violations = violationService.findByUserId(userId,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "violationId")));
            return ResponseEntity.ok(violations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{violationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteViolation(@PathVariable Long violationId) {
        try {
            violationService.deleteViolationById(violationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalViolationsCount() {
        try {
            long count = violationService.getTotalViolationCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/type-ratio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getViolationsByType() {
        try {
            List<Map<String, Object>> data = violationService.getViolationsByType();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.ViolationRequestDTO;
import com.example.librarymanagement.dto.response.ViolationResponseDTO;
import com.example.librarymanagement.enums.ViolationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ViolationService {
    Page<ViolationResponseDTO> findAllViolations(Boolean resolved, ViolationType violationType,
                                                 LocalDate startDate, LocalDate endDate, Pageable pageable);

    ViolationResponseDTO createViolation(ViolationRequestDTO violationRequestDTO);

    ViolationResponseDTO updateViolation(Long violationId, ViolationRequestDTO violationRequestDTO);

    Optional<ViolationResponseDTO> findById(Long violationId);

    Page<ViolationResponseDTO> findByUserId(Long userId, Pageable pageable);

    void deleteViolationById(Long violationId);

    long getTotalViolationCount();

    List<Map<String, Object>> getViolationsByType();
}

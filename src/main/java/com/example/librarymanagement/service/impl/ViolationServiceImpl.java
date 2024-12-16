package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.ViolationRequestDTO;
import com.example.librarymanagement.dto.response.ViolationResponseDTO;
import com.example.librarymanagement.enums.ViolationType;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.model.Violation;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.repository.ViolationRepository;
import com.example.librarymanagement.service.ViolationService;
import com.example.librarymanagement.specification.ViolationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViolationServiceImpl implements ViolationService {

    private final ViolationRepository violationRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ViolationResponseDTO> findAllViolations(Boolean resolved, ViolationType violationType,
                                                        LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Violation> specification = ViolationSpecification.findByCriteria(resolved, violationType, startDate, endDate);
        Page<Violation> violations = violationRepository.findAll(specification, pageable);

        return violations.map(this::mapToResponseDTO);
    }

    @Override
    public ViolationResponseDTO createViolation(ViolationRequestDTO violationRequestDTO) {
        User user = userRepository.findById(violationRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Violation violation = Violation.builder()
                .user(user)
                .description(violationRequestDTO.getDescription())
                .violationDate(violationRequestDTO.getViolationDate())
                .resolved(violationRequestDTO.getResolved())
                .violationType(violationRequestDTO.getViolationType())
                .build();

        if (violationRequestDTO.getResolved()) {
            violation.setResolvedAt(LocalDate.now());
        }

        Violation savedViolation = violationRepository.save(violation);

        return mapToResponseDTO(savedViolation);
    }

    @Override
    public ViolationResponseDTO updateViolation(Long violationId, ViolationRequestDTO violationRequestDTO) {
        // Tìm Violation hiện tại
        Violation violation = violationRepository.findById(violationId)
                .orElseThrow(() -> new ResourceNotFoundException("Violation not found"));

        // Cập nhật thông tin vi phạm
        violation.setResolved(violationRequestDTO.getResolved());
        if (violationRequestDTO.getResolved()) {
            violation.setResolvedAt(LocalDate.now());
        } else {
            violation.setResolvedAt(null);
        }
        violation.setDescription(violationRequestDTO.getDescription());
        violation.setViolationDate(violationRequestDTO.getViolationDate());
        violation.setViolationType(violationRequestDTO.getViolationType());

        // Lưu lại vào cơ sở dữ liệu
        violationRepository.save(violation);

        // Trả về ViolationResponseDTO đã cập nhật
        return mapToResponseDTO(violation);
    }

    // Tìm vi phạm theo ID
    @Override
    public Optional<ViolationResponseDTO> findById(Long violationId) {
        return violationRepository.findById(violationId).map(this::mapToResponseDTO);
    }

    // Tìm vi phạm của người dùng
    @Override
    public Page<ViolationResponseDTO> findByUserId(Long userId, Pageable pageable) {
        Page<Violation> violations = violationRepository.findByUserUserId(userId, pageable);

        return violations.map(violation -> ViolationResponseDTO.builder()
                .id(violation.getViolationId())
                .userName(violation.getUser().getName())
                .violationDate(violation.getViolationDate())
                .description(violation.getDescription())
                .resolved(violation.getResolved())
                .resolveAt(violation.getResolvedAt())
                .violationType(violation.getViolationType())
                .build());
    }

    @Override
    public void deleteViolationById(Long violationId) {
        violationRepository.deleteById(violationId);
    }

    @Override
    public long getTotalViolationCount() {
        return violationRepository.countTotalViolations();
    }

    @Override
    public List<Map<String, Object>> getViolationsByType() {
        List<Map<String, Object>> rawData = violationRepository.countViolationsByType();

        return rawData.stream().map(entry -> {
            String type = entry.get("type").toString();
            String formattedName = switch (type) {
                case "LATE_RETURN" -> "Trả muộn";
                case "DAMAGED_BOOK" -> "Hư hỏng";
                case "LOST_BOOK" -> "Mất sách";
                default -> "Khác";
            };
            Map<String, Object> formattedEntry = new HashMap<>();
            formattedEntry.put("name", formattedName);
            formattedEntry.put("value", entry.get("count"));
            return formattedEntry;
        }).collect(Collectors.toList());
    }

    // Helper method to map Violation entity to ViolationResponseDTO
    private ViolationResponseDTO mapToResponseDTO(Violation violation) {
        return ViolationResponseDTO.builder()
                .id(violation.getViolationId())
                .userId(violation.getUser().getUserId())
                .userName(violation.getUser().getName())
                .email(violation.getUser().getEmail())
                .violationDate(violation.getViolationDate())
                .description(violation.getDescription())
                .resolved(violation.getResolved())
                .resolveAt(violation.getResolvedAt())
                .violationType(violation.getViolationType())
                .build();
    }
}
package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.LibraryCardRequestDTO;
import com.example.librarymanagement.dto.response.LibraryCardResponseDTO;
import com.example.librarymanagement.enums.CardStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.LibraryCard;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.LibraryCardRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.LibraryCardService;
import com.example.librarymanagement.specification.LibraryCardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LibraryCardServiceImpl implements LibraryCardService {

    private final LibraryCardRepository libraryCardRepository;
    private final UserRepository userRepository;

    @Override
    public LibraryCardResponseDTO createLibraryCardRequest(LibraryCardRequestDTO libraryCardRequestDTO) {
        User user = userRepository.findById(libraryCardRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        libraryCardRepository.findByUserUserId(user.getUserId())
                .ifPresent(card -> {
                    throw new IllegalStateException("Người dùng đã có thẻ thư viện");
                });

        LibraryCard libraryCard = LibraryCard.builder()
                .user(user)
                .status(CardStatus.PENDING)
                .build();

        LibraryCard savedCard = libraryCardRepository.save(libraryCard);
        return convertToResponseDTO(savedCard);
    }

    @Override
    public LibraryCardResponseDTO approveLibraryCardRequest(Long cardId) {
        LibraryCard libraryCard = libraryCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ thư viện"));

        if (libraryCard.getStatus() != CardStatus.PENDING) {
            throw new IllegalStateException("Thẻ đã được phê duyệt rồi");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusYears(1);

        libraryCard.setIssueDate(issueDate);
        libraryCard.setExpiryDate(expiryDate);
        libraryCard.setStatus(CardStatus.ACTIVE);

        LibraryCard updatedCard = libraryCardRepository.save(libraryCard);
        return convertToResponseDTO(updatedCard);
    }

    @Override
    public void rejectLibraryCardRequest(Long cardId) {
        LibraryCard libraryCard = libraryCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ thư viện"));

        if (libraryCard.getStatus() != CardStatus.PENDING) {
            throw new IllegalStateException("Thẻ đã được phê duyệt");
        }

        libraryCard.setStatus(CardStatus.REJECTED);
        libraryCardRepository.save(libraryCard);
    }

    @Override
    public Page<LibraryCardResponseDTO> findLibraryCards(String userName, String email, CardStatus status, Pageable pageable) {
        Specification<LibraryCard> specification = Specification.where(LibraryCardSpecification.byUserName(userName))
                .and(LibraryCardSpecification.byUserEmail(email))
                .and(LibraryCardSpecification.byStatus(status));

        return libraryCardRepository.findAll(specification, pageable)
                .map(this::convertToResponseDTO);
    }

    public LibraryCardResponseDTO lockLibraryCard(Long cardId) {
        LibraryCard libraryCard = libraryCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ thư viện"));

        if (libraryCard.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Thẻ đã bị khóa");
        }

        libraryCard.setStatus(CardStatus.BLOCKED);

        LibraryCard updatedLibraryCard = libraryCardRepository.save(libraryCard);

        return convertToResponseDTO(updatedLibraryCard);
    }

    public LibraryCardResponseDTO unlockLibraryCard(Long cardId) {
        LibraryCard libraryCard = libraryCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ thư viện"));

        if (libraryCard.getStatus() != CardStatus.BLOCKED) {
            throw new IllegalStateException("Thẻ không bị khóa");
        }

        libraryCard.setStatus(CardStatus.ACTIVE);

        LibraryCard updatedLibraryCard = libraryCardRepository.save(libraryCard);

        return convertToResponseDTO(updatedLibraryCard);
    }

    @Override
    public LibraryCardResponseDTO renewLibraryCard(Long cardId) {
        LibraryCard libraryCard = libraryCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ thư viện"));

        if (libraryCard.getStatus() != CardStatus.EXPIRED) {
            throw new IllegalStateException("Thẻ quá hạn mới được gia hạn");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusYears(1);

        libraryCard.setStatus(CardStatus.ACTIVE);
        libraryCard.setIssueDate(issueDate);
        libraryCard.setExpiryDate(expiryDate);

        LibraryCard updatedCard = libraryCardRepository.save(libraryCard);
        return convertToResponseDTO(updatedCard);
    }

    private LibraryCardResponseDTO convertToResponseDTO(LibraryCard libraryCard) {
        return LibraryCardResponseDTO.builder()
                .cardId(libraryCard.getCardId())
                .userName(libraryCard.getUser().getName())
                .email(libraryCard.getUser().getEmail())
                .phoneNumber(libraryCard.getUser().getPhoneNumber())
                .issueDate(libraryCard.getIssueDate())
                .expiryDate(libraryCard.getExpiryDate())
                .status(libraryCard.getStatus().toString())  // Dùng toString() của enum để lấy giá trị
                .build();
    }
}


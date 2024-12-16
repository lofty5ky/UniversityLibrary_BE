package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.LibraryCardRequestDTO;
import com.example.librarymanagement.dto.response.LibraryCardResponseDTO;
import com.example.librarymanagement.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibraryCardService {

    // Gửi yêu cầu tạo thẻ thư viện
    LibraryCardResponseDTO createLibraryCardRequest(LibraryCardRequestDTO libraryCardRequestDTO);

    // Admin phê duyệt yêu cầu
    LibraryCardResponseDTO approveLibraryCardRequest(Long cardId);

    // Admin từ chối yêu cầu
    void rejectLibraryCardRequest(Long cardId);

    LibraryCardResponseDTO lockLibraryCard(Long cardId);

    LibraryCardResponseDTO unlockLibraryCard(Long cardId);

    // Tìm kiếm thẻ thư viện với phân trang và các điều kiện động
    Page<LibraryCardResponseDTO> findLibraryCards(String userName, String email, CardStatus status, Pageable pageable);

    // Gia hạn thẻ thư viện
    LibraryCardResponseDTO renewLibraryCard(Long cardId);
}
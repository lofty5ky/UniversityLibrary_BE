package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.PublisherRequestDTO;
import com.example.librarymanagement.dto.response.PublisherResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PublisherService {
    // Tạo nhà xuất bản mới
    PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequest);

    // Tìm nhà xuất bản theo ID
    Optional<PublisherResponseDTO> findPublisherById(Long publisherId);

    // Lấy tất cả nhà xuất bản
    Page<PublisherResponseDTO> findAllPublishers(Pageable pageable);

    // Cập nhật nhà xuất bản
    PublisherResponseDTO updatePublisher(Long publisherId, PublisherRequestDTO publisherRequest);

    // Xóa nhà xuất bản theo ID
    void deletePublisherById(Long publisherId);
}

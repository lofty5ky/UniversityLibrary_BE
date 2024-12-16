package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.PublisherRequestDTO;
import com.example.librarymanagement.dto.response.PublisherResponseDTO;
import com.example.librarymanagement.model.Publisher;
import com.example.librarymanagement.repository.PublisherRepository;
import com.example.librarymanagement.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    @Override
    public PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequest) {
        if (publisherRepository.findByName(publisherRequest.getName()).isPresent()) {
            throw new RuntimeException("Đã có nhà xuất bản này");
        }
        // Chuyển từ DTO sang entity
        Publisher publisher = Publisher.builder()
                .name(publisherRequest.getName())
                .address(publisherRequest.getAddress())
                .contactInfo(publisherRequest.getContactInfo())
                .build();

        // Lưu vào cơ sở dữ liệu
        Publisher savedPublisher = publisherRepository.save(publisher);

        // Trả về thông tin nhà xuất bản dưới dạng DTO
        return convertToResponseDTO(savedPublisher);
    }

    @Override
    public Optional<PublisherResponseDTO> findPublisherById(Long publisherId) {
        return publisherRepository.findById(publisherId)
                .map(this::convertToResponseDTO);
    }

    @Override
    public Page<PublisherResponseDTO> findAllPublishers(Pageable pageable) {
        Page<Publisher> publishers = publisherRepository.findAll(pageable);
        return publishers.map(this::convertToResponseDTO);
    }

    @Override
    public PublisherResponseDTO updatePublisher(Long publisherId, PublisherRequestDTO publisherRequest) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        if (!publisher.getName().equals(publisherRequest.getName())) {
            if (publisherRepository.findByName(publisherRequest.getName()).isPresent()) {
                throw new RuntimeException("Tên nhà xuất bản bị trùng");
            }
        }

        publisher.setName(publisherRequest.getName());
        publisher.setAddress(publisherRequest.getAddress());
        publisher.setContactInfo(publisherRequest.getContactInfo());

        Publisher updatedPublisher = publisherRepository.save(publisher);

        return convertToResponseDTO(updatedPublisher);
    }

    @Override
    public void deletePublisherById(Long publisherId) {
        publisherRepository.deleteById(publisherId);
    }

    // Convert Publisher entity to PublisherResponseDTO
    private PublisherResponseDTO convertToResponseDTO(Publisher publisher) {
        return PublisherResponseDTO.builder()
                .publisherId(publisher.getPublisherId())
                .name(publisher.getName())
                .address(publisher.getAddress())
                .contactInfo(publisher.getContactInfo())
                .build();
    }
}


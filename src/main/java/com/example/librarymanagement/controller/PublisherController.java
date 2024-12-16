package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.PublisherRequestDTO;
import com.example.librarymanagement.dto.response.PublisherResponseDTO;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    // Lấy tất cả nhà xuất bản
    @GetMapping()
    public ResponseEntity<Page<PublisherResponseDTO>> getAllPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<PublisherResponseDTO> publishers = publisherService.findAllPublishers(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publisherId")));
            return ResponseEntity.ok(publishers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Lấy nhà xuất bản theo ID
    @GetMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDTO> getPublisherById(@PathVariable Long publisherId) {
        try {
            PublisherResponseDTO publisher = publisherService.findPublisherById(publisherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
            return ResponseEntity.ok(publisher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tạo nhà xuất bản mới
    @PostMapping()
    public ResponseEntity<?> createPublisher(@Valid @RequestBody PublisherRequestDTO publisherRequest) {
        try {
            PublisherResponseDTO publisher = publisherService.createPublisher(publisherRequest);
            return ResponseEntity.ok(publisher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật nhà xuất bản
    @PutMapping("/{publisherId}")
    public ResponseEntity<?> updatePublisher(@PathVariable Long publisherId,
                                             @Valid @RequestBody PublisherRequestDTO publisherRequest) {
        try {
            System.out.println("Creating publisher: " + publisherRequest);
            PublisherResponseDTO publisher = publisherService.updatePublisher(publisherId, publisherRequest);
            return ResponseEntity.ok(publisher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa nhà xuất bản theo ID
    @DeleteMapping("/{publisherId}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {
        try {
            publisherService.deletePublisherById(publisherId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
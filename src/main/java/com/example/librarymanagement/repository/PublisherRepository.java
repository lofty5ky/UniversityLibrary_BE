package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    // Tìm nhà xuất bản theo tên
    Optional<Publisher> findByName(String name);

    // Tìm nhà xuất bản theo địa chỉ (không phân biệt chữ hoa/chữ thường)
    List<Publisher> findByAddressContainingIgnoreCase(String address);
}
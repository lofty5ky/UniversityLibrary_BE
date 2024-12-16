package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Tìm tác giả theo tên (không phân biệt chữ hoa/chữ thường)
    List<Author> findByNameContainingIgnoreCase(String name);

    // Tìm tất cả tác giả theo danh sách ID
    @NonNull
    List<Author> findAllById(@NonNull Iterable<Long> ids);

    Optional<Author> findByNameIgnoreCase(String name);
}

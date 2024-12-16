package com.example.librarymanagement.repository;

import com.example.librarymanagement.enums.CardStatus;
import com.example.librarymanagement.model.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long>, JpaSpecificationExecutor<LibraryCard> {
    List<LibraryCard> findByExpiryDateBeforeAndStatusNot(LocalDate expiryDate, CardStatus status);

    Optional<LibraryCard> findByUserUserId(Long userId);
}
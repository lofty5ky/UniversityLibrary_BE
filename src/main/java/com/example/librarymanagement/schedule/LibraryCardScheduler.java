package com.example.librarymanagement.schedule;

import com.example.librarymanagement.enums.CardStatus;
import com.example.librarymanagement.model.LibraryCard;
import com.example.librarymanagement.repository.LibraryCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LibraryCardScheduler {
    private final LibraryCardRepository libraryCardRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy mỗi ngày lúc 00:00
    public void updateExpiredLibraryCards() {
        LocalDate today = LocalDate.now();

        // Tìm tất cả các LibraryCard có expiryDate nhỏ hơn ngày hiện tại và chưa hết hạn
        List<LibraryCard> expiredCards = libraryCardRepository.findByExpiryDateBeforeAndStatusNot(today, CardStatus.EXPIRED);

        expiredCards.forEach(card -> card.setStatus(CardStatus.EXPIRED));

        libraryCardRepository.saveAll(expiredCards);

        log.info("Updated expired library cards: {}", expiredCards.size());
    }
}
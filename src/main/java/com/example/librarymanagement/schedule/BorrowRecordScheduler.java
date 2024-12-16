package com.example.librarymanagement.schedule;

import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowRecordScheduler {
    private final BorrowRecordRepository borrowRecordRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy mỗi ngày lúc 00:00
    public void updateOverdueBorrowRecords() {
        LocalDate today = LocalDate.now();

        // Tìm tất cả các BorrowRecord có dueDate nhỏ hơn ngày hiện tại, returnDate là null, và status khác RETURNED
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findByDueDateBeforeAndReturnDateIsNullAndStatusNot(today, BorrowStatus.OVERDUE);

        overdueRecords.forEach(record -> record.setStatus(BorrowStatus.OVERDUE));

        borrowRecordRepository.saveAll(overdueRecords);

        System.out.println("Updated overdue borrow records: " + overdueRecords.size());
    }
}

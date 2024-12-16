package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.response.BorrowRecordResponseDTO;
import com.example.librarymanagement.enums.BookStatus;
import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.BookCopy;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.repository.BookCopyRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import com.example.librarymanagement.service.BorrowRecordService;
import com.example.librarymanagement.specification.BorrowRecordSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    @Override
    public Page<BorrowRecordResponseDTO> findBorrowRecords(Long userId, String userName, String email, BorrowStatus status, Long copyId,
                                                           LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<BorrowRecord> specification = Specification.where(BorrowRecordSpecification.byUserId(userId))
                .and(BorrowRecordSpecification.byUserName(userName))
                .and(BorrowRecordSpecification.byEmail(email))
                .and(BorrowRecordSpecification.byStatus(status))
                .and(BorrowRecordSpecification.byCopyId(copyId))
                .and(BorrowRecordSpecification.byBorrowDateBetween(startDate, endDate));

        return borrowRecordRepository.findAll(specification, pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    public void updateBorrowStatusToReturned(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecordRepository.save(borrowRecord);

        BookCopy bookCopy = borrowRecord.getCopy();
        if (bookCopy.getStatus() == BookStatus.BORROWED) {
            bookCopy.setStatus(BookStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        } else {
            throw new IllegalStateException("Book copy is not currently borrowed");
        }
    }

    @Override
    public void deleteBorrowRecordById(Long borrowRecordId) {
        borrowRecordRepository.deleteById(borrowRecordId);
    }

    @Override
    public long countBorrowRecords() {
        return borrowRecordRepository.count();
    }

    @Override
    public long countUnreturnedBooks() {
        return borrowRecordRepository.countUnreturnedBooks();
    }

    @Override
    public List<Map<String, Object>> getBorrowedByMonth() {
        return borrowRecordRepository.countBorrowedByMonth(LocalDate.now().getYear());
    }

    @Override
    public List<Map<String, Object>> getTop5BorrowedBooks() {
        List<Map<String, Object>> books = borrowRecordRepository.findTop5BorrowedBooks();

        for (Map<String, Object> book : books) {
            Long bookId = (Long) book.get("bookId");
            List<String> authors = bookRepository.findAuthorsByBookId(bookId);
            book.put("authors", authors);
        }

        return books;
    }

    private BorrowRecordResponseDTO convertToResponseDTO(BorrowRecord borrowRecord) {
        return BorrowRecordResponseDTO.builder()
                .recordId(borrowRecord.getRecordId())
                .userId(borrowRecord.getUser().getUserId())
                .userName(borrowRecord.getUser().getName())
                .email(borrowRecord.getUser().getEmail())
                .bookId(borrowRecord.getCopy() != null ? borrowRecord.getCopy().getBook().getBookId() : null)
                .bookTitle(borrowRecord.getCopy() != null ? borrowRecord.getCopy().getBook().getTitle() : null)
                .copyId(borrowRecord.getCopy() != null ? borrowRecord.getCopy().getCopyId() : null)
                .bookCondition(borrowRecord.getCopy() != null ? borrowRecord.getCopy().getBookCondition().name() : null)
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .status(borrowRecord.getStatus().name())
                .build();
    }
}

package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.BookCopyRequestDTO;
import com.example.librarymanagement.dto.response.BookCopyResponseDTO;
import com.example.librarymanagement.enums.BookCondition;
import com.example.librarymanagement.enums.BookStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BookCopy;
import com.example.librarymanagement.repository.BookCopyRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.service.BookCopyService;
import com.example.librarymanagement.specification.BookCopySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    @Override
    public Page<BookCopyResponseDTO> findBookCopies(String condition, String status, Long bookId, Long copyId, Pageable pageable) {
        Specification<BookCopy> specification = Specification.where(BookCopySpecification.byCondition(condition))
                .and(BookCopySpecification.byStatus(status))
                .and(BookCopySpecification.byBookId(bookId))
                .and(BookCopySpecification.byCopyId(copyId));

        return bookCopyRepository.findAll(specification, pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    public BookCopyResponseDTO createBookCopy(BookCopyRequestDTO bookCopyRequestDTO) {
        Book book = bookRepository.findById(bookCopyRequestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookCopyRequestDTO.getBookId()));
        book.setTotalQuantity(book.getTotalQuantity() + 1);
        bookRepository.save(book);

        BookCopy bookCopy = BookCopy.builder()
                .book(book)
                .bookCondition(bookCopyRequestDTO.getCondition())
                .status(bookCopyRequestDTO.getStatus())
                .build();

        BookCopy savedCopy = bookCopyRepository.save(bookCopy);
        return convertToResponseDTO(savedCopy);
    }

    public BookCopyResponseDTO getBookCopyById(Long copyId) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy not found with ID: " + copyId));
        return convertToResponseDTO(bookCopy);
    }

    @Override
    public BookCopyResponseDTO updateBookCopy(Long copyId, BookCopyRequestDTO bookCopyRequestDTO) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy not found"));

        bookCopy.setBookCondition(bookCopyRequestDTO.getCondition());
        bookCopy.setStatus(bookCopyRequestDTO.getStatus());

        BookCopy updatedCopy = bookCopyRepository.save(bookCopy);
        return convertToResponseDTO(updatedCopy);
    }

    @Override
    public void deleteBookCopyById(Long copyId) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy not found"));

        Book book = bookRepository.findById(bookCopy.getBook().getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getTotalQuantity() > 0) {
            book.setTotalQuantity(book.getTotalQuantity() - 1);
        }

        bookRepository.save(book);

        bookCopyRepository.deleteById(copyId);
    }

    @Override
    public void createBookCopiesForNewBook(Long bookId, int totalQuantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        for (int i = 0; i < totalQuantity; i++) {
            BookCopy bookCopy = BookCopy.builder()
                    .book(book)
                    .bookCondition(BookCondition.NEW)
                    .status(BookStatus.AVAILABLE)
                    .build();
            bookCopyRepository.save(bookCopy);
        }
    }

    public long getTotalBookCopiesCount(){
        return bookCopyRepository.countTotalBookCopies();
    }

    private BookCopyResponseDTO convertToResponseDTO(BookCopy bookCopy) {
        return BookCopyResponseDTO.builder()
                .copyId(bookCopy.getCopyId())
                .bookId(bookCopy.getBook().getBookId())
                .bookTitle(bookCopy.getBook().getTitle())
                .bookImage(bookCopy.getBook().getImage())
                .condition(bookCopy.getBookCondition())
                .status(bookCopy.getStatus())
                .build();
    }
}

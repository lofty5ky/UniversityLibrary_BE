package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.BookRequestDTO;
import com.example.librarymanagement.dto.response.BookResponseDTO;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.Author;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BookCategory;
import com.example.librarymanagement.model.Publisher;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.BookCopyService;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final AuthorRepository authorRepository;
    private final BookCopyService bookCopyService;
    private final BookCopyRepository bookCopyRepository;

    @Override
    public Page<BookResponseDTO> findBooks(String title, String authorName, Integer publicationYear, Long categoryId, Pageable pageable) {
        Specification<Book> specification = Specification.where(BookSpecification.byTitle(title))
                .and(BookSpecification.byAuthorName(authorName))
                .and(BookSpecification.byPublicationYear(publicationYear))
                .and(BookSpecification.byCategoryId(categoryId));

        return bookRepository.findAll(specification, pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    public Page<BookResponseDTO> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    public Optional<BookResponseDTO> findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(this::convertToResponseDTO);
    }

    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        Publisher publisher = publisherRepository.findById(bookRequestDTO.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
        BookCategory category = bookCategoryRepository.findById(bookRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Author> authors = authorRepository.findAllById(bookRequestDTO.getAuthorIds());
        if (authors.size() != bookRequestDTO.getAuthorIds().size()) {
            throw new ResourceNotFoundException("Some authors not found");
        }

        Book book = Book.builder()
                .title(bookRequestDTO.getTitle())
                .bookSummary(bookRequestDTO.getBookSummary())
                .publisher(publisher)
                .category(category)
                .publicationYear(bookRequestDTO.getPublicationYear())
                .totalQuantity(bookRequestDTO.getTotalQuantity())
                .image(bookRequestDTO.getImage())
                .authors(new HashSet<>(authors))
                .build();

        Book savedBook = bookRepository.save(book);

        bookCopyService.createBookCopiesForNewBook(savedBook.getBookId(), savedBook.getTotalQuantity());

        return convertToResponseDTO(savedBook);
    }

    @Override
    public BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Publisher publisher = publisherRepository.findById(bookRequestDTO.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
        BookCategory category = bookCategoryRepository.findById(bookRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Author> authors = authorRepository.findAllById(bookRequestDTO.getAuthorIds());

        book.setTitle(bookRequestDTO.getTitle());
        book.setBookSummary(bookRequestDTO.getBookSummary());
        book.setPublisher(publisher);
        book.setCategory(category);
        book.setPublicationYear(bookRequestDTO.getPublicationYear());
        book.setTotalQuantity(bookRequestDTO.getTotalQuantity());
        if (bookRequestDTO.getImage() != null) {
            book.setImage(bookRequestDTO.getImage());
        }
        book.setAuthors(new HashSet<>(authors));

        Book updatedBook = bookRepository.save(book);
        return convertToResponseDTO(updatedBook);
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public long getTotalBooksCount(){
        return bookRepository.countTotalBooks();
    }

    private BookResponseDTO convertToResponseDTO(Book book) {
        int availableQuantity = bookCopyRepository.countAvailableCopiesByBookId(book.getBookId());

        return BookResponseDTO.builder()
                .id(book.getBookId())
                .title(book.getTitle())
                .bookSummary(book.getBookSummary())
                .publisherName(book.getPublisher().getName())
                .categoryName(book.getCategory().getName())
                .publicationYear(book.getPublicationYear())
                .totalQuantity(book.getTotalQuantity())
                .image(book.getImage())
                .authors(book.getAuthors().stream()
                        .map(Author::getName)
                        .collect(Collectors.toSet()))
                .authorBiography(book.getAuthors().stream()
                        .map(Author::getBiography)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .availableQuantity(availableQuantity)
                .build();
    }
}

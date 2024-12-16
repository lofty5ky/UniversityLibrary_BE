package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.AuthorRequestDTO;
import com.example.librarymanagement.dto.response.AuthorResponseDTO;
import com.example.librarymanagement.model.Author;
import com.example.librarymanagement.repository.AuthorRepository;
import com.example.librarymanagement.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        Author author = Author.builder()
                .name(authorRequestDTO.getName())
                .biography(authorRequestDTO.getBiography())
                .birthDate(authorRequestDTO.getBirthDate())
                .build();

        Author savedAuthor = authorRepository.save(author);

        return convertToResponseDTO(savedAuthor);
    }

    @Override
    public Optional<AuthorResponseDTO> findById(Long authorId) {
        return authorRepository.findById(authorId)
                .map(this::convertToResponseDTO);
    }

    @Override
    public Page<AuthorResponseDTO> findAllAuthors(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(this::convertToResponseDTO);
    }

    @Override
    public List<AuthorResponseDTO> findByName(String name) {
        return authorRepository.findByNameIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long authorId, AuthorRequestDTO authorRequestDTO) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setName(authorRequestDTO.getName());
        author.setBiography(authorRequestDTO.getBiography());
        author.setBirthDate(authorRequestDTO.getBirthDate());

        Author updatedAuthor = authorRepository.save(author);

        return convertToResponseDTO(updatedAuthor);
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    // Helper method to convert Author entity to AuthorResponseDTO
    private AuthorResponseDTO convertToResponseDTO(Author author) {
        return AuthorResponseDTO.builder()
                .authorId(author.getAuthorId())
                .name(author.getName())
                .biography(author.getBiography())
                .birthDate(author.getBirthDate())
                .build();
    }
}

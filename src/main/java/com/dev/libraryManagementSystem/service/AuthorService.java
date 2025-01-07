package com.dev.libraryManagementSystem.service;

import com.dev.libraryManagementSystem.dto.AuthorDTO;
import com.dev.libraryManagementSystem.entity.Author;
import com.dev.libraryManagementSystem.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        author.setName(authorDTO.getName());
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete author with existing books");
        }

        authorRepository.delete(author);
    }

    public List<AuthorDTO> getAllAuthorsWithBookCount() {
        List<Object[]> results = authorRepository.findAllAuthorsWithBookCount();
        List<AuthorDTO> authorDTOs = new ArrayList<>();

        for (Object[] result : results) {
            Author author = (Author) result[0];
            Long bookCount = (Long) result[1];

            AuthorDTO dto = new AuthorDTO();
            dto.setId(author.getId());
            dto.setName(author.getName());
            dto.setBookCount(bookCount);

            authorDTOs.add(dto);
        }

        return authorDTOs;
    }
}


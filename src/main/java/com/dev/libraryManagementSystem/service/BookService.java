package com.dev.libraryManagementSystem.service;

import com.dev.libraryManagementSystem.entity.Book;
import com.dev.libraryManagementSystem.entity.Author;
import com.dev.libraryManagementSystem.repository.BookRepository;
import com.dev.libraryManagementSystem.repository.AuthorRepository;
import com.dev.libraryManagementSystem.dto.BookDTO;
import com.dev.libraryManagementSystem.specification.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Book createBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setGenre(bookDTO.getGenre());
        book.setAvailable(true);

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        // Check ISBN uniqueness only if it's changed
        if (!book.getIsbn().equals(bookDTO.getIsbn()) && bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setGenre(bookDTO.getGenre());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Cannot delete book that is currently borrowed");
        }

        bookRepository.delete(book);
    }

    public Page<Book> findBooks(Long authorId, String genre, Boolean available, Pageable pageable) {
        Specification<Book> spec = Specification.where(null);

        if (authorId != null) {
            spec = spec.and(BookSpecification.hasAuthorId(authorId));
        }
        if (genre != null) {
            spec = spec.and(BookSpecification.hasGenre(genre));
        }
        if (available != null) {
            spec = spec.and(BookSpecification.isAvailable(available));
        }

        return bookRepository.findAll(spec, pageable);
    }
}
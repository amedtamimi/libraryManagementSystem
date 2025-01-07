package com.dev.libraryManagementSystem.repository;

import com.dev.libraryManagementSystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
    Page<Book> findByGenre(String genre, Pageable pageable);
    Page<Book> findByAvailable(boolean available, Pageable pageable);
    boolean existsByIsbn(String isbn);
}

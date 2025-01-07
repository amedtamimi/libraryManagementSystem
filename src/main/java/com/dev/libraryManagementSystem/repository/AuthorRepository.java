package com.dev.libraryManagementSystem.repository;

import com.dev.libraryManagementSystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a, COUNT(b) as bookCount FROM Author a LEFT JOIN a.books b GROUP BY a")
    List<Object[]> findAllAuthorsWithBookCount();

    @Query("SELECT COUNT(b) FROM Author a LEFT JOIN a.books b WHERE a.id = :authorId")
    Long getBookCountByAuthorId(@Param("authorId") Long authorId);
}

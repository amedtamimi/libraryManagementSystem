package com.dev.libraryManagementSystem.specification;

import com.dev.libraryManagementSystem.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> hasAuthorId(Long authorId) {
        return (root, query, cb) -> {
            if (authorId == null) return null;
            return cb.equal(root.get("author").get("id"), authorId);
        };
    }

    public static Specification<Book> hasGenre(String genre) {
        return (root, query, cb) -> {
            if (genre == null) return null;
            return cb.equal(root.get("genre"), genre);
        };
    }

    public static Specification<Book> isAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) return null;
            return cb.equal(root.get("available"), available);
        };
    }
}
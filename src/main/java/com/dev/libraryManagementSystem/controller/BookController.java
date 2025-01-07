package com.dev.libraryManagementSystem.controller;

import com.dev.libraryManagementSystem.dto.BookDTO;
import com.dev.libraryManagementSystem.entity.Book;
import com.dev.libraryManagementSystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            summary = "Create new book",
            description = "Create a new book in the library (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = Book.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @Operation(
            summary = "Update book",
            description = "Update an existing book's details (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }


    @Operation(
            summary = "Delete book",
            description = "Delete a book from the library (Admin only)"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search books",
            description = "Search books with optional filters and pagination"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    public ResponseEntity<Page<Book>> getBooks(
            @Parameter(description = "Filter by author ID")
            @RequestParam(required = false) Long authorId,

            @Parameter(description = "Filter by genre")
            @RequestParam(required = false) String genre,

            @Parameter(description = "Filter by availability")
            @RequestParam(required = false) Boolean available,

            @Parameter(description = "Pagination parameters")
            Pageable pageable){
        return ResponseEntity.ok(bookService.findBooks(authorId, genre, available, pageable));
    }
}

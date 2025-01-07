package com.dev.libraryManagementSystem.controller;

import com.dev.libraryManagementSystem.dto.AuthorDTO;
import com.dev.libraryManagementSystem.entity.Author;
import com.dev.libraryManagementSystem.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Author management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            summary = "Create author",
            description = "Create a new author (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = Author.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(authorDTO));
    }

    @Operation(
            summary = "Update author",
            description = "Update an existing author's details (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = Author.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Author> updateAuthor( @Parameter(description = "Author ID") @PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }

    @Operation(
            summary = "Delete author",
            description = "Delete an author (Admin only, not allowed if author has books)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Author deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete author with existing books"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@Parameter(description = "Author ID") @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all authors",
            description = "Get a list of all authors with their book counts"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuthorDTO.class))
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    public ResponseEntity<List<AuthorDTO>> getAllAuthorsWithBookCount() {
        return ResponseEntity.ok(authorService.getAllAuthorsWithBookCount());
    }
}
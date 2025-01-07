package com.dev.libraryManagementSystem.controller;

import com.dev.libraryManagementSystem.dto.BorrowerUpdateDTO;
import com.dev.libraryManagementSystem.entity.BorrowRecord;
import com.dev.libraryManagementSystem.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dev.libraryManagementSystem.service.BorrowerService;
import com.dev.libraryManagementSystem.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Borrowing", description = "Book borrowing management APIs")
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final BorrowingService borrowingService;

    public BorrowerController(BorrowerService borrowerService, BorrowingService borrowingService) {
        this.borrowerService = borrowerService;
        this.borrowingService = borrowingService;
    }

    @Operation(
            summary = "Update borrower",
            description = "Update borrower details (Admin or Self)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Borrower updated successfully",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrower not found"
            )
    })    @PutMapping("/borrowers/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    public ResponseEntity<User> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody BorrowerUpdateDTO updateDTO) {
        User updatedBorrower = borrowerService.updateBorrower(id, updateDTO);
        return ResponseEntity.ok(updatedBorrower);
    }

    @Operation(
            summary = "Delete borrower",
            description = "Delete a borrower account (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Borrower deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete borrower with active loans"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrower not found"
            )
    })
    @DeleteMapping("/borrowers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Borrow book",
            description = "Borrow a book for a specific borrower"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Book borrowed successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Book not available or borrower limit exceeded"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book or borrower not found"
            )
    })
    @PostMapping("/borrow")
    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    public ResponseEntity<BorrowRecord> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId) {
        BorrowRecord borrowRecord = borrowingService.borrowBook(bookId, borrowerId);
        return new ResponseEntity<>(borrowRecord, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Return book",
            description = "Return a borrowed book"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book returned successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrow record not found"
            )
    })
    @PostMapping("/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    public ResponseEntity<BorrowRecord> returnBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId) {
        BorrowRecord borrowRecord = borrowingService.returnBook(bookId, borrowerId);
        return ResponseEntity.ok(borrowRecord);
    }

    @Operation(
            summary = "Get active borrows",
            description = "Get list of all currently borrowed books (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @GetMapping("/borrowed-books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowRecord>> getAllActiveBorrows() {
        List<BorrowRecord> activeLoans = borrowingService.getAllActiveBorrows();
        return ResponseEntity.ok(activeLoans);
    }

    @Operation(
            summary = "Get overdue books",
            description = "Get list of all overdue books (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @GetMapping("/overdue-books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowRecord>> getOverdueBooks() {
        List<BorrowRecord> overdueBooks = borrowingService.getOverdueBorrows();
        return ResponseEntity.ok(overdueBooks);
    }
}
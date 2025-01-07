package com.dev.libraryManagementSystem.service;

import com.dev.libraryManagementSystem.entity.Book;
import com.dev.libraryManagementSystem.entity.Borrower;
import com.dev.libraryManagementSystem.entity.BorrowRecord;
import com.dev.libraryManagementSystem.repository.BookRepository;
import com.dev.libraryManagementSystem.repository.BorrowerRepository;
import com.dev.libraryManagementSystem.repository.BorrowRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowingService {
    private static final int MAX_BOOKS_PER_BORROWER = 5;
    private static final int LOAN_PERIOD_DAYS = 14;

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowerService borrowerService;

    public BorrowingService(BookRepository bookRepository,
                            BorrowerRepository borrowerRepository,
                            BorrowRecordRepository borrowRecordRepository,
                            BorrowerService borrowerService) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.borrowerService = borrowerService;
    }

    public BorrowRecord borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found"));

        int activeLoans = borrowerService.getActiveBorrowCount(borrowerId);
        if (activeLoans >= MAX_BOOKS_PER_BORROWER) {
            throw new IllegalStateException("Borrower has reached the maximum number of borrowed books");
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setBorrower(borrower);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setDueDate(LocalDateTime.now().plusDays(LOAN_PERIOD_DAYS));

        book.setAvailable(false);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    public BorrowRecord returnBook(Long bookId, Long borrowerId) {
        BorrowRecord borrowRecord = borrowRecordRepository
                .findByBookIdAndBorrowerIdAndReturnDateIsNull(bookId, borrowerId)
                .orElseThrow(() -> new EntityNotFoundException("No active borrow record found"));

        borrowRecord.setReturnDate(LocalDateTime.now());

        Book book = borrowRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    public List<BorrowRecord> getAllActiveBorrows() {
        return borrowRecordRepository.findByReturnDateIsNull();
    }

    public List<BorrowRecord> getOverdueBorrows() {
        LocalDateTime now = LocalDateTime.now();
        return borrowRecordRepository.findByReturnDateIsNullAndDueDateBefore(now);
    }
}
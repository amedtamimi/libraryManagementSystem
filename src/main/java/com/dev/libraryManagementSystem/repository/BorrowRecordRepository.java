package com.dev.libraryManagementSystem.repository;

import com.dev.libraryManagementSystem.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    int countByBorrowerIdAndReturnDateIsNull(Long borrowerId);

    Optional<BorrowRecord> findByBookIdAndBorrowerIdAndReturnDateIsNull(Long bookId, Long borrowerId);

    List<BorrowRecord> findByReturnDateIsNull();

    List<BorrowRecord> findByReturnDateIsNullAndDueDateBefore(LocalDateTime date);
}

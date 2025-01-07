package com.dev.libraryManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecordDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long borrowerId;
    private String borrowerName;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private boolean isOverdue;
}
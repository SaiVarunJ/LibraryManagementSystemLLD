package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a borrowing record for a patron.
 * Tracks when a book was borrowed and returned.
 */
public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String recordId;
    private final Book book;
    private final Patron patron;
    private final LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private BorrowStatus status;

    /**
     * Constructor for creating a new BorrowRecord.
     */
    public BorrowRecord(String recordId, Book book, Patron patron, LocalDateTime dueDate) {
        this.recordId = Objects.requireNonNull(recordId, "Record ID cannot be null");
        this.book = Objects.requireNonNull(book, "Book cannot be null");
        this.patron = Objects.requireNonNull(patron, "Patron cannot be null");
        this.borrowDate = LocalDateTime.now();
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.status = BorrowStatus.ACTIVE;
    }

    // Getters
    public String getRecordId() {
        return recordId;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    /**
     * Check if the book is overdue.
     */
    public boolean isOverdue() {
        if (status == BorrowStatus.ACTIVE) {
            return LocalDateTime.now().isAfter(dueDate);
        }
        return false;
    }

    /**
     * Mark the book as returned.
     */
    public void returnBook() {
        this.returnDate = LocalDateTime.now();
        this.status = BorrowStatus.COMPLETED;
    }

    /**
     * Renew the book (extend due date by 14 days).
     */
    public void renewBook() {
        if (status == BorrowStatus.ACTIVE && !isOverdue()) {
            this.dueDate = this.dueDate.plusDays(14);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowRecord)) return false;
        BorrowRecord that = (BorrowRecord) o;
        return recordId.equals(that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "recordId='" + recordId + '\'' +
                ", book=" + book.getTitle() +
                ", patron=" + patron.getName() +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}


package com.library.service;

import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.model.Patron;

/**
 * Interface for lending-related operations.
 * Demonstrates the Single Responsibility Principle (SRP).
 */
public interface ILendingService {
    /**
     * Checkout a book for a patron.
     */
    BorrowRecord checkoutBook(Book book, Patron patron);

    /**
     * Return a book borrowed by a patron.
     */
    void returnBook(String recordId);

    /**
     * Renew a book for a patron.
     */
    void renewBook(String recordId);

    /**
     * Get a borrow record by ID.
     */
    BorrowRecord getBorrowRecord(String recordId);

    /**
     * Get all active borrow records.
     */
    java.util.List<BorrowRecord> getActiveBorrowRecords();

    /**
     * Get overdue borrow records.
     */
    java.util.List<BorrowRecord> getOverdueBorrowRecords();

    /**
     * Get full borrow history (all records) for a given patron.
     */
    java.util.List<BorrowRecord> getBorrowHistoryForPatron(String patronId);

    /**
     * Get the list of currently borrowed books for a patron.
     */
    java.util.List<Book> getBorrowedBooksForPatron(String patronId);
}

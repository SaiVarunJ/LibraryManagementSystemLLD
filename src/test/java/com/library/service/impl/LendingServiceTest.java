package com.library.service.impl;

import com.library.model.*;
import com.library.observer.LibraryEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LendingService.
 */
public class LendingServiceTest {
    private LendingService lendingService;
    private BookService bookService;
    private PatronService patronService;
    private LibraryEventPublisher eventPublisher;
    private Book testBook;
    private Patron testPatron;

    @BeforeEach
    public void setUp() {
        eventPublisher = new LibraryEventPublisher();
        lendingService = new LendingService(eventPublisher);
        bookService = new BookService();
        patronService = new PatronService();

        testBook = new Book("ISBN001", "Test Book", "Test Author", 2024, "Test Publisher", 5);
        testPatron = new Patron("P001", "Test Patron", "test@example.com", "555-0000");

        bookService.addBook(testBook);
        patronService.addPatron(testPatron);
    }

    @Test
    public void testCheckoutBook() {
        BorrowRecord record = lendingService.checkoutBook(testBook, testPatron);
        assertNotNull(record);
        assertEquals(BorrowStatus.ACTIVE, record.getStatus());
        assertTrue(testPatron.getCurrentlyBorrowedBooks().contains(testBook));
    }

    @Test
    public void testReturnBook() {
        BorrowRecord record = lendingService.checkoutBook(testBook, testPatron);
        lendingService.returnBook(record.getRecordId());

        BorrowRecord returned = lendingService.getBorrowRecord(record.getRecordId());
        assertEquals(BorrowStatus.COMPLETED, returned.getStatus());
    }

    @Test
    public void testRenewBook() {
        BorrowRecord record = lendingService.checkoutBook(testBook, testPatron);
        LocalDateTime originalDueDate = record.getDueDate();

        lendingService.renewBook(record.getRecordId());
        assertTrue(record.getDueDate().isAfter(originalDueDate));
    }

    @Test
    public void testCheckoutBookNotAvailable() {
        // Make book unavailable
        for (int i = 0; i < 5; i++) {
            testBook.decrementAvailableCopies();
        }

        assertThrows(IllegalStateException.class, () ->
            lendingService.checkoutBook(testBook, testPatron)
        );
    }
}


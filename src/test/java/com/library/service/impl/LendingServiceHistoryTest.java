package com.library.service.impl;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Patron;
import com.library.observer.LibraryEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LendingServiceHistoryTest {
    private LendingService lendingService;
    private Book testBook;
    private Patron testPatron;

    @BeforeEach
    public void setUp() {
        lendingService = new LendingService(new LibraryEventPublisher());
        testBook = new Book("LEND-001", "Test Lending", "Author", 2020, "Pub", 2);
        testPatron = new Patron("PT-100", "Test Patron", "test@ex.com", "555-0000");
    }

    @Test
    public void testCheckoutAddsToHistoryAndLendingRecords() {
        BorrowRecord record = lendingService.checkoutBook(testBook, testPatron);
        assertNotNull(record);
        assertEquals("LEND-001", record.getBook().getIsbn());

        // Patron's history should include the record
        List<BorrowRecord> patronHistory = testPatron.getBorrowHistory();
        assertEquals(1, patronHistory.size());
        assertEquals(record.getRecordId(), patronHistory.get(0).getRecordId());

        // Lending service history should include the record
        List<BorrowRecord> serviceHistory = lendingService.getBorrowHistoryForPatron(testPatron.getPatronId());
        assertEquals(1, serviceHistory.size());
        assertEquals(record.getRecordId(), serviceHistory.get(0).getRecordId());

        // Return the book and verify status update
        lendingService.returnBook(record.getRecordId());
        BorrowRecord after = lendingService.getBorrowRecord(record.getRecordId());
        assertNotNull(after.getReturnDate());
        assertEquals(com.library.model.BorrowStatus.COMPLETED, after.getStatus());
    }
}


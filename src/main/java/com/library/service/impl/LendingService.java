package com.library.service.impl;

import com.library.model.*;
import com.library.service.ILendingService;
import com.library.service.IRecommendationService;
import com.library.observer.LibraryEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of ILendingService.
 * Manages book checkout, return, and renewal operations.
 */
public class LendingService implements ILendingService {
    private static final Logger logger = LoggerFactory.getLogger(LendingService.class);
    private final Map<String, BorrowRecord> borrowRecords; // Key: Record ID
    private final LibraryEventPublisher eventPublisher;
    private final IRecommendationService recommendationService; // optional

    public LendingService(LibraryEventPublisher eventPublisher) {
        this(eventPublisher, null);
    }

    /**
     * New constructor that accepts an optional recommendation service so lending can update popularity metrics.
     */
    public LendingService(LibraryEventPublisher eventPublisher, IRecommendationService recommendationService) {
        this.borrowRecords = new HashMap<>();
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "Event publisher cannot be null");
        this.recommendationService = recommendationService; // may be null for legacy callers/tests
    }

    @Override
    public BorrowRecord checkoutBook(Book book, Patron patron) {
        Objects.requireNonNull(book, "Book cannot be null");
        Objects.requireNonNull(patron, "Patron cannot be null");

        if (!book.isAvailable()) {
            logger.warn("Book not available for checkout: {}", book.getTitle());
            throw new IllegalStateException("Book is not available for checkout");
        }

        if (!patron.canBorrowMoreBooks()) {
            logger.warn("Patron has reached maximum borrow limit: {}", patron.getPatronId());
            throw new IllegalStateException("Patron has reached maximum borrow limit");
        }

        // Create borrow record
        String recordId = "BR-" + System.currentTimeMillis();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(14); // 14-day loan period
        BorrowRecord record = new BorrowRecord(recordId, book, patron, dueDate);

        // Update book and patron
        book.decrementAvailableCopies();
        patron.borrowBook(book);
        patron.addBorrowRecord(record);

        // Store record
        borrowRecords.put(recordId, record);

        // Notify recommendation service (if present) to update popularity metrics
        if (recommendationService != null) {
            try {
                recommendationService.recordBorrow(book.getIsbn());
            } catch (Exception e) {
                logger.warn("Failed to record borrow in recommendation service for ISBN {}: {}", book.getIsbn(), e.getMessage());
            }
        }

        logger.info("Book checked out: {} to patron {}", book.getTitle(), patron.getPatronId());
        return record;
    }

    @Override
    public void returnBook(String recordId) {
        Objects.requireNonNull(recordId, "Record ID cannot be null");
        BorrowRecord record = borrowRecords.get(recordId);

        if (record == null) {
            logger.warn("Borrow record not found: {}", recordId);
            throw new IllegalArgumentException("Borrow record not found");
        }

        if (record.getStatus() != BorrowStatus.ACTIVE) {
            logger.warn("Borrow record is not active: {}", recordId);
            throw new IllegalStateException("Borrow record is not active");
        }

        // Update book and patron
        Book book = record.getBook();
        Patron patron = record.getPatron();

        book.incrementAvailableCopies();
        patron.returnBook(book);
        record.returnBook();

        logger.info("Book returned: {} by patron {}", book.getTitle(), patron.getPatronId());
        eventPublisher.publishBookReturnedEvent(recordId);
    }

    @Override
    public void renewBook(String recordId) {
        Objects.requireNonNull(recordId, "Record ID cannot be null");
        BorrowRecord record = borrowRecords.get(recordId);

        if (record == null) {
            logger.warn("Borrow record not found: {}", recordId);
            throw new IllegalArgumentException("Borrow record not found");
        }

        record.renewBook();
        logger.info("Book renewed: {} (New due date: {})", record.getBook().getTitle(), record.getDueDate());
    }

    @Override
    public BorrowRecord getBorrowRecord(String recordId) {
        Objects.requireNonNull(recordId, "Record ID cannot be null");
        return borrowRecords.get(recordId);
    }

    @Override
    public List<BorrowRecord> getActiveBorrowRecords() {
        return borrowRecords.values().stream()
                .filter(r -> r.getStatus() == BorrowStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecord> getOverdueBorrowRecords() {
        List<BorrowRecord> overdueRecords = borrowRecords.values().stream()
                .filter(BorrowRecord::isOverdue)
                .collect(Collectors.toList());

        // Publish overdue notifications
        for (BorrowRecord record : overdueRecords) {
            eventPublisher.publishBookOverdueEvent(record.getRecordId());
        }

        return overdueRecords;
    }

    @Override
    public List<BorrowRecord> getBorrowHistoryForPatron(String patronId) {
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        return borrowRecords.values().stream()
                .filter(r -> r.getPatron() != null && patronId.equals(r.getPatron().getPatronId()))
                .sorted(Comparator.comparing(BorrowRecord::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBorrowedBooksForPatron(String patronId) {
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        return borrowRecords.values().stream()
                .filter(r -> r.getPatron() != null && patronId.equals(r.getPatron().getPatronId()))
                .filter(r -> r.getStatus() == BorrowStatus.ACTIVE)
                .map(BorrowRecord::getBook)
                .collect(Collectors.toList());
    }
}

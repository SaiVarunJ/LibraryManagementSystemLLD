package com.library;

import com.library.model.*;
import com.library.service.*;
import com.library.service.impl.*;
import com.library.observer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Main Library class that coordinates all library operations.
 * Acts as a Facade for the library system, demonstrating the Facade Design Pattern.
 * Also demonstrates Dependency Inversion Principle (DIP) by depending on abstractions.
 */
public class Library {
    private static final Logger logger = LoggerFactory.getLogger(Library.class);

    private final IBookService bookService;
    private final IPatronService patronService;
    private final ILendingService lendingService;
    private final IReservationService reservationService;
    private final IRecommendationService recommendationService;
    private final IBranchService branchService;
    private final LibraryEventPublisher eventPublisher;

    public Library() {
        // Initialize event publisher
        this.eventPublisher = new LibraryEventPublisher();

        // Initialize core services that don't depend on recommendations
        this.bookService = new BookService();
        this.patronService = new PatronService();

        // Initialize recommendation service early so other services (like LendingService) can use it
        // Pass the BookService so RecommendationService sees books added later at runtime
        this.recommendationService = new RecommendationService(this.bookService);

        // Initialize services that may depend on recommendation service
        this.lendingService = new LendingService(eventPublisher, recommendationService);
        this.reservationService = new ReservationService(eventPublisher);
        this.branchService = new BranchService();

        // Subscribe to library events
        eventPublisher.subscribe(new EmailNotifier());
        eventPublisher.subscribe(new SMSNotifier());

        logger.info("Library initialized successfully");
    }

    // ==================== Book Management ====================

    /**
     * Add a new book to the library.
     */
    public void addBook(Book book) {
        bookService.addBook(book);
    }

    /**
     * Remove a book from the library.
     */
    public void removeBook(String isbn) {
        bookService.removeBook(isbn);
    }

    /**
     * Update book information.
     */
    public void updateBook(Book book) {
        bookService.updateBook(book);
    }

    /**
     * Search for books by title.
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookService.searchByTitle(title);
    }

    /**
     * Search for books by author.
     */
    public List<Book> searchBooksByAuthor(String author) {
        return bookService.searchByAuthor(author);
    }

    /**
     * Search for books by ISBN.
     */
    public List<Book> searchBooksByIsbn(String isbn) {
        return bookService.searchByIsbn(isbn);
    }

    /**
     * Get all books in the library.
     */
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Get all available books.
     */
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    // ==================== Patron Management ====================

    /**
     * Add a new patron to the library.
     */
    public void addPatron(Patron patron) {
        patronService.addPatron(patron);
    }

    /**
     * Remove a patron from the library.
     */
    public void removePatron(String patronId) {
        patronService.removePatron(patronId);
    }

    /**
     * Update patron information.
     */
    public void updatePatron(Patron patron) {
        patronService.updatePatron(patron);
    }

    /**
     * Get a patron by ID.
     */
    public Patron getPatron(String patronId) {
        return patronService.getPatron(patronId);
    }

    /**
     * Search for patrons by name.
     */
    public List<Patron> searchPatronsByName(String name) {
        return patronService.searchByName(name);
    }

    /**
     * Get all patrons in the library.
     */
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    // ==================== Lending Operations ====================

    /**
     * Checkout a book for a patron.
     */
    public BorrowRecord checkoutBook(String isbn, String patronId) {
        Book book = bookService.getBook(isbn);
        Patron patron = patronService.getPatron(patronId);

        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }

        return lendingService.checkoutBook(book, patron);
    }

    /**
     * Return a borrowed book.
     */
    public void returnBook(String recordId) {
        lendingService.returnBook(recordId);
    }

    /**
     * Renew a borrowed book.
     */
    public void renewBook(String recordId) {
        lendingService.renewBook(recordId);
    }

    /**
     * Get all active borrow records.
     */
    public List<BorrowRecord> getActiveBorrowRecords() {
        return lendingService.getActiveBorrowRecords();
    }

    /**
     * Get all overdue borrow records.
     */
    public List<BorrowRecord> getOverdueBorrowRecords() {
        return lendingService.getOverdueBorrowRecords();
    }

    // ==================== Reservation Operations ====================

    /**
     * Create a reservation for a book.
     */
    public BookReservation reserveBook(String isbn, String patronId) {
        Book book = bookService.getBook(isbn);
        Patron patron = patronService.getPatron(patronId);

        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }

        return reservationService.createReservation(book, patron);
    }

    /**
     * Cancel a reservation.
     */
    public void cancelReservation(String reservationId) {
        reservationService.cancelReservation(reservationId);
    }

    /**
     * Get active reservations for a book.
     */
    public List<BookReservation> getReservationsForBook(String isbn) {
        return reservationService.getActiveReservationsForBook(isbn);
    }

    /**
     * Get active reservations for a patron.
     */
    public List<BookReservation> getReservationsForPatron(String patronId) {
        return reservationService.getActiveReservationsForPatron(patronId);
    }

    /**
     * Process expired reservations.
     */
    public void processExpiredReservations() {
        reservationService.processExpiredReservations();
    }

    // ==================== Recommendation Operations ====================

    /**
     * Get book recommendations for a patron.
     */
    public List<Book> getRecommendationsForPatron(String patronId, int count) {
        Patron patron = patronService.getPatron(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        return recommendationService.getRecommendations(patron, count);
    }

    /**
     * Get similar books to a given book.
     */
    public List<Book> getSimilarBooks(String isbn, int count) {
        Book book = bookService.getBook(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        return recommendationService.getSimilarBooks(book, count);
    }

    /**
     * Get popular books.
     */
    public List<Book> getPopularBooks(int count) {
        return recommendationService.getPopularBooks(count);
    }

    // ==================== Branch Operations ====================

    /**
     * Add a new branch.
     */
    public void addBranch(Branch branch) {
        branchService.addBranch(branch);
    }

    /**
     * Remove a branch.
     */
    public void removeBranch(String branchId) {
        branchService.removeBranch(branchId);
    }

    /**
     * Get a branch by ID.
     */
    public Branch getBranch(String branchId) {
        return branchService.getBranch(branchId);
    }

    /**
     * Get all branches.
     */
    public List<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }

    /**
     * Transfer books between branches.
     */
    public void transferBooks(String sourceBranchId, String targetBranchId, String isbn, int quantity) {
        branchService.transferBooks(sourceBranchId, targetBranchId, isbn, quantity);
    }

    /**
     * Get book availability across all branches.
     */
    public Map<String, Integer> getBookAvailabilityAcrossBranches(String isbn) {
        return branchService.getBookAvailabilityAcrossBranches(isbn);
    }

    // ==================== Event Management ====================

    /**
     * Subscribe an observer to library events.
     */
    public void subscribeToEvents(LibraryEventObserver observer) {
        eventPublisher.subscribe(observer);
    }

    /**
     * Unsubscribe an observer from library events.
     */
    public void unsubscribeFromEvents(LibraryEventObserver observer) {
        eventPublisher.unsubscribe(observer);
    }

    /**
     * Get full borrow history for a patron (through the lending service).
     */
    public List<BorrowRecord> getBorrowHistoryForPatron(String patronId) {
        return lendingService.getBorrowHistoryForPatron(patronId);
    }

    /**
     * Get list of currently borrowed books for a patron.
     */
    public List<Book> getBorrowedBooksForPatron(String patronId) {
        return lendingService.getBorrowedBooksForPatron(patronId);
    }

    // ==================== Utility Methods ====================

    /**
     * Get library statistics.
     */
    public void printLibraryStatistics() {
        System.out.println("\n========== LIBRARY STATISTICS ==========");
        System.out.println("Total Books: " + bookService.getAllBooks().size());
        System.out.println("Available Books: " + bookService.getAvailableBooks().size());
        System.out.println("Total Patrons: " + patronService.getAllPatrons().size());
        System.out.println("Active Borrows: " + lendingService.getActiveBorrowRecords().size());
        System.out.println("Overdue Books: " + lendingService.getOverdueBorrowRecords().size());
        System.out.println("Total Branches: " + branchService.getAllBranches().size());
        System.out.println("========================================\n");
    }

    /**
     * Allow runtime switching of the recommendation strategy via the Library facade.
     * This delegates to the underlying RecommendationService if available.
     */
    public void setRecommendationStrategy(com.library.service.impl.RecommendationStrategy strategy) {
        if (this.recommendationService instanceof com.library.service.impl.RecommendationService) {
            ((com.library.service.impl.RecommendationService) this.recommendationService).setStrategy(strategy);
        } else {
            throw new UnsupportedOperationException("Underlying recommendation service does not support strategy switching");
        }
    }

}

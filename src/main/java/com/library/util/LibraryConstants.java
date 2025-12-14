package com.library.util;

/**
 * Constants used throughout the library management system.
 */
public class LibraryConstants {

    // Borrowing constraints
    public static final int MAX_BORROW_DURATION_DAYS = 14;
    public static final int RENEWAL_DURATION_DAYS = 14;
    public static final int MAX_BOOKS_PER_PATRON = 5;

    // Reservation constraints
    public static final int RESERVATION_VALIDITY_DAYS = 7;

    // System messages
    public static final String BOOK_NOT_FOUND = "Book not found in the system";
    public static final String PATRON_NOT_FOUND = "Patron not found in the system";
    public static final String BOOK_NOT_AVAILABLE = "Book is not available for checkout";
    public static final String MAX_BORROW_LIMIT_REACHED = "Patron has reached maximum borrow limit";
    public static final String BORROW_RECORD_NOT_FOUND = "Borrow record not found";
    public static final String RESERVATION_NOT_FOUND = "Reservation not found";

    // Default values
    public static final int DEFAULT_RECOMMENDATION_COUNT = 5;

    private LibraryConstants() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Cannot instantiate constants class");
    }
}


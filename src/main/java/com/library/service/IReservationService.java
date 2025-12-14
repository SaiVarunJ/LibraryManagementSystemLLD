package com.library.service;

import com.library.model.BookReservation;
import com.library.model.Book;
import com.library.model.Patron;
import java.util.List;

/**
 * Interface for reservation-related operations.
 * Demonstrates the Single Responsibility Principle (SRP).
 */
public interface IReservationService {
    /**
     * Create a reservation for a book.
     */
    BookReservation createReservation(Book book, Patron patron);

    /**
     * Cancel a reservation.
     */
    void cancelReservation(String reservationId);

    /**
     * Get a reservation by ID.
     */
    BookReservation getReservation(String reservationId);

    /**
     * Get all active reservations for a book.
     */
    List<BookReservation> getActiveReservationsForBook(String isbn);

    /**
     * Get all active reservations for a patron.
     */
    List<BookReservation> getActiveReservationsForPatron(String patronId);

    /**
     * Process expired reservations.
     */
    void processExpiredReservations();
}


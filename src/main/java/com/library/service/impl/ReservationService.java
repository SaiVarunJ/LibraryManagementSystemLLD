package com.library.service.impl;

import com.library.model.*;
import com.library.service.IReservationService;
import com.library.observer.LibraryEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IReservationService.
 * Manages book reservations and notifications.
 */
public class ReservationService implements IReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final Map<String, BookReservation> reservations; // Key: Reservation ID
    private final LibraryEventPublisher eventPublisher;

    public ReservationService(LibraryEventPublisher eventPublisher) {
        this.reservations = new HashMap<>();
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "Event publisher cannot be null");
    }

    @Override
    public BookReservation createReservation(Book book, Patron patron) {
        Objects.requireNonNull(book, "Book cannot be null");
        Objects.requireNonNull(patron, "Patron cannot be null");

        // Create reservation
        String reservationId = "RES-" + System.currentTimeMillis();
        BookReservation reservation = new BookReservation(reservationId, book, patron);

        // Add to patron and store
        patron.addReservation(reservation);
        reservations.put(reservationId, reservation);

        logger.info("Book reserved: {} by patron {}", book.getTitle(), patron.getPatronId());
        return reservation;
    }

    @Override
    public void cancelReservation(String reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        BookReservation reservation = reservations.get(reservationId);

        if (reservation == null) {
            logger.warn("Reservation not found: {}", reservationId);
            throw new IllegalArgumentException("Reservation not found");
        }

        reservation.cancel();
        reservation.getPatron().removeReservation(reservation);
        logger.info("Reservation cancelled: {}", reservationId);
    }

    @Override
    public BookReservation getReservation(String reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        return reservations.get(reservationId);
    }

    @Override
    public List<BookReservation> getActiveReservationsForBook(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        return reservations.values().stream()
                .filter(r -> r.getBook().getIsbn().equals(isbn) && r.getStatus() == ReservationStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservation> getActiveReservationsForPatron(String patronId) {
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        return reservations.values().stream()
                .filter(r -> r.getPatron().getPatronId().equals(patronId) && r.getStatus() == ReservationStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public void processExpiredReservations() {
        List<BookReservation> expiredReservations = reservations.values().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE && r.isExpired())
                .collect(Collectors.toList());

        for (BookReservation reservation : expiredReservations) {
            reservation.expire();
            reservation.getPatron().removeReservation(reservation);
            logger.info("Reservation expired: {}", reservation.getReservationId());
        }
    }
}


package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a book reservation made by a patron.
 * Allows patrons to reserve books that are currently checked out.
 */
public class BookReservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final Book book;
    private final Patron patron;
    private final LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private ReservationStatus status;

    /**
     * Constructor for creating a new BookReservation.
     */
    public BookReservation(String reservationId, Book book, Patron patron) {
        this.reservationId = Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        this.book = Objects.requireNonNull(book, "Book cannot be null");
        this.patron = Objects.requireNonNull(patron, "Patron cannot be null");
        this.reservationDate = LocalDateTime.now();
        this.expiryDate = LocalDateTime.now().plusDays(7); // Reservation valid for 7 days
        this.status = ReservationStatus.ACTIVE;
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Check if the reservation is expired.
     */
    public boolean isExpired() {
        return status == ReservationStatus.ACTIVE && LocalDateTime.now().isAfter(expiryDate);
    }

    /**
     * Mark the reservation as fulfilled.
     */
    public void fulfill() {
        this.status = ReservationStatus.FULFILLED;
    }

    /**
     * Cancel the reservation.
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    /**
     * Expire the reservation.
     */
    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookReservation)) return false;
        BookReservation that = (BookReservation) o;
        return reservationId.equals(that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "BookReservation{" +
                "reservationId='" + reservationId + '\'' +
                ", book=" + book.getTitle() +
                ", patron=" + patron.getName() +
                ", reservationDate=" + reservationDate +
                ", status=" + status +
                '}';
    }
}


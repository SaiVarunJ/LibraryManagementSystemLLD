package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a library patron (member).
 * Maintains patron information and borrowing history.
 */
public class Patron implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String patronId;
    private String name;
    private String email;
    private String phoneNumber;
    private final LocalDateTime membershipDate;
    private PatronStatus status;
    private final List<BorrowRecord> borrowHistory;
    private final Set<Book> currentlyBorrowedBooks;
    private final List<BookReservation> reservations;

    /**
     * Constructor for creating a new Patron.
     */
    public Patron(String patronId, String name, String email, String phoneNumber) {
        this.patronId = Objects.requireNonNull(patronId, "Patron ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phoneNumber = phoneNumber;
        this.membershipDate = LocalDateTime.now();
        this.status = PatronStatus.ACTIVE;
        this.borrowHistory = new ArrayList<>();
        this.currentlyBorrowedBooks = new HashSet<>();
        this.reservations = new ArrayList<>();
    }

    // Getters
    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getMembershipDate() {
        return membershipDate;
    }

    public PatronStatus getStatus() {
        return status;
    }

    public List<BorrowRecord> getBorrowHistory() {
        return new ArrayList<>(borrowHistory);
    }

    public Set<Book> getCurrentlyBorrowedBooks() {
        return new HashSet<>(currentlyBorrowedBooks);
    }

    public List<BookReservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public int getCurrentBorrowCount() {
        return currentlyBorrowedBooks.size();
    }

    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(PatronStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Add a borrow record to patron's history.
     */
    public void addBorrowRecord(BorrowRecord record) {
        borrowHistory.add(Objects.requireNonNull(record, "Borrow record cannot be null"));
    }

    /**
     * Add a book to currently borrowed books.
     */
    public void borrowBook(Book book) {
        currentlyBorrowedBooks.add(Objects.requireNonNull(book, "Book cannot be null"));
    }

    /**
     * Remove a book from currently borrowed books.
     */
    public void returnBook(Book book) {
        currentlyBorrowedBooks.remove(Objects.requireNonNull(book, "Book cannot be null"));
    }

    /**
     * Check if patron can borrow more books (max 5 books at a time).
     */
    public boolean canBorrowMoreBooks() {
        return currentlyBorrowedBooks.size() < 5;
    }

    /**
     * Add a reservation for a book.
     */
    public void addReservation(BookReservation reservation) {
        reservations.add(Objects.requireNonNull(reservation, "Reservation cannot be null"));
    }

    /**
     * Remove a reservation.
     */
    public void removeReservation(BookReservation reservation) {
        reservations.remove(reservation);
    }

    /**
     * Get all active reservations.
     */
    public List<BookReservation> getActiveReservations() {
        return reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patron)) return false;
        Patron patron = (Patron) o;
        return patronId.equals(patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", currentlyBorrowedBooks=" + currentlyBorrowedBooks.size() +
                '}';
    }
}


package com.library.model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a library branch.
 * Supports multiple branches with independent book inventories.
 */
public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String branchId;
    private String branchName;
    private String location;
    private String phoneNumber;
    private final Map<String, Book> bookInventory; // Key: ISBN
    private final Set<Patron> registeredPatrons;

    /**
     * Constructor for creating a new Branch.
     */
    public Branch(String branchId, String branchName, String location, String phoneNumber) {
        this.branchId = Objects.requireNonNull(branchId, "Branch ID cannot be null");
        this.branchName = Objects.requireNonNull(branchName, "Branch name cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.phoneNumber = phoneNumber;
        this.bookInventory = new HashMap<>();
        this.registeredPatrons = new HashSet<>();
    }

    // Getters
    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Collection<Book> getAllBooks() {
        return new ArrayList<>(bookInventory.values());
    }

    public Set<Patron> getRegisteredPatrons() {
        return new HashSet<>(registeredPatrons);
    }

    // Setters
    public void setBranchName(String branchName) {
        this.branchName = Objects.requireNonNull(branchName, "Branch name cannot be null");
    }

    public void setLocation(String location) {
        this.location = Objects.requireNonNull(location, "Location cannot be null");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Add a book to this branch's inventory.
     */
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        bookInventory.put(book.getIsbn(), book);
    }

    /**
     * Remove a book from this branch's inventory.
     */
    public void removeBook(String isbn) {
        bookInventory.remove(Objects.requireNonNull(isbn, "ISBN cannot be null"));
    }

    /**
     * Get a book by ISBN.
     */
    public Book getBook(String isbn) {
        return bookInventory.get(Objects.requireNonNull(isbn, "ISBN cannot be null"));
    }

    /**
     * Register a patron to this branch.
     */
    public void registerPatron(Patron patron) {
        registeredPatrons.add(Objects.requireNonNull(patron, "Patron cannot be null"));
    }

    /**
     * Unregister a patron from this branch.
     */
    public void unregisterPatron(Patron patron) {
        registeredPatrons.remove(Objects.requireNonNull(patron, "Patron cannot be null"));
    }

    /**
     * Get total number of books in inventory.
     */
    public int getTotalBookCount() {
        return bookInventory.values().stream()
                .mapToInt(Book::getTotalCopies)
                .sum();
    }

    /**
     * Get total number of available books.
     */
    public int getAvailableBookCount() {
        return bookInventory.values().stream()
                .mapToInt(Book::getAvailableCopies)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch)) return false;
        Branch branch = (Branch) o;
        return branchId.equals(branch.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId);
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", location='" + location + '\'' +
                ", totalBooks=" + getTotalBookCount() +
                ", availableBooks=" + getAvailableBookCount() +
                '}';
    }
}

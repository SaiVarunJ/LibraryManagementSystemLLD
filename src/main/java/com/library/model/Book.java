package com.library.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Book in the library system.
 * This class encapsulates book information and implements Serializable for potential persistence.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;

    private String publisher;
    private BookStatus status;
    private int totalCopies;
    private int availableCopies;

    /**
     * Constructor for creating a new Book.
     */
    public Book(String isbn, String title, String author, int publicationYear, String publisher, int totalCopies) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
        this.publicationYear = publicationYear;
        this.publisher = Objects.requireNonNull(publisher, "Publisher cannot be null");
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public BookStatus getStatus() {
        return status;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // Setters
    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public void setAuthor(String author) {
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setPublisher(String publisher) {
        this.publisher = Objects.requireNonNull(publisher, "Publisher cannot be null");
    }

    public void setStatus(BookStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Increment available copies when a book is returned.
     */
    public void incrementAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            updateStatus();
        }
    }

    /**
     * Decrement available copies when a book is borrowed.
     */
    public void decrementAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
            updateStatus();
        }
    }

    /**
     * Check if book is available for borrowing.
     */
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    /**
     * Add more copies to the library inventory.
     */
    public void addCopies(int count) {
        if (count > 0) {
            totalCopies += count;
            availableCopies += count;
            updateStatus();
        }
    }

    /**
     * Update book status based on available copies.
     */
    private void updateStatus() {
        if (availableCopies == 0) {
            this.status = BookStatus.CHECKED_OUT;
        } else if (availableCopies < totalCopies) {
            this.status = BookStatus.PARTIALLY_AVAILABLE;
        } else {
            this.status = BookStatus.AVAILABLE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", publisher='" + publisher + '\'' +
                ", status=" + status +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                '}';
    }
}


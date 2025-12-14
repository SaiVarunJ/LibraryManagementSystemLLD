package com.library.service;

import com.library.model.Book;
import java.util.List;

/**
 * Interface for book-related operations.
 * Demonstrates the Single Responsibility Principle (SRP) and Interface Segregation Principle (ISP).
 */
public interface IBookService {
    /**
     * Add a new book to the library.
     */
    void addBook(Book book);

    /**
     * Remove a book from the library by ISBN.
     */
    void removeBook(String isbn);

    /**
     * Update an existing book's information.
     */
    void updateBook(Book book);

    /**
     * Get a book by ISBN.
     */
    Book getBook(String isbn);

    /**
     * Search for books by title.
     */
    List<Book> searchByTitle(String title);

    /**
     * Search for books by author.
     */
    List<Book> searchByAuthor(String author);

    /**
     * Search for books by ISBN.
     */
    List<Book> searchByIsbn(String isbn);

    /**
     * Get all books in the library.
     */
    List<Book> getAllBooks();

    /**
     * Get all available books.
     */
    List<Book> getAvailableBooks();
}


package com.library.service.impl;

import com.library.model.Book;
import com.library.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IBookService.
 * Manages book inventory operations.
 */
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final Map<String, Book> bookCatalog; // Key: ISBN

    public BookService() {
        this.bookCatalog = new HashMap<>();
    }

    @Override
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        bookCatalog.put(book.getIsbn(), book);
        logger.info("Book added: {} by {}", book.getTitle(), book.getAuthor());
    }

    @Override
    public void removeBook(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        Book removed = bookCatalog.remove(isbn);
        if (removed != null) {
            logger.info("Book removed: {} (ISBN: {})", removed.getTitle(), isbn);
        } else {
            logger.warn("Book not found for ISBN: {}", isbn);
        }
    }

    @Override
    public void updateBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        if (bookCatalog.containsKey(book.getIsbn())) {
            bookCatalog.put(book.getIsbn(), book);
            logger.info("Book updated: {}", book.getTitle());
        } else {
            logger.warn("Book not found for update: {}", book.getIsbn());
        }
    }

    @Override
    public Book getBook(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        return bookCatalog.get(isbn);
    }

    @Override
    public List<Book> searchByTitle(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        return bookCatalog.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        Objects.requireNonNull(author, "Author cannot be null");
        return bookCatalog.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByIsbn(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        List<Book> result = new ArrayList<>();
        Book book = bookCatalog.get(isbn);
        if (book != null) {
            result.add(book);
        }
        return result;
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCatalog.values());
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookCatalog.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
}


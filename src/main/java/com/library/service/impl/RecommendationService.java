package com.library.service.impl;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.service.IBookService;
import com.library.service.IRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IRecommendationService.
 * Provides book recommendations based on patron borrowing history and preferences.
 * Implements the Strategy Pattern for recommendation algorithms.
 */
public class RecommendationService implements IRecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private final IBookService bookService;
    private final Map<String, Integer> bookBorrowCount; // Track borrowing frequency
    private volatile RecommendationStrategy strategy;

    public RecommendationService(IBookService bookService) {
        this(bookService, new AuthorPublisherStrategy());
    }

    public RecommendationService(IBookService bookService, RecommendationStrategy strategy) {
        this.bookService = Objects.requireNonNull(bookService, "BookService cannot be null");
        this.bookBorrowCount = new HashMap<>();
        this.strategy = Objects.requireNonNull(strategy, "Strategy cannot be null");
        ensureBookCountsInitialized();
    }

    private void ensureBookCountsInitialized() {
        for (Book book : this.bookService.getAllBooks()) {
            bookBorrowCount.putIfAbsent(book.getIsbn(), 0);
        }
    }

    /**
     * Allow switching the recommendation strategy at runtime.
     * Uses a volatile field for safe publication; setter validates input.
     */
    public void setStrategy(RecommendationStrategy newStrategy) {
        this.strategy = Objects.requireNonNull(newStrategy, "Strategy cannot be null");
        logger.info("Recommendation strategy changed to {}", newStrategy.getClass().getSimpleName());
    }

    /**
     * Retrieve the currently active strategy (for testing or inspection).
     */
    public RecommendationStrategy getStrategy() {
        return this.strategy;
    }

    @Override
    public List<Book> getRecommendations(Patron patron, int count) {
        Objects.requireNonNull(patron, "Patron cannot be null");
        ensureBookCountsInitialized();
        RecommendationStrategy strategy = this.strategy;
        List<Book> allBooks = this.bookService.getAllBooks();
        List<Book> recommendations = strategy.recommend(patron, allBooks, bookBorrowCount, count);
        logger.info("Generated {} recommendations for patron {} using strategy {}", recommendations.size(), patron.getPatronId(), strategy.getClass().getSimpleName());
        return recommendations;
    }

    @Override
    public List<Book> getSimilarBooks(Book book, int count) {
        Objects.requireNonNull(book, "Book cannot be null");
        ensureBookCountsInitialized();
        List<Book> allBooks = this.bookService.getAllBooks();

        return allBooks.stream()
                .filter(b -> !b.equals(book))
                .filter(b -> b.getAuthor().equalsIgnoreCase(book.getAuthor()) ||
                        b.getPublisher().equalsIgnoreCase(book.getPublisher()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getPopularBooks(int count) {
        ensureBookCountsInitialized();
        List<Book> allBooks = this.bookService.getAllBooks();
        return allBooks.stream()
                .sorted((b1, b2) -> {
                    int count1 = bookBorrowCount.getOrDefault(b1.getIsbn(), 0);
                    int count2 = bookBorrowCount.getOrDefault(b2.getIsbn(), 0);
                    return Integer.compare(count2, count1); // Descending order
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Update borrow count when a book is borrowed (called by LendingService).
     */
    @SuppressWarnings("unused")
    public void updateBorrowCount(String isbn) {
        bookBorrowCount.put(isbn, bookBorrowCount.getOrDefault(isbn, 0) + 1);
    }

    @Override
    public void recordBorrow(String isbn) {
        // Ensure the book map is aware of this ISBN and increment
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        bookBorrowCount.putIfAbsent(isbn, 0);
        updateBorrowCount(isbn);
        logger.debug("Recorded borrow for ISBN: {} (new count={})", isbn, bookBorrowCount.getOrDefault(isbn, 0));
    }
}

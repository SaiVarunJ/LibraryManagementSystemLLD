package com.library.service;

import com.library.model.Book;
import com.library.model.Patron;
import java.util.List;

/**
 * Interface for recommendation-related operations.
 * Demonstrates the Strategy Pattern for recommendations.
 */
public interface IRecommendationService {
    /**
     * Get book recommendations for a patron based on their borrowing history.
     */
    List<Book> getRecommendations(Patron patron, int count);

    /**
     * Get similar books to a given book.
     */
    List<Book> getSimilarBooks(Book book, int count);

    /**
     * Get popular books based on borrowing frequency.
     */
    List<Book> getPopularBooks(int count);

    /**
     * Notify the recommendation service that a book was borrowed (used to update popularity metrics).
     */
    void recordBorrow(String isbn);
}

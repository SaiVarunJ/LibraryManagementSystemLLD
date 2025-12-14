package com.library.service.impl;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Patron;

import java.util.*;
import java.util.stream.Collectors;

public class AuthorPublisherStrategy implements RecommendationStrategy {

    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, Map<String, Integer> borrowCount, int count) {
        Objects.requireNonNull(patron, "Patron cannot be null");
        List<BorrowRecord> borrowHistory = Optional.ofNullable(patron.getBorrowHistory()).orElse(Collections.emptyList());
        if (borrowHistory.isEmpty()) {
            // Fallback to popularity strategy when no history
            return new PopularityStrategy().recommend(patron, allBooks, borrowCount, count);
        }

        Set<String> favoriteAuthors = borrowHistory.stream()
                .map(r -> r.getBook().getAuthor())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> favoritePublishers = borrowHistory.stream()
                .map(r -> r.getBook().getPublisher())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Book> alreadyBorrowed = borrowHistory.stream()
                .map(BorrowRecord::getBook)
                .collect(Collectors.toSet());

        Set<Book> currentlyBorrowed = Optional.ofNullable(patron.getCurrentlyBorrowedBooks()).orElse(Collections.emptySet());

        Map<Book, Integer> scoreMap = new HashMap<>();
        for (Book b : allBooks) {
            if (currentlyBorrowed.contains(b) || alreadyBorrowed.contains(b)) continue;
            int score = 0;
            if (favoriteAuthors.contains(b.getAuthor())) score += 10;
            if (favoritePublishers.contains(b.getPublisher())) score += 5;
            score += borrowCount.getOrDefault(b.getIsbn(), 0);
            scoreMap.put(b, score);
        }

        return scoreMap.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(Math.max(0, count))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}


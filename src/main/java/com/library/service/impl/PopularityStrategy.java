package com.library.service.impl;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.*;
import java.util.stream.Collectors;

public class PopularityStrategy implements RecommendationStrategy {

    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, Map<String, Integer> borrowCount, int count) {
        if (allBooks == null) return Collections.emptyList();
        return allBooks.stream()
                .sorted((b1, b2) -> Integer.compare(borrowCount.getOrDefault(b2.getIsbn(), 0),
                                                     borrowCount.getOrDefault(b1.getIsbn(), 0)))
                .limit(Math.max(0, count))
                .collect(Collectors.toList());
    }
}


package com.library.service.impl;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.List;
import java.util.Map;

public interface RecommendationStrategy {
    List<Book> recommend(Patron patron, List<Book> allBooks, Map<String, Integer> borrowCount, int count);
}


package com.library.service.impl;

import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookService();
    }

    @Test
    public void testAddAndGetBook() {
        Book book = new Book("TST-001", "Test Driven Development", "Kent Beck", 2003, "Addison-Wesley", 2);
        bookService.addBook(book);

        Book fetched = bookService.getBook("TST-001");
        assertNotNull(fetched, "Book should be retrievable after add");
        assertEquals("Test Driven Development", fetched.getTitle());

        List<Book> all = bookService.getAllBooks();
        assertEquals(1, all.size());
    }

    @Test
    public void testRemoveBook() {
        Book book = new Book("TST-002", "Refactoring", "Martin Fowler", 1999, "Addison-Wesley", 1);
        bookService.addBook(book);
        assertNotNull(bookService.getBook("TST-002"));

        bookService.removeBook("TST-002");
        assertNull(bookService.getBook("TST-002"));
        assertTrue(bookService.getAllBooks().isEmpty());

        // Removing non-existent book should not throw
        bookService.removeBook("NO-SUCH-ISBN");
    }

    @Test
    public void testUpdateBook() {
        Book book = new Book("TST-003", "Old Title", "Some Author", 2000, "Publisher", 3);
        bookService.addBook(book);

        Book updated = new Book("TST-003", "New Title", "Some Author", 2001, "Publisher", 3);
        // updateBook replaces the entry only if exists
        bookService.updateBook(updated);

        Book fetched = bookService.getBook("TST-003");
        assertNotNull(fetched);
        assertEquals("New Title", fetched.getTitle());
        assertEquals(2001, fetched.getPublicationYear());
    }

    @Test
    public void testSearchAndAvailability() {
        Book b1 = new Book("TST-004", "Clean Code", "Robert C. Martin", 2008, "Prentice Hall", 2);
        Book b2 = new Book("TST-005", "Clean Architecture", "Robert C. Martin", 2017, "Pearson", 1);
        bookService.addBook(b1);
        bookService.addBook(b2);

        List<Book> byTitle = bookService.searchByTitle("Clean");
        assertEquals(2, byTitle.size());

        List<Book> byAuthor = bookService.searchByAuthor("Robert");
        assertEquals(2, byAuthor.size());

        List<Book> available = bookService.getAvailableBooks();
        assertEquals(2, available.size());

        // Decrement available copies on one book and verify availability
        b2.decrementAvailableCopies();
        List<Book> availableAfter = bookService.getAvailableBooks();
        assertEquals(1, availableAfter.size());
    }
}


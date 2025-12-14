package com.library.service.impl;

import com.library.Library;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Patron;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LendingBorrowedBooksTest {

    @Test
    public void testCheckoutAndReturnUpdatesBorrowedListAndInventory() {
        Library library = new Library();

        // create and add book and patron
        Book book = new Book("LB-001", "Lending Book", "Author A", 2020, "Pub", 2);
        Patron patron = new Patron("PT-200", "Borrower", "b@example.com", "555-2000");

        library.addBook(book);
        library.addPatron(patron);

        // Verify initial available copies
        Book stored = library.getAllBooks().stream().filter(b -> b.getIsbn().equals("LB-001")).findFirst().orElse(null);
        assertNotNull(stored);
        int initialAvailable = stored.getAvailableCopies();
        assertEquals(2, initialAvailable);

        // Checkout
        BorrowRecord rec = library.checkoutBook("LB-001", "PT-200");
        assertNotNull(rec);

        // Borrowed books for patron should include the book
        List<com.library.model.Book> borrowed = library.getBorrowedBooksForPatron("PT-200");
        assertEquals(1, borrowed.size());
        assertEquals("LB-001", borrowed.get(0).getIsbn());

        // Available copies should decrease by 1
        Book afterCheckout = library.getAllBooks().stream().filter(b -> b.getIsbn().equals("LB-001")).findFirst().orElse(null);
        assertNotNull(afterCheckout);
        assertEquals(initialAvailable - 1, afterCheckout.getAvailableCopies());

        // Return
        library.returnBook(rec.getRecordId());

        // Borrowed books should now be empty
        List<com.library.model.Book> borrowedAfter = library.getBorrowedBooksForPatron("PT-200");
        assertEquals(0, borrowedAfter.size());

        // Available copies restored
        Book afterReturn = library.getAllBooks().stream().filter(b -> b.getIsbn().equals("LB-001")).findFirst().orElse(null);
        assertNotNull(afterReturn);
        assertEquals(initialAvailable, afterReturn.getAvailableCopies());
    }
}


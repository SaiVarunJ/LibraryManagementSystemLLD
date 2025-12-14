package com.library.demo;

import com.library.Library;
import com.library.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo application showcasing the Library Management System.
 * Demonstrates all core features and optional extensions.
 */
public class LibraryDemo {
    private static final Logger logger = LoggerFactory.getLogger(LibraryDemo.class);

    public static void main(String[] args) {
        logger.info("Starting Library Management System Demo");

        Library library = new Library();

        // ==================== BOOK MANAGEMENT DEMO ====================
        System.out.println("\n========== BOOK MANAGEMENT ==========");
        demonstrateBookManagement(library);

        // ==================== PATRON MANAGEMENT DEMO ====================
        System.out.println("\n========== PATRON MANAGEMENT ==========");
        demonstratePatronManagement(library);

        // ==================== LENDING OPERATIONS DEMO ====================
        System.out.println("\n========== LENDING OPERATIONS ==========");
        demonstrateLendingOperations(library);

        // ==================== RESERVATION DEMO ====================
        System.out.println("\n========== RESERVATION SYSTEM ==========");
        demonstrateReservations(library);

        // ==================== RECOMMENDATION DEMO ====================
        System.out.println("\n========== RECOMMENDATION SYSTEM ==========");
        demonstrateRecommendations(library);

        // ==================== BRANCH MANAGEMENT DEMO ====================
        System.out.println("\n========== BRANCH MANAGEMENT ==========");
        demonstrateBranchOperations(library);

        // ==================== LIBRARY STATISTICS ====================
        library.printLibraryStatistics();
    }

    /**
     * Demonstrate book management functionality.
     */
    private static void demonstrateBookManagement(Library library) {
        // Add books
        Book book1 = new Book("ISBN001", "Clean Code", "Robert C. Martin", 2008, "Prentice Hall", 5);
        Book book2 = new Book("ISBN002", "Design Patterns", "Gang of Four", 1994, "Addison-Wesley", 3);
        Book book3 = new Book("ISBN003", "Refactoring", "Martin Fowler", 1999, "Addison-Wesley", 4);
        Book book4 = new Book("ISBN004", "The Pragmatic Programmer", "David Thomas", 1999, "Addison-Wesley", 2);
        // Additional books to create clearer differences between strategies
        Book book5 = new Book("ISBN005", "Effective Java", "Joshua Bloch", 2001, "Addison-Wesley", 4);
        Book book6 = new Book("ISBN006", "Clean Architecture", "Robert C. Martin", 2017, "Pearson", 3);
        Book book7 = new Book("ISBN007", "Working Effectively with Legacy Code", "Michael Feathers", 2004, "Prentice Hall", 2);
        Book book8 = new Book("ISBN008", "Patterns of Enterprise Application Architecture", "Martin Fowler", 2002, "Addison-Wesley", 2);
        Book book9 = new Book("ISBN009", "Head First Design Patterns", "Eric Freeman", 2004, "O'Reilly", 3);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        library.addBook(book6);
        library.addBook(book7);
        library.addBook(book8);
        library.addBook(book9);

        System.out.println("✓ Added 9 books to the library");

        // Search by title
        List<Book> searchResults = library.searchBooksByTitle("Clean");
        System.out.println("✓ Search by title 'Clean': Found " + searchResults.size() + " book(s)");

        // Search by author
        searchResults = library.searchBooksByAuthor("Martin");
        System.out.println("✓ Search by author 'Martin': Found " + searchResults.size() + " book(s)");

        // Get available books
        List<Book> availableBooks = library.getAvailableBooks();
        System.out.println("✓ Available books: " + availableBooks.size());

        // --- Demonstrate update and remove operations ---
        System.out.println("\n-> Updating book ISBN005 (Effective Java) title to 'Effective Java - 3rd Edition'");
        Book toUpdate = library.getAllBooks().stream().filter(b -> b.getIsbn().equals("ISBN005")).findFirst().orElse(null);
        if (toUpdate != null) {
            toUpdate.setTitle("Effective Java - 3rd Edition");
            toUpdate.setPublicationYear(2018);
            library.updateBook(toUpdate);
            System.out.println("✓ Updated ISBN005: " + library.getAllBooks().stream().filter(b -> b.getIsbn().equals("ISBN005")).findFirst().get().getTitle());
        }

        System.out.println("\n-> Removing book ISBN009 (Head First Design Patterns) from inventory");
        library.removeBook("ISBN009");
        boolean exists = library.getAllBooks().stream().anyMatch(b -> b.getIsbn().equals("ISBN009"));
        System.out.println("✓ ISBN009 present after removal? " + exists);
    }

    /**
     * Demonstrate patron management functionality.
     */
    private static void demonstratePatronManagement(Library library) {
        // Add patrons
        Patron patron1 = new Patron("P001", "John Doe", "john@example.com", "555-0001");
        Patron patron2 = new Patron("P002", "Jane Smith", "jane@example.com", "555-0002");
        Patron patron3 = new Patron("P003", "Bob Johnson", "bob@example.com", "555-0003");

        library.addPatron(patron1);
        library.addPatron(patron2);
        library.addPatron(patron3);

        System.out.println("✓ Added 3 patrons to the library");

        // Search patron by name
        List<Patron> searchResults = library.searchPatronsByName("John");
        System.out.println("✓ Search by name 'John': Found " + searchResults.size() + " patron(s)");

        // Get all patrons
        List<Patron> allPatrons = library.getAllPatrons();
        System.out.println("✓ Total patrons: " + allPatrons.size());

        // --- Demonstrate updating a patron's information ---
        System.out.println("\n-> Updating patron P001's contact info (email & phone)");
        Patron p = library.getPatron("P001");
        if (p != null) {
            p.setEmail("john.doe@newdomain.com");
            p.setPhoneNumber("555-9999");
            library.updatePatron(p);
            Patron updated = library.getPatron("P001");
            System.out.println("✓ Updated P001: email=" + updated.getEmail() + ", phone=" + updated.getPhoneNumber());
        }
    }

    /**
     * Demonstrate lending operations.
     */
    private static void demonstrateLendingOperations(Library library) {
        // Checkout books
        try {
            BorrowRecord record1 = library.checkoutBook("ISBN001", "P001");
            System.out.println("✓ Patron P001 checked out '" + record1.getBook().getTitle() + "'");
            System.out.println("  Due date: " + record1.getDueDate());

            BorrowRecord record2 = library.checkoutBook("ISBN002", "P002");
            System.out.println("✓ Patron P002 checked out '" + record2.getBook().getTitle() + "'");

            BorrowRecord record3 = library.checkoutBook("ISBN003", "P001");
            System.out.println("✓ Patron P001 checked out '" + record3.getBook().getTitle() + "'");

            // Get active borrow records
            List<BorrowRecord> activeBorrows = library.getActiveBorrowRecords();
            System.out.println("✓ Active borrow records: " + activeBorrows.size());

            // Renew a book
            library.renewBook(record1.getRecordId());
            System.out.println("✓ Book renewed for patron P001");

            // Return a book
            library.returnBook(record2.getRecordId());
            System.out.println("✓ Patron P002 returned '" + record2.getBook().getTitle() + "'");

            // --- Additional checkouts to produce popularity differences ---
            // Checkout and return ISBN004 twice by different patrons to increase its borrow count
            BorrowRecord extra1 = library.checkoutBook("ISBN004", "P003");
            library.returnBook(extra1.getRecordId());
            BorrowRecord extra2 = library.checkoutBook("ISBN004", "P002");
            library.returnBook(extra2.getRecordId());

            // Checkout and return ISBN002 once more
            BorrowRecord extra3 = library.checkoutBook("ISBN002", "P003");
            library.returnBook(extra3.getRecordId());

            // Now popularity counts: ISBN001, ISBN003, ISBN004, ISBN002 have borrows

            // --- Print borrow history for patron P001 ---
            System.out.println("\n--- Borrow history for P001 ---");
            List<BorrowRecord> history = library.getBorrowHistoryForPatron("P001");
            System.out.println("History entries: " + history.size());
            for (BorrowRecord br : history) {
                System.out.println(" - " + br.getBook().getTitle()
                        + " | status=" + br.getStatus()
                        + " | borrowed=" + br.getBorrowDate()
                        + (br.getReturnDate() != null ? " | returned=" + br.getReturnDate() : ""));
            }

        } catch (Exception e) {
            logger.error("Error during lending operations", e);
        }
    }

    /**
     * Demonstrate reservation functionality.
     */
    private static void demonstrateReservations(Library library) {
        try {
            // Create reservations
            BookReservation res1 = library.reserveBook("ISBN001", "P002");
            System.out.println("✓ Patron P002 reserved book (Reservation ID: " + res1.getReservationId() + ")");

            BookReservation res2 = library.reserveBook("ISBN002", "P003");
            System.out.println("✓ Patron P003 reserved book (Reservation ID: " + res2.getReservationId() + ")");

            // Get reservations for a patron
            List<BookReservation> patronReservations = library.getReservationsForPatron("P002");
            System.out.println("✓ Active reservations for P002: " + patronReservations.size());

            // Get reservations for a book
            List<BookReservation> bookReservations = library.getReservationsForBook("ISBN001");
            System.out.println("✓ Active reservations for ISBN001: " + bookReservations.size());

        } catch (Exception e) {
            logger.error("Error during reservation operations", e);
        }
    }

    /**
     * Demonstrate recommendation system.
     */
    private static void demonstrateRecommendations(Library library) {
        try {
            // Get recommendations for patron P001 (who borrowed Clean Code and Refactoring)
            List<Book> recommendations = library.getRecommendationsForPatron("P001", 3);
            System.out.println("✓ Recommendations for P001: " + recommendations.size() + " book(s)");
            recommendations.forEach(b -> System.out.println("  - " + b.getTitle() + " by " + b.getAuthor()));

            // --- Now switch recommendation strategy at runtime to PopularityStrategy ---
            System.out.println("\n-> Switching recommendation strategy to PopularityStrategy at runtime...");
            library.setRecommendationStrategy(new com.library.service.impl.PopularityStrategy());

            // Re-run recommendations to show the effect of the new strategy
            List<Book> recommendationsAfter = library.getRecommendationsForPatron("P001", 3);
            System.out.println("✓ Recommendations for P001 after switching to PopularityStrategy: " + recommendationsAfter.size() + " book(s)");
            recommendationsAfter.forEach(b -> System.out.println("  - " + b.getTitle() + " by " + b.getAuthor()));

        } catch (Exception e) {
            logger.error("Error during recommendation operations", e);
        }
    }

    /**
     * Demonstrate multi-branch operations.
     */
    private static void demonstrateBranchOperations(Library library) {
        try {
            // Create branches
            Branch branch1 = new Branch("B001", "Downtown Library", "123 Main St", "555-1000");
            Branch branch2 = new Branch("B002", "Uptown Library", "456 Oak Ave", "555-2000");

            library.addBranch(branch1);
            library.addBranch(branch2);
            System.out.println("✓ Added 2 library branches");

            // Add books to branches
            Book book1 = new Book("ISBN001", "Clean Code", "Robert C. Martin", 2008, "Prentice Hall", 5);
            Book book2 = new Book("ISBN002", "Design Patterns", "Gang of Four", 1994, "Addison-Wesley", 3);

            branch1.addBook(book1);
            branch1.addBook(book2);
            branch2.addBook(new Book("ISBN001", "Clean Code", "Robert C. Martin", 2008, "Prentice Hall", 2));

            System.out.println("✓ Added books to branches");

            // Check availability across branches
            java.util.Map<String, Integer> availability = library.getBookAvailabilityAcrossBranches("ISBN001");
            System.out.println("✓ Book ISBN001 available at " + availability.size() + " branch(es)");
            availability.forEach((branchId, count) ->
                System.out.println("  Branch " + branchId + ": " + count + " copies")
            );

            // Transfer books between branches
            library.transferBooks("B001", "B002", "ISBN001", 2);
            System.out.println("✓ Transferred 2 copies of ISBN001 from B001 to B002");

        } catch (Exception e) {
            logger.error("Error during branch operations", e);
        }
    }
}

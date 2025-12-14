package com.library.observer;

/**
 * Observer interface for library events.
 * Implements the Observer Design Pattern for notifications.
 */
public interface LibraryEventObserver {
    /**
     * Called when a book becomes available after being reserved.
     */
    void onBookAvailable(String bookIsbn, String patronId);

    /**
     * Called when a patron's borrowed book is overdue.
     */
    void onBookOverdue(String recordId);

    /**
     * Called when a book is returned to the library.
     */
    void onBookReturned(String recordId);
}


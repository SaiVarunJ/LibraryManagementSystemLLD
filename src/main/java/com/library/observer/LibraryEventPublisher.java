package com.library.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Event publisher for library events.
 * Manages observers and notifies them of library events.
 * Implements the Observer Design Pattern.
 */
public class LibraryEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(LibraryEventPublisher.class);
    private final List<LibraryEventObserver> observers = new ArrayList<>();

    /**
     * Subscribe an observer to library events.
     */
    public void subscribe(LibraryEventObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer subscribed: {}", observer.getClass().getSimpleName());
        }
    }

    /**
     * Unsubscribe an observer from library events.
     */
    public void unsubscribe(LibraryEventObserver observer) {
        if (observers.remove(observer)) {
            logger.info("Observer unsubscribed: {}", observer.getClass().getSimpleName());
        }
    }

    /**
     * Notify all observers that a book is now available.
     */
    public void publishBookAvailableEvent(String bookIsbn, String patronId) {
        logger.info("Publishing book available event for book {} and patron {}", bookIsbn, patronId);
        for (LibraryEventObserver observer : observers) {
            observer.onBookAvailable(bookIsbn, patronId);
        }
    }

    /**
     * Notify all observers about overdue book.
     */
    public void publishBookOverdueEvent(String recordId) {
        logger.warn("Publishing book overdue event for record {}", recordId);
        for (LibraryEventObserver observer : observers) {
            observer.onBookOverdue(recordId);
        }
    }

    /**
     * Notify all observers about book return.
     */
    public void publishBookReturnedEvent(String recordId) {
        logger.info("Publishing book returned event for record {}", recordId);
        for (LibraryEventObserver observer : observers) {
            observer.onBookReturned(recordId);
        }
    }

    /**
     * Get count of subscribed observers.
     */
    public int getObserverCount() {
        return observers.size();
    }
}


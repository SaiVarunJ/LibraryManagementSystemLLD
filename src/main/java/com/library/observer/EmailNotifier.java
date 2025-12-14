package com.library.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email notification implementation.
 * Notifies patrons via email about library events.
 */
public class EmailNotifier implements LibraryEventObserver {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotifier.class);

    @Override
    public void onBookAvailable(String bookIsbn, String patronId) {
        logger.info("Sending email to patron {} about available book {}", patronId, bookIsbn);
        System.out.println("Email sent to patron " + patronId + ": Book " + bookIsbn + " is now available!");
    }

    @Override
    public void onBookOverdue(String recordId) {
        logger.warn("Sending overdue notification for record {}", recordId);
        System.out.println("Email sent: Your borrowed book (Record: " + recordId + ") is overdue!");
    }

    @Override
    public void onBookReturned(String recordId) {
        logger.info("Sending return confirmation for record {}", recordId);
        System.out.println("Email sent: Return confirmation for record " + recordId);
    }
}


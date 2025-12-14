package com.library.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SMS notification implementation.
 * Notifies patrons via SMS about library events.
 */
public class SMSNotifier implements LibraryEventObserver {
    private static final Logger logger = LoggerFactory.getLogger(SMSNotifier.class);

    @Override
    public void onBookAvailable(String bookIsbn, String patronId) {
        logger.info("Sending SMS to patron {} about available book {}", patronId, bookIsbn);
        System.out.println("SMS sent to patron " + patronId + ": Book " + bookIsbn + " is now available!");
    }

    @Override
    public void onBookOverdue(String recordId) {
        logger.warn("Sending overdue SMS for record {}", recordId);
        System.out.println("SMS sent: Your borrowed book (Record: " + recordId + ") is overdue!");
    }

    @Override
    public void onBookReturned(String recordId) {
        logger.info("Sending return confirmation SMS for record {}", recordId);
        System.out.println("SMS sent: Return confirmation for record " + recordId);
    }
}


package com.library.util;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 */
public class IdGenerator {

    /**
     * Generate a unique record ID for borrowing records.
     */
    public static String generateRecordId() {
        return "BR-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generate a unique reservation ID.
     */
    public static String generateReservationId() {
        return "RES-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generate a unique patron ID.
     */
    public static String generatePatronId() {
        return "P-" + System.currentTimeMillis();
    }

    /**
     * Generate a unique branch ID.
     */
    public static String generateBranchId() {
        return "B-" + System.currentTimeMillis();
    }
}


package com.library.model;

/**
 * Enum representing the status of a book in the library.
 */
public enum BookStatus {
    AVAILABLE("Available"),
    PARTIALLY_AVAILABLE("Partially Available"),
    CHECKED_OUT("Checked Out"),
    RESERVED("Reserved");

    private final String displayName;

    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


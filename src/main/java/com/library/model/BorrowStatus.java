package com.library.model;

/**
 * Enum representing the status of a borrow record.
 */
public enum BorrowStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    OVERDUE("Overdue");

    private final String displayName;

    BorrowStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


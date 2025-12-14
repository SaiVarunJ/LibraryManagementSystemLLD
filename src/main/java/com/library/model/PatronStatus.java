package com.library.model;

/**
 * Enum representing the status of a patron.
 */
public enum PatronStatus {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    INACTIVE("Inactive");

    private final String displayName;

    PatronStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


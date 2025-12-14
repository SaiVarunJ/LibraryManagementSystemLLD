package com.library.model;

/**
 * Enum representing the status of a book reservation.
 */
public enum ReservationStatus {
    ACTIVE("Active"),
    FULFILLED("Fulfilled"),
    CANCELLED("Cancelled"),
    EXPIRED("Expired");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


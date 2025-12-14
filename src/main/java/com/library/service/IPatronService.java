package com.library.service;

import com.library.model.Patron;
import java.util.List;

/**
 * Interface for patron-related operations.
 * Demonstrates the Single Responsibility Principle (SRP).
 */
public interface IPatronService {
    /**
     * Add a new patron to the library.
     */
    void addPatron(Patron patron);

    /**
     * Remove a patron from the library.
     */
    void removePatron(String patronId);

    /**
     * Update patron information.
     */
    void updatePatron(Patron patron);

    /**
     * Get a patron by ID.
     */
    Patron getPatron(String patronId);

    /**
     * Get all patrons in the library.
     */
    List<Patron> getAllPatrons();

    /**
     * Search for patrons by name.
     */
    List<Patron> searchByName(String name);
}


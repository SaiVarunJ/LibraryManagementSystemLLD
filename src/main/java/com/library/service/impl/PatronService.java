package com.library.service.impl;

import com.library.model.Patron;
import com.library.service.IPatronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IPatronService.
 * Manages patron-related operations.
 */
public class PatronService implements IPatronService {
    private static final Logger logger = LoggerFactory.getLogger(PatronService.class);
    private final Map<String, Patron> patrons; // Key: Patron ID

    public PatronService() {
        this.patrons = new HashMap<>();
    }

    @Override
    public void addPatron(Patron patron) {
        Objects.requireNonNull(patron, "Patron cannot be null");
        patrons.put(patron.getPatronId(), patron);
        logger.info("Patron added: {} (ID: {})", patron.getName(), patron.getPatronId());
    }

    @Override
    public void removePatron(String patronId) {
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        Patron removed = patrons.remove(patronId);
        if (removed != null) {
            logger.info("Patron removed: {} (ID: {})", removed.getName(), patronId);
        } else {
            logger.warn("Patron not found: {}", patronId);
        }
    }

    @Override
    public void updatePatron(Patron patron) {
        Objects.requireNonNull(patron, "Patron cannot be null");
        if (patrons.containsKey(patron.getPatronId())) {
            patrons.put(patron.getPatronId(), patron);
            logger.info("Patron updated: {}", patron.getName());
        } else {
            logger.warn("Patron not found for update: {}", patron.getPatronId());
        }
    }

    @Override
    public Patron getPatron(String patronId) {
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        return patrons.get(patronId);
    }

    @Override
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }

    @Override
    public List<Patron> searchByName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        return patrons.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}


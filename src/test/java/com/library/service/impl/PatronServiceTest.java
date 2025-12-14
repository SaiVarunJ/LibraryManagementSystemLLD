package com.library.service.impl;

import com.library.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatronServiceTest {
    private PatronService patronService;

    @BeforeEach
    public void setUp() {
        patronService = new PatronService();
    }

    @Test
    public void testAddAndGetPatron() {
        Patron p = new Patron("PT-001", "Alice", "alice@example.com", "555-1010");
        patronService.addPatron(p);

        Patron fetched = patronService.getPatron("PT-001");
        assertNotNull(fetched);
        assertEquals("Alice", fetched.getName());

        List<Patron> all = patronService.getAllPatrons();
        assertEquals(1, all.size());
    }

    @Test
    public void testUpdatePatron() {
        Patron p = new Patron("PT-002", "Bob", "bob@example.com", "555-2020");
        patronService.addPatron(p);

        p.setEmail("bob.new@example.com");
        p.setPhoneNumber("555-9999");
        patronService.updatePatron(p);

        Patron updated = patronService.getPatron("PT-002");
        assertEquals("bob.new@example.com", updated.getEmail());
        assertEquals("555-9999", updated.getPhoneNumber());
    }

    @Test
    public void testRemoveAndSearch() {
        Patron p1 = new Patron("PT-003", "Carol", "carol@example.com", "555-3030");
        Patron p2 = new Patron("PT-004", "Caroline", "caroline@example.com", "555-4040");
        patronService.addPatron(p1);
        patronService.addPatron(p2);

        List<Patron> found = patronService.searchByName("Carol");
        assertEquals(2, found.size());

        patronService.removePatron("PT-003");
        assertNull(patronService.getPatron("PT-003"));
        assertNotNull(patronService.getPatron("PT-004"));

        // Removing non-existent should not throw
        patronService.removePatron("NO-SUCH-ID");
    }
}


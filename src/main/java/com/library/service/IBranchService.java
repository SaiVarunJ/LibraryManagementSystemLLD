package com.library.service;

import com.library.model.Book;
import com.library.model.Branch;

/**
 * Interface for branch-related operations.
 * Supports multi-branch library system with book transfers.
 */
public interface IBranchService {
    /**
     * Add a new branch to the library system.
     */
    void addBranch(Branch branch);

    /**
     * Remove a branch from the library system.
     */
    void removeBranch(String branchId);

    /**
     * Get a branch by ID.
     */
    Branch getBranch(String branchId);

    /**
     * Get all branches.
     */
    java.util.List<Branch> getAllBranches();

    /**
     * Transfer books between branches.
     */
    void transferBooks(String sourceBranchId, String targetBranchId, String isbn, int quantity);

    /**
     * Get book availability across all branches.
     */
    java.util.Map<String, Integer> getBookAvailabilityAcrossBranches(String isbn);
}


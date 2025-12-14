package com.library.service.impl;

import com.library.model.Book;
import com.library.model.Branch;
import com.library.service.IBranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IBranchService.
 * Manages library branches and book transfers between branches.
 */
public class BranchService implements IBranchService {
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);
    private final Map<String, Branch> branches; // Key: Branch ID

    public BranchService() {
        this.branches = new HashMap<>();
    }

    @Override
    public void addBranch(Branch branch) {
        Objects.requireNonNull(branch, "Branch cannot be null");
        branches.put(branch.getBranchId(), branch);
        logger.info("Branch added: {} at {}", branch.getBranchName(), branch.getLocation());
    }

    @Override
    public void removeBranch(String branchId) {
        Objects.requireNonNull(branchId, "Branch ID cannot be null");
        Branch removed = branches.remove(branchId);
        if (removed != null) {
            logger.info("Branch removed: {}", removed.getBranchName());
        } else {
            logger.warn("Branch not found: {}", branchId);
        }
    }

    @Override
    public Branch getBranch(String branchId) {
        Objects.requireNonNull(branchId, "Branch ID cannot be null");
        return branches.get(branchId);
    }

    @Override
    public List<Branch> getAllBranches() {
        return new ArrayList<>(branches.values());
    }

    @Override
    public void transferBooks(String sourceBranchId, String targetBranchId, String isbn, int quantity) {
        Objects.requireNonNull(sourceBranchId, "Source branch ID cannot be null");
        Objects.requireNonNull(targetBranchId, "Target branch ID cannot be null");
        Objects.requireNonNull(isbn, "ISBN cannot be null");

        Branch sourceBranch = branches.get(sourceBranchId);
        Branch targetBranch = branches.get(targetBranchId);

        if (sourceBranch == null) {
            logger.warn("Source branch not found: {}", sourceBranchId);
            throw new IllegalArgumentException("Source branch not found");
        }

        if (targetBranch == null) {
            logger.warn("Target branch not found: {}", targetBranchId);
            throw new IllegalArgumentException("Target branch not found");
        }

        Book sourceBook = sourceBranch.getBook(isbn);
        if (sourceBook == null || sourceBook.getAvailableCopies() < quantity) {
            logger.warn("Insufficient copies available for transfer");
            throw new IllegalStateException("Insufficient copies available for transfer");
        }

        // Transfer books
        for (int i = 0; i < quantity; i++) {
            sourceBook.decrementAvailableCopies();
        }

        Book targetBook = targetBranch.getBook(isbn);
        if (targetBook == null) {
            // Create new book entry in target branch
            Book newBook = new Book(sourceBook.getIsbn(), sourceBook.getTitle(),
                    sourceBook.getAuthor(), sourceBook.getPublicationYear(),
                    sourceBook.getPublisher(), quantity);
            targetBranch.addBook(newBook);
        } else {
            targetBook.addCopies(quantity);
        }

        logger.info("Transferred {} copies of {} from {} to {}",
                quantity, sourceBook.getTitle(), sourceBranch.getBranchName(), targetBranch.getBranchName());
    }

    @Override
    public Map<String, Integer> getBookAvailabilityAcrossBranches(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        Map<String, Integer> availability = new HashMap<>();

        for (Branch branch : branches.values()) {
            Book book = branch.getBook(isbn);
            if (book != null && book.getAvailableCopies() > 0) {
                availability.put(branch.getBranchId(), book.getAvailableCopies());
            }
        }

        return availability;
    }
}


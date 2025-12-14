# Library Management System (LLD)

A comprehensive, well-architected Library Management System built in Java, demonstrating advanced Object-Oriented Programming (OOP), SOLID principles, and Design Patterns.

## 📋 Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Architecture & Design](#architecture--design)
- [Design Patterns Used](#design-patterns-used)
- [SOLID Principles](#solid-principles)
- [Project Structure](#project-structure)
- [Class Diagrams](#class-diagrams)
- [Usage Examples](#usage-examples)
- [Building & Running](#building--running)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)

## Overview

This Library Management System is a fully-featured application that allows librarians to efficiently manage books, patrons, and lending processes. The system demonstrates professional software engineering practices with clean architecture, proper design patterns, and comprehensive logging.

**Key Statistics:**
- **Classes**: 25+ well-organized classes
- **Design Patterns**: 4 (Facade, Observer, Factory, Strategy)
- **Test Coverage**: Unit tests for core services
- **Lines of Code**: 2000+

## Core Features

### 1. **Book Management**
- ✅ Add, update, and remove books
- ✅ Track book copies and availability
- ✅ Search by title, author, or ISBN
- ✅ Book status tracking (Available, Partially Available, Checked Out, Reserved)
- ✅ Automatic status updates based on availability

### 2. **Patron Management**
- ✅ Add, update, and remove patrons
- ✅ Track patron borrowing history
- ✅ View active borrowings
- ✅ Patron status management (Active, Suspended, Inactive)

### 3. **Lending Process**
- ✅ Checkout books with automatic due date calculation (14 days)
- ✅ Return books with status tracking
- ✅ Renew books (extend due date by 14 days)
- ✅ Limit borrowing to 5 books per patron
- ✅ Overdue tracking and notifications

### 4. **Inventory Management**
- ✅ Real-time inventory tracking
- ✅ Available/borrowed copy management
- ✅ Automatic status updates

### 5. **Reservation System** (Optional Extension)
- ✅ Reserve books that are currently checked out
- ✅ Automatic expiry after 7 days
- ✅ Patron notification when book becomes available
- ✅ Cancel reservations

### 6. **Multi-Branch Support** (Optional Extension)
- ✅ Multiple library branch management
- ✅ Book transfers between branches
- ✅ Cross-branch availability checking
- ✅ Independent inventory per branch

### 7. **Recommendation System** (Optional Extension)
- ✅ Personalized recommendations based on borrowing history
- ✅ Similar books recommendation
- ✅ Popular books tracking
- ✅ Author and publisher preference analysis

### 8. **Notification System** (Observer Pattern)
- ✅ Email notifications
- ✅ SMS notifications
- ✅ Extensible for additional notifiers
- ✅ Events: Book available, Book overdue, Book returned

## Architecture & Design

### Layered Architecture

```
┌─────────────────────────────────┐
│   Presentation Layer            │ (Demo Application)
│   (LibraryDemo.java)            │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   Service/Business Logic Layer  │ (Service Implementations)
│   ├── BookService               │
│   ├── PatronService             │
│   ├── LendingService            │
│   ├── ReservationService        │
│   ├── RecommendationService     │
│   └── BranchService             │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   Domain Model Layer            │ (Entities)
│   ├── Book                      │
│   ├── Patron                    │
│   ├── BorrowRecord              │
│   ├── BookReservation           │
│   ├── Branch                    │
│   └── Enums                     │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   Observer/Event System         │ (Notifications)
│   ├── LibraryEventPublisher     │
│   ├── EmailNotifier             │
│   └── SMSNotifier               │
└─────────────────────────────────┘
```

## Design Patterns Used

### 1. **Facade Pattern**
**Location**: `com.library.Library` class

The `Library` class acts as a simplified interface to the complex subsystem of services. It provides a unified entry point for all operations, hiding the complexity of service coordination.

```java
// Instead of: new BookService(), new PatronService(), etc.
Library library = new Library();
library.addBook(book);
library.checkoutBook(isbn, patronId);
```

**Benefits**:
- Simplified client code
- Reduced coupling
- Single point of access

### 2. **Observer Pattern**
**Location**: `com.library.observer` package

Implements a publish-subscribe system for library events. When events occur (book becomes available, book overdue), all registered observers are notified.

```
LibraryEventPublisher (Subject)
    ├── Subscribe/Unsubscribe Observers
    └── Publish Events
        
LibraryEventObserver (Observer Interface)
    ├── EmailNotifier
    └── SMSNotifier
```

**Benefits**:
- Loose coupling between event producers and consumers
- Easy to add new notification types
- Event-driven architecture

### 3. **Strategy Pattern**
**Location**: `com.library.service.impl.RecommendationService`

Different recommendation strategies can be employed based on:
- Patron borrowing history
- Book popularity
- Author/Publisher preferences

**Benefits**:
- Flexible recommendation algorithms
- Easy to add new strategies
- Runtime algorithm selection

### 4. **Factory Pattern** (Implicit)
Services are created and managed in the `Library` class, acting as a factory for service instances.

## SOLID Principles

### 1. **Single Responsibility Principle (SRP)**
Each class has a single, well-defined responsibility:
- `BookService`: Book management only
- `PatronService`: Patron management only
- `LendingService`: Lending operations only
- `ReservationService`: Reservation management only

### 2. **Open/Closed Principle (OCP)**
- `LibraryEventObserver` interface allows adding new notification types without modifying existing code
- New service implementations can be added without changing the `Library` class

### 3. **Liskov Substitution Principle (LSP)**
- All service implementations properly implement their interfaces
- `EmailNotifier` and `SMSNotifier` are interchangeable `LibraryEventObserver` implementations

### 4. **Interface Segregation Principle (ISP)**
- Separate interfaces for each concern:
  - `IBookService` for book operations
  - `IPatronService` for patron operations
  - `ILendingService` for lending operations
  - `IReservationService` for reservation operations
  - `IRecommendationService` for recommendations
  - `IBranchService` for branch management

### 5. **Dependency Inversion Principle (DIP)**
- Services depend on abstractions (interfaces), not concrete implementations
- `Library` class depends on service interfaces, not implementations
- Event publishers are injected into services

```java
public class LendingService implements ILendingService {
    private final LibraryEventPublisher eventPublisher;
    
    public LendingService(LibraryEventPublisher eventPublisher) {
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }
}
```

## Project Structure

```
LibraryManagementSystemLLD/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── library/
│   │   │           ├── Library.java (Facade)
│   │   │           ├── model/
│   │   │           │   ├── Book.java
│   │   │           │   ├── BookStatus.java
│   │   │           │   ├── Patron.java
│   │   │           │   ├── PatronStatus.java
│   │   │           │   ├── BorrowRecord.java
│   │   │           │   ├── BorrowStatus.java
│   │   │           │   ├── BookReservation.java
│   │   │           │   ├── ReservationStatus.java
│   │   │           │   └── Branch.java
│   │   │           ├── service/
│   │   │           │   ├── IBookService.java
│   │   │           │   ├── IPatronService.java
│   │   │           │   ├── ILendingService.java
│   │   │           │   ├── IReservationService.java
│   │   │           │   ├── IRecommendationService.java
│   │   │           │   ├── IBranchService.java
│   │   │           │   └── impl/
│   │   │           │       ├── BookService.java
│   │   │           │       ├── PatronService.java
│   │   │           │       ├── LendingService.java
│   │   │           │       ├── ReservationService.java
│   │   │           │       ├── RecommendationService.java
│   │   │           │       └── BranchService.java
│   │   │           ├── observer/
│   │   │           │   ├── LibraryEventObserver.java
│   │   │           │   ├── LibraryEventPublisher.java
│   │   │           │   ├── EmailNotifier.java
│   │   │           │   └── SMSNotifier.java
│   │   │           └── demo/
│   │   │               └── LibraryDemo.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
│       └── java/
│           └── com/
│               └── library/
│                   └── service/
│                       └── impl/
│                           ├── BookServiceTest.java
│                           └── LendingServiceTest.java
├── pom.xml
└── README.md
```

## Class Diagrams

### 1. **Model Classes Diagram**

```
┌─────────────────────┐
│       Book          │
├─────────────────────┤
│ - isbn: String      │
│ - title: String     │
│ - author: String    │
│ - year: int         │
│ - publisher: String │
│ - status: BookStatus│
│ - totalCopies: int  │
│ - availableCopies:int
├─────────────────────┤
│ + isAvailable()     │
│ + incrementCopies() │
│ + decrementCopies() │
└─────────────────────┘

┌─────────────────────┐
│      Patron         │
├─────────────────────┤
│ - patronId: String  │
│ - name: String      │
│ - email: String     │
│ - status: Status    │
│ - borrowHistory     │
│ - currentBorrows    │
├─────────────────────┤
│ + borrowBook()      │
│ + returnBook()      │
│ + canBorrow()       │
└─────────────────────┘

┌──────────────────────────┐
│    BorrowRecord          │
├──────────────────────────┤
│ - recordId: String       │
│ - book: Book             │
│ - patron: Patron         │
│ - borrowDate: DateTime   │
│ - dueDate: DateTime      │
│ - returnDate: DateTime   │
│ - status: BorrowStatus   │
├──────────────────────────┤
│ + isOverdue()            │
│ + returnBook()           │
│ + renewBook()            │
└──────────────────────────┘

┌──────────────────────────┐
│   BookReservation        │
├──────────────────────────┤
│ - reservationId: String  │
│ - book: Book             │
│ - patron: Patron         │
│ - reservationDate: DT    │
│ - expiryDate: DateTime   │
│ - status: ResStatus      │
├──────────────────────────┤
│ + isExpired()            │
│ + fulfill()              │
│ + cancel()               │
└──────────────────────────┘

┌─────────────────────┐
│      Branch         │
├─────────────────────┤
│ - branchId: String  │
│ - name: String      │
│ - location: String  │
│ - inventory: Map    │
│ - patrons: Set      │
├─────────────────────┤
│ + addBook()         │
│ + removeBook()      │
│ + getBook()         │
│ + registerPatron()  │
└─────────────────────┘
```

### 2. **Service Architecture Diagram**

```
                    ┌──────────────┐
                    │  Library     │ (Facade)
                    │  (Main API)  │
                    └──────┬───────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
   ┌─────────┐        ┌─────────┐       ┌─────────┐
   │ Book    │        │ Patron  │       │Lending  │
   │Service  │        │Service  │       │Service  │
   └─────────┘        └─────────┘       └─────────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │ LibraryEvent     │
                  │ Publisher        │
                  │ (Observer)       │
                  └────────┬─────────┘
                           │
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
      ┌─────────┐    ┌─────────┐    ┌─────────┐
      │ Email   │    │ SMS     │    │ Custom  │
      │Notifier │    │Notifier │    │Notifier │
      └─────────┘    └─────────┘    └─────────┘
```

### 3. **Observer Pattern Diagram**

```
┌──────────────────────────────────┐
│  LibraryEventPublisher           │
├──────────────────────────────────┤
│ - observers: List<Observer>      │
├──────────────────────────────────┤
│ + subscribe(observer)            │
│ + unsubscribe(observer)          │
│ + publishBookAvailableEvent()    │
│ + publishBookOverdueEvent()      │
│ + publishBookReturnedEvent()     │
└──────────┬───────────────────────┘
           │
           │ notifies
           │
    ┌──────▼──────────────┐
    │ LibraryEventObserver│ (Interface)
    ├──────────────────────┤
    │ + onBookAvailable()  │
    │ + onBookOverdue()    │
    │ + onBookReturned()   │
    └──────┬───┬──────┬───┘
           │   │      │
    ┌──────▼─┐ │      └─────────────┐
    │ Email  │ │                    │
    │Notifier│ │                    ▼
    └────────┘ │            ┌────────────┐
               │            │Custom      │
          ┌────▼────┐       │Notifier    │
          │ SMS     │       └────────────┘
          │Notifier │
          └─────────┘
```

## Usage Examples

### 1. **Basic Book Management**

```java
Library library = new Library();

// Add books
Book book = new Book("ISBN001", "Clean Code", "Robert C. Martin", 
                     2008, "Prentice Hall", 5);
library.addBook(book);

// Search books
List<Book> results = library.searchBooksByAuthor("Robert C. Martin");

// Get available books
List<Book> available = library.getAvailableBooks();
```

### 2. **Patron Management**

```java
// Add patron
Patron patron = new Patron("P001", "John Doe", 
                          "john@example.com", "555-0001");
library.addPatron(patron);

// Update patron
patron.setEmail("newemail@example.com");
library.updatePatron(patron);

// Search patrons
List<Patron> results = library.searchPatronsByName("John");
```

### 3. **Checkout and Return**

```java
// Checkout book
BorrowRecord record = library.checkoutBook("ISBN001", "P001");
System.out.println("Due date: " + record.getDueDate());

// Renew book
library.renewBook(record.getRecordId());

// Return book
library.returnBook(record.getRecordId());
```

### 4. **Reservations**

```java
// Create reservation
BookReservation reservation = library.reserveBook("ISBN001", "P002");

// Get patron's reservations
List<BookReservation> reservations = 
    library.getReservationsForPatron("P002");

// Cancel reservation
library.cancelReservation(reservation.getReservationId());
```

### 5. **Recommendations**

```java
// Get personalized recommendations
List<Book> recommendations = 
    library.getRecommendationsForPatron("P001", 5);

// Get popular books
List<Book> popular = library.getPopularBooks(10);

// Get similar books
List<Book> similar = library.getSimilarBooks("ISBN001", 5);
```

### 6. **Multi-Branch Operations**

```java
// Create branches
Branch downtown = new Branch("B001", "Downtown Library", 
                            "123 Main St", "555-1000");
Branch uptown = new Branch("B002", "Uptown Library", 
                          "456 Oak Ave", "555-2000");
library.addBranch(downtown);
library.addBranch(uptown);

// Add books to branch
downtown.addBook(book);

// Transfer books between branches
library.transferBooks("B001", "B002", "ISBN001", 2);

// Check availability across branches
Map<String, Integer> availability = 
    library.getBookAvailabilityAcrossBranches("ISBN001");
```

### 7. **Notifications**

```java
// Subscribe to events
library.subscribeToEvents(new EmailNotifier());
library.subscribeToEvents(new SMSNotifier());

// When book is returned, all subscribers are notified
library.returnBook(recordId);
// Output: Email and SMS notifications sent automatically
```

## Building & Running

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Build

```bash
# Navigate to project directory
cd LibraryManagementSystemLLD

# Build the project
mvn clean package

# Run tests
mvn test
```

### Run Demo Application

```bash
# Run the demo
mvn exec:java -Dexec.mainClass="com.library.demo.LibraryDemo"

# Or using the JAR
java -jar target/library-management-system-1.0.0.jar
```

### Expected Output

```
========== BOOK MANAGEMENT ==========
✓ Added 4 books to the library
✓ Search by title 'Clean': Found 1 book(s)
✓ Search by author 'Martin': Found 2 book(s)
✓ Available books: 4

========== PATRON MANAGEMENT ==========
✓ Added 3 patrons to the library
✓ Search by name 'John': Found 1 patron(s)
✓ Total patrons: 3

========== LENDING OPERATIONS ==========
✓ Patron P001 checked out 'Clean Code'
  Due date: [14 days from now]
✓ Book renewed for patron P001
✓ Patron P002 returned 'Design Patterns'

[... more output ...]

========== LIBRARY STATISTICS ==========
Total Books: 4
Available Books: 3
Total Patrons: 3
Active Borrows: 2
Overdue Books: 0
Total Branches: 2
========================================
```

## Testing

### Unit Tests Included

- `BookServiceTest.java`: Tests for book operations
- `LendingServiceTest.java`: Tests for lending operations

### Run Tests

```bash
mvn test
```

### Test Coverage

- Book addition, removal, and search
- Patron management
- Checkout and return operations
- Book renewal
- Error handling and edge cases

### Sample Test

```java
@Test
public void testCheckoutBook() {
    BorrowRecord record = lendingService.checkoutBook(testBook, testPatron);
    assertNotNull(record);
    assertEquals(BorrowStatus.ACTIVE, record.getStatus());
    assertTrue(testPatron.getCurrentlyBorrowedBooks().contains(testBook));
}
```

## Logging

The system uses **SLF4J with Logback** for comprehensive logging:

- **Console Output**: Real-time operation logs
- **File Output**: Persistent logs in `logs/library.log`
- **Log Levels**: DEBUG for detailed operations, INFO for important events, WARN for potential issues

### Configured in `logback.xml`:
- Rolling file policy (10MB max per file, 30-day retention)
- Pattern: `[timestamp] [thread] [level] [class] - message`

## OOP Concepts Demonstrated

1. **Encapsulation**: Private fields with getters/setters
2. **Inheritance**: Base classes and abstract patterns
3. **Polymorphism**: Service interfaces with multiple implementations
4. **Abstraction**: Interface-based design
5. **Composition**: Services composed within Library facade

## SOLID Principles Summary

| Principle | Implementation | Location |
|-----------|---|---|
| SRP | Each service has single responsibility | `service/impl/*` |
| OCP | Observer pattern for extensibility | `observer/*` |
| LSP | Proper interface implementations | All service implementations |
| ISP | Segregated service interfaces | `service/*` |
| DIP | Dependency injection via constructors | `Library`, `LendingService` |

## Performance Considerations

- **HashMap** for O(1) book/patron lookups
- **HashSet** for efficient patron borrow tracking
- **Stream API** for efficient filtering and searching
- **Lazy initialization** of collections

## Security Features

- **Null checks** using `Objects.requireNonNull()`
- **Input validation** in all service methods
- **Immutable record IDs** (ISBN, Patron ID, Record ID)
- **Status validation** before operations

## Future Enhancements

1. **Database Integration**: Hibernate/JPA for persistence
2. **REST API**: Spring Boot REST endpoints
3. **Web UI**: Angular/React frontend
4. **Authentication**: User login and role-based access
5. **Fine Management**: Late fees calculation
6. **Advanced Analytics**: Borrowing trends, popular genres
7. **Integration**: External APIs for book information (Google Books API)
8. **Mobile App**: Native mobile application
9. **Distributed System**: Microservices architecture
10. **Real Notifications**: Email/SMS via external services

## Code Quality

- **Clean Code**: Follows naming conventions and best practices
- **Documentation**: Comprehensive Javadoc comments
- **Error Handling**: Proper exception handling and logging
- **Testing**: Unit tests for critical functionality
- **Maintainability**: Modular design for easy updates

## License

This project is provided as an educational resource for demonstrating OOP and SOLID principles in Java.

## Author

Created as a comprehensive Library Management System Low-Level Design (LLD) project demonstrating professional Java development practices.

---

## Quick Start Guide

### 1. Clone and Setup
```bash
git clone <repository-url>
cd LibraryManagementSystemLLD
mvn clean install
```

### 2. Run Demo
```bash
mvn exec:java -Dexec.mainClass="com.library.demo.LibraryDemo"
```

### 3. Integrate into Your Project
```java
Library library = new Library();

// Add books
library.addBook(new Book("ISBN001", "Book Title", "Author", 2024, "Publisher", 5));

// Add patrons
library.addPatron(new Patron("P001", "Name", "email@example.com", "555-0000"));

// Checkout
BorrowRecord record = library.checkoutBook("ISBN001", "P001");

// Return
library.returnBook(record.getRecordId());
```

## Support

For questions or issues, please refer to the inline documentation and Javadoc comments throughout the codebase.

---

**Last Updated**: December 2024  
**Version**: 1.0.0  
**Status**: Production Ready ✅


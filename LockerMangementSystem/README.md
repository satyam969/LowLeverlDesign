# Locker Management System - Low-Level Design Evaluation

## Introduction

This document evaluates the provided Low-Level Design (LLD) for a Locker Management System, assessing its adherence to functional requirements, design principles, and suitability for a fresher-level LLD interview at companies like Uber or for college placements.

## Functional Requirements (Recap)

The system should:
1.  Assign incoming packages to available lockers based on package size and locker availability.
2.  Manage lockers of different sizes (Small, Medium, Large, XL) and match packages to appropriate locker sizes.
3.  Generate unique, secure access codes.
4.  Track locker states (Available, Occupied, Out of Order) and manage state transitions.
5.  Notify recipients when packages are stored, providing locker number and access code.
6.  Handle package expiration (e.g., 7 days) and automatically release expired packages/lockers.
7.  Support searching for packages by tracking number, recipient information, or locker number.
8.  Support multiple warehouse locations, each with its own set of lockers.
9.  Allow recipients to retrieve packages using the access code.
10. Allow staff to mark lockers as Out of Order and restore them.

## Design Patterns Identified

The solution effectively utilizes several common design patterns:

*   **Singleton Pattern**: `LockerSystem`, `AccessCode`, and `ExpirationService` use the Singleton pattern to ensure a single instance of these classes throughout the application. This is appropriate for global access points and services that should not be duplicated.
*   **State Pattern**: The `Locker` class, in conjunction with the `LockerState` interface and its concrete implementations (`AvailableState`, `OccupiedState`, `MaintenanceState`), demonstrates a good use of the State pattern. This correctly encapsulates state-specific behavior and ensures proper state transitions.
*   **Strategy Pattern**: The `LockerStrategy` interface and `BestFitStrategy` implementation allow for flexible locker assignment policies. This makes the system extensible for adding new assignment strategies (e.g., `FirstFitStrategy`, `LeastUsedStrategy`) without modifying the core `LockerService`.
*   **Observer Pattern**: The `NotificationService` acts as a subject, managing `Observer` (abstract class) and its concrete implementations (`EmailObserver`, `SmsObserver`). This pattern effectively decouples the notification logic from the `LockerSystem` and allows for easy addition of new notification channels.

## Adherence to SOLID Principles

*   **Single Responsibility Principle (SRP)**: Generally well-adhered to. Each class has a distinct responsibility (e.g., `Locker` manages its state, `LockerService` handles business logic for lockers, `NotificationService` manages notifications). However, `LockerSystem` seems to be taking on a few too many responsibilities (initialization, assignment, vacation, maintenance, expiration checks). It could be further decomposed.
*   **Open/Closed Principle (OCP)**: Demonstrated by the Strategy pattern (`LockerStrategy`) for locker assignment and the Observer pattern (`Observer`) for notifications. New strategies or observers can be added without modifying existing code.
*   **Liskov Substitution Principle (LSP)**: Adhered to by the `LockerState` hierarchy. Subtypes (`AvailableState`, `OccupiedState`, `MaintenanceState`) can be substituted for `LockerState` without altering the correctness of the program.
*   **Interface Segregation Principle (ISP)**: The `LockerState` and `LockerStrategy` interfaces are granular, defining only necessary methods for their respective concerns. This is a good practice.
*   **Dependency Inversion Principle (DIP)**: Dependencies are generally on abstractions (interfaces/abstract classes) rather than concretions (e.g., `LockerService` depends on `LockerStrategy`, `LockerState`, not directly on `BestFitStrategy` or specific state implementations). This promotes loose coupling.

## Meeting Functional Requirements

1.  **Assign packages to lockers based on size and availability**: Met by `LockerService` using `LockerStrategy` (specifically `BestFitStrategy`) and `Locker` state management.
2.  **Manage different locker sizes**: `LockerSize` enum and `Locker` class handle this.
3.  **Generate unique, secure access codes**: `AccessCode` (Singleton) handles this.
4.  **Track locker states and transitions**: Implemented using the State pattern (`LockerState`, `AvailableState`, `OccupiedState`, `MaintenanceState`).
5.  **Notify recipients**: Handled by `NotificationService` and `Observer` pattern. However, the current `LockerSystem.assignLocker` method adds new observers for *each* package assignment using `EmailObserver` and `SmsObserver` constructor, and then removes them by calling `notificationService.removeObserver` with recipient contact info, which is incorrect. The `NotificationService`'s `removeObserver` method expects a `uid` generated during `addObserver`. This needs correction. Also, the `notifyObservers` in `LockerSystem` takes `String... uids` but is passed `pkg.getRecipientEmail()` and `pkg.getRecipientNumber()`, which are not UIDs. This is a critical bug in the notification logic.
6.  **Handle package expiration**: `ExpirationService` (Singleton) and `LockerService.checkForExpiredPackages` address this.
7.  **Search for packages**: `SearchService` and `PackageManager` are designed for this. `SearchService` current only supports searching by `packageId` to `lockerId`. Searching by recipient information (name/email) is not fully implemented in `SearchService` or `PackageManager` beyond storing the email/phone in the Package object itself.
8.  **Support multiple warehouse locations**: `LockerHub` handles this by managing lockers per area (`Map<String, Map<String, Locker>>`).
9.  **Allow recipients to retrieve packages using access code**: `Locker.getPackage(String code)` handles this.
10. **Allow staff to mark lockers Out of Order and restore**: `LockerService.maintainLocker` and `LockerState` transitions handle this.

## Areas for Improvement and Further Discussion

1.  **Notification System Refinement**: The current implementation of `NotificationService` and its usage in `LockerSystem.assignLocker` and `LockerService.checkForExpiredPackages` has a bug regarding observer management. The `addObserver` method returns a `uid`, which should be used for `removeObserver` and `notifyObservers`. The `LockerSystem` is incorrectly using recipient contact info as UIDs. The `NotificationService` should map UIDs to observers, and the `LockerSystem` (or a dedicated service) should store these UIDs in relation to packages to correctly manage notifications. A better approach for notifications would be to register `Package` or `Recipient` specific observers once, or have `NotificationService` manage subscriptions based on contact information directly if UIDs are deemed too complex for this level of detail.
2.  **`Package` Class Enhancement**: The `Package` class currently does not have a field for its size, but `BestFitStrategy` relies on `pkg.getSize().ordinal()`. This is a missing piece of data in the `Package` constructor and needs to be added for the system to function correctly for size-based assignment.
3.  **`LockerSystem` Coupling**: The `LockerSystem` acts as a central orchestrator, but it is too tightly coupled with `AccessCode` (instantiating it directly via `accessCode.getAccessCode()` even though it's a Singleton, and `accessCode` member is not initialized in `initializeSystem` or constructor). It also directly manipulates `NotificationService` observers. It should primarily delegate to its services rather than directly managing access codes or observer lifecycles. The `accessCode` field is not initialized anywhere. It should be injected or obtained via `AccessCode.getInstance()`.
4.  **`SearchService` Scope**: The `SearchService` currently only delegates to `PackageManager` for package ID to locker ID mapping. To meet the requirement of searching by recipient information, `PackageManager` or `SearchService` would need to maintain additional mappings (e.g., `recipientEmail` to `packageId` or `lockerId`).
5.  **Concurrency Considerations**: While `LockerSystem`, `AccessCode`, and `ExpirationService` use `ReentrantLock` for Singleton instantiation, the overall system's concurrency for operations like assigning/vacating lockers needs further consideration. If multiple threads try to assign lockers simultaneously to the same area, race conditions might occur when selecting a `bestFitLocker` or modifying `LockerHub`'s internal maps. Proper synchronization mechanisms (e.g., locks around `LockerHub` modifications or fine-grained locks per locker/area) would be required for a truly multi-threaded environment.
6.  **Error Handling and Robustness**: The system currently uses `System.out.println` for error messages (e.g., "Locker is already occupied."). In a production system, this should be replaced with proper logging mechanisms and potentially custom exceptions to handle various failure scenarios gracefully.
7.  **Configuration**: Hardcoded values (e.g., 7 days for package expiration) should ideally be configurable.
8.  **Persistence**: The current system is entirely in-memory. For a real-world scenario, data persistence (e.g., using a database) would be essential.
9.  **API Design**: The `LockerSystem` methods are somewhat verbose. A more streamlined API for clients (e.g., a facade) could be considered.

## Suitability for LLD Round (Fresher/College Placements)

### Strengths:

*   **Demonstrates understanding of Design Patterns**: Effective use of Singleton, State, Strategy, and Observer patterns. This is a significant plus for an LLD round.
*   **Clear Separation of Concerns**: Classes generally have well-defined roles, contributing to maintainability and readability.
*   **Extensibility**: The use of Strategy and Observer patterns makes the system extensible for future changes (e.g., new locker assignment algorithms, new notification channels).
*   **Handles Core Functional Requirements**: Most core requirements are addressed with a reasonable design.
*   **Multithreading awareness**: The use of `ReentrantLock` for Singletons shows an awareness of concurrent programming, even if full concurrency isn't deeply implemented across all services.

### Weaknesses/Areas for Interview Discussion:

*   **Notification Logic Bug**: The major bug in how observers are added/removed/notified in `LockerSystem` is a critical flaw that would need to be identified and corrected during an interview.
*   **Missing Package Size**: The `Package` class lacks the `size` attribute, which is fundamental to the `BestFitStrategy`.
*   **`LockerSystem` as a God Object**: While it orchestrates, it's a bit too involved in low-level details (like directly calling `accessCode.getAccessCode()` without initialization, and handling observer UIDs). This could lead to discussions about further decomposition or using a factory/builder for system initialization.
*   **Error Handling**: Relying solely on `System.out.println` is not production-ready. An interviewer would likely ask about exception handling, logging, and more robust error reporting.
*   **Concurrency Depth**: While Singletons are thread-safe, the broader concurrent access to `LockerHub` and `Locker` objects would be a natural follow-up question. The current implementation might not be fully thread-safe for high-concurrency operations.
*   **Testability**: The tight coupling in `LockerSystem` (especially with `AccessCode` not being injected properly) could make unit testing harder. Injecting dependencies consistently would improve this.
*   **`AccessCode` Initialization**: The `accessCode` field in `LockerSystem` is never initialized.

### Overall Verdict for Fresher LLD Round:

This solution presents a solid foundation. The candidate demonstrates a good grasp of OOP principles and design patterns, which is excellent for a fresher. However, the identified bugs (notification logic, missing package size, `AccessCode` initialization in `LockerSystem`) and areas for deeper discussion (concurrency, error handling, `LockerSystem` decomposition) would be key points of an interview. A candidate who can identify these issues themselves, propose solutions, and discuss trade-offs would certainly impress.

## Demo File (`Demo.java`)

To demonstrate the system, we will create a `Demo.java` file that will:

1.  Initialize the `LockerSystem` with necessary services.
2.  Add some lockers to different areas in `LockerHub`.
3.  Create and assign packages to lockers.
4.  Simulate package retrieval.
5.  Demonstrate placing a locker in maintenance and restoring it.
6.  Show the expiration check (though a real time-based check would require more advanced scheduling).
7.  Demonstrate search functionality.

(The content for Demo.java will follow this document.)

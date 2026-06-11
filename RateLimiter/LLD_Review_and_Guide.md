# Rate Limiter LLD Review & Uber Interview Guide

This document provides a detailed review of your Rate Limiter implementation, rates your use of design patterns, and suggests improvements to help you ace LLD interviews at top-tier companies like Uber.

## 1. Executive Summary & Rating

**Current Score: 4/10**

*   **Strengths:** You correctly identified and applied the **Strategy** and **Factory** patterns. This shows you understand how to decouple algorithms from the client.
*   **Weaknesses:** The implementation has critical functional bugs (NPE, Race Conditions), violates standard Java naming conventions, and lacks thread safety for concurrent environments.
*   **Verdict:** Good conceptual start, but the code would likely fail a senior LLD round due to lack of correctness, thread safety, and "production-readiness".

---

## 2. Detailed Code Review

### A. Critical Bugs (Must Fix)

1.  **`dynamicwindow.java` - NullPointerException**
    *   **Issue:** The `Queue<Long> requestTimestamps` is never initialized in the constructor.
    *   **Crash:** Calling `addRequest()` will throw `NullPointerException` immediately.
    *   **Fix:** `this.requestTimestamps = new LinkedList<>();` (or `ArrayDeque`) in the constructor.

2.  **`model/User.java` - Missing `hashCode` & `equals`**
    *   **Issue:** You use `User` as a key in `HashMap`. Since you didn't override `hashCode()` and `equals()`, the map uses "Identity Equality" (memory address).
    *   **Impact:** `new User("Alice", FREE)` is NOT equal to another `new User("Alice", FREE)`. In a real server, every request creates a new User object, so rate limiting would **never trigger** (every request looks like a new user).
    *   **Fix:** Implement `hashCode` and `equals` based on user ID or Name.

3.  **`RateLimiter.java` - Thread Safety (Race Condition)**
    *   **Issue 1:** `HashMap` is not thread-safe. Concurrent reads/writes will cause exceptions or corruption.
    *   **Issue 2:** The "Check-then-Act" pattern in `addRequest` is not atomic:
        ```java
        if (!hasAlreadyRequested(user)) {
             // Thread A and Thread B can both enter here
             RateLimiter.put(...) 
        }
        ```
    *   **Impact:** You might overwrite a user's limiter or corrupt the map.
    *   **Fix:** Use `ConcurrentHashMap` and `computeIfAbsent`.

### B. Code Style & Standards

1.  **Naming Conventions:**
    *   **Classes/Interfaces:** MUST be PascalCase (`Bucket`, `FixedWindow`, `RateLimiterStrategy`). Current: `bucket`, `fixedwindow`.
    *   **Enums:** `usertype` -> `UserType`.
    *   **Typos:** `ratelimiterstartegy` implies "Strategy". Check spelling carefully in interviews.

2.  **Encapsulation:**
    *   `public Map<User, ratelimiterstartegy> RateLimiter;` in `RateLimiter` class exposes internal state. Hiding this is crucial (make it `private`).
    *   Also, naming the field `RateLimiter` (same as class) is very confusing. Name it `userRateLimiters` or similar.

---

## 3. Design Pattern Analysis

### Strategy Pattern
*   **Usage:** **Good**. You defined an interface (`ratelimiterstartegy`) and implemented it (`bucket`, `fixedwindow`).
*   **Rating:** 8/10 for concept, 4/10 for naming/implementation.

### Factory Pattern
*   **Usage:** **Okay**. The `ratelimiterfactory` centralizes creation.
*   **Critique:** The `createRateLimiter` method signature is "polluted". You pass **all possible parameters** (`capacity`, `refillRate`, `maxRequests`, `windowSizeMs`) to one method.
*   **Better Approach:** Use a parameter object (e.g., `RateLimiterConfig`) or specific factory methods per type.

---

## 4. How to Ace the Uber LLD Round

Uber's LLD rounds focus on **Scalability**, **Concurrency**, and **Extensibility**.

### A. Core Requirements (You missed these)

1.  **Concurrency / Thread Safety:**
    *   Uber is highly concurrent. A `synchronized` block on the whole method (like in your strategies) is "okay" for a simple object, but a global lock or unsafe map is a fail.
    *   **Pro Tip:** Use `AtomicInteger` or `ReentrantLock` for fine-grained control if high performance is needed, though `synchronized` per user is acceptable for LLD if justified correctly.

2.  **Configuration Management:**
    *   Hardcoding `100` requests or `60000`ms in `RateLimiter.java` is a red flag.
    *   **Solution:** Pass a `RuleService` or `Config` object that determines limits based on User Tier, Location, Time, etc.

3.  **Cleanup (Memory Leak):**
    *   Your `Map<User, Strategy>` grows forever. If you have 1 million users, you run out of RAM.
    *   **Solution:** Mention a cleanup strategy (e.g., a background thread removing inactive users, or using a Cache with TTL like Guava Cache or Redis).

### B. Advanced Concepts (Bonus Points)

1.  **Distributed Rate Limiting:**
    *   Your solution works for **one server**. Uber has thousands.
    *   **Question:** "How does this work if I have 5 servers?"
    *   **Answer:** "We need a shared store like **Redis**."
    *   **Pattern:** Instead of in-memory `tokens`, use Redis `INCR` and `EXPIRE` (Fixed Window) or Lua Scripts (Token Bucket).

2.  **Observability:**
    *   Add logging or metrics. "If a user is blocked, we should log a metric `rate_limit_exceeded`."

---

## 5. Proposed Refactoring (The "Ace" Code)

Here is how a cleaned-up, interview-ready version of your core Logic would look:

### 1. The Strategy Interface
```java
public interface RateLimiterStrategy {
    boolean allowRequest();
}
```

### 2. Thread-Safe Factory & Manager
```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterManager {
    // 1. Thread-Safe Map
    private final Map<String, RateLimiterStrategy> userLimiters = new ConcurrentHashMap<>();
    
    // 2. Dependency Injection for Config
    private final RateLimiterFactory factory;

    public RateLimiterManager(RateLimiterFactory factory) {
        this.factory = factory;
    }

    public boolean allowRequest(User user) {
        // 3. Atomic "Get or Create"
        RateLimiterStrategy limiter = userLimiters.computeIfAbsent(user.getId(), k -> {
            // Determine config based on user tier
            return factory.create(user.getType()); 
        });

        return limiter.allowRequest();
    }
}
```

### 3. Correct Token Bucket (Thread-Safe)
```java
public class TokenBucketStrategy implements RateLimiterStrategy {
    private final long capacity;
    private final double refillRatePerMs;
    
    private double tokens;
    private long lastRefillTimestamp;

    public TokenBucketStrategy(long capacity, long refillRate, long timeUnitMs) {
        this.capacity = capacity;
        this.refillRatePerMs = (double) refillRate / timeUnitMs;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allowRequest() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;
        double tokensToAdd = elapsed * refillRatePerMs;
        
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}
```

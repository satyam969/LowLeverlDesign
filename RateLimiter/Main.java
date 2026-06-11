import enums.UserType;
import model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        User user1 = new User("Alice", UserType.FREE); // Fixed Window: 10 req / 60s
        User user2 = new User("Bob", UserType.PREMIUM); // Dynamic/Sliding: 100 req / 60s
        RateLimiter rateLimiter = new RateLimiter();

        System.out.println("=== Single Thread Test ===");
        System.out.println("User1 Request 1: " + rateLimiter.addRequest(user1)); // true
        System.out.println("User1 Request 2: " + rateLimiter.addRequest(user1)); // true

        System.out.println("\n=== Multi-thread Test (User1 - Limit 10) ===");
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Simulating 20 concurrent requests for User1. 
        // We expect ~10 to pass and ~10 to be rejected (depending on previous 2 requests).
        for (int i = 0; i < threadCount; i++) {
            final int requestId = i;
            executor.submit(() -> {
                boolean allowed = rateLimiter.addRequest(user1);
                if (allowed) {
                    System.out.println("Req " + requestId + ": ALLOWED");
                } else {
                    System.err.println("Req " + requestId + ": BLOCKED");
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Finished Multi-thread Test.");
    }
}
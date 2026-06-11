package strategy;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowStrategy implements RateLimiterStrategy {
    private final int windowSizeInMillis;
    private final int maxRequests;
    private final Queue<Long> requestTimestamps;

    public SlidingWindowStrategy(int windowSizeInMillis, int maxRequests) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.maxRequests = maxRequests;
        this.requestTimestamps = new LinkedList<>();
    }

    @Override
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() > windowSizeInMillis) {
            requestTimestamps.poll();
        }
        if (requestTimestamps.size() >= maxRequests) {
            return false;
        }
        requestTimestamps.offer(currentTime);
        return true;
    }
}

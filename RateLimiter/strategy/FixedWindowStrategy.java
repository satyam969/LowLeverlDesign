package strategy;

public class FixedWindowStrategy implements RateLimiterStrategy {
    private final int maxRequests;
    private final long windowSizeMs;
    private long windowStart;
    private int requestCount;

    public FixedWindowStrategy(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
        this.windowStart = System.currentTimeMillis();
        this.requestCount = 0;
    }

    @Override
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - windowStart >= windowSizeMs) {
            windowStart = currentTime;
            requestCount = 0;
        }
        if (requestCount >= maxRequests) {
            return false;
        }
        requestCount++;
        return true;
    }
}

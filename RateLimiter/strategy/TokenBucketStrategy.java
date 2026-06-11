package strategy;

public class TokenBucketStrategy implements RateLimiterStrategy {
    private final int capacity;
    private final int refillRate;
    private int tokens;
    private long lastRefillTimestamp;

    public TokenBucketStrategy(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTimestamp;
        int tokensToAdd = (int) (elapsedTime / 1000) * refillRate;
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = currentTime;
        }
    }
}

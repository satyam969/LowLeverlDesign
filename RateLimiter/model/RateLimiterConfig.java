package model;

public class RateLimiterConfig {
    private int maxRequests;
    private long windowSizeMs;
    private int capacity;
    private int refillRate;

    // Builder or Constructor. Using Builder for flexibility.
    private RateLimiterConfig(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.windowSizeMs = builder.windowSizeMs;
        this.capacity = builder.capacity;
        this.refillRate = builder.refillRate;
    }

    public int getMaxRequests() { return maxRequests; }
    public long getWindowSizeMs() { return windowSizeMs; }
    public int getCapacity() { return capacity; }
    public int getRefillRate() { return refillRate; }

    public static class Builder {
        private int maxRequests;
        private long windowSizeMs;
        private int capacity;
        private int refillRate;

        public Builder setMaxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder setWindowSizeMs(long windowSizeMs) {
            this.windowSizeMs = windowSizeMs;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setRefillRate(int refillRate) {
            this.refillRate = refillRate;
            return this;
        }

        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
}

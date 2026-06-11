package factory;

import enums.LimiterType;
import model.RateLimiterConfig;
import strategy.RateLimiterStrategy;
import strategy.FixedWindowStrategy;
import strategy.SlidingWindowStrategy;
import strategy.TokenBucketStrategy;

public class RateLimiterFactory {
    public static RateLimiterStrategy createRateLimiter(LimiterType type, RateLimiterConfig config) {
        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowStrategy(config.getMaxRequests(), config.getWindowSizeMs());
            case DYNAMIC_WINDOW:
                return new SlidingWindowStrategy((int) config.getWindowSizeMs(), config.getMaxRequests());
            case TOKEN_BUCKET:
                return new TokenBucketStrategy(config.getCapacity(), config.getRefillRate());
            default:
                throw new IllegalArgumentException("Invalid rate limiter type");
        }
    }
}

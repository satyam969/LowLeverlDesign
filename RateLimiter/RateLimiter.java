import enums.LimiterType;
import enums.UserType;
import factory.RateLimiterFactory;
import model.RateLimiterConfig;
import model.User;
import strategy.RateLimiterStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final Map<User, RateLimiterStrategy> userLimiters;

    public RateLimiter() {
        this.userLimiters = new ConcurrentHashMap<>();
    }

    public boolean addRequest(User user) {
        RateLimiterStrategy strategy = userLimiters.computeIfAbsent(user, k -> {
            if (user.getUserType() == UserType.FREE) {
                // Config Pattern: Using Builder to create configuration
                RateLimiterConfig config = new RateLimiterConfig.Builder()
                        .setMaxRequests(10)
                        .setWindowSizeMs(60000)
                        .build();
                return RateLimiterFactory.createRateLimiter(LimiterType.FIXED_WINDOW, config);
            } else {
                RateLimiterConfig config = new RateLimiterConfig.Builder()
                        .setMaxRequests(100)
                        .setWindowSizeMs(60000)
                        .build();
                return RateLimiterFactory.createRateLimiter(LimiterType.DYNAMIC_WINDOW, config);
            }
        });
        // "allowRequest" determines if the request is permitted by the strategy
        return strategy.allowRequest();
    }
}

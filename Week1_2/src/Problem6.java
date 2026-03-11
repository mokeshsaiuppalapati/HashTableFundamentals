import java.util.*;
import java.util.concurrent.*;

class Problem6 {

    class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate;
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRate) {
            this.tokens = maxTokens;
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        synchronized int getRemainingTokens() {
            refill();
            return tokens;
        }

        synchronized long getRetryAfterSeconds() {
            refill();
            if (tokens > 0) return 0;
            return (long) Math.ceil(1.0 / refillRate);
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;
            if (tokensToAdd >= 1) {
                tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
                lastRefillTime = now;
            }
        }
    }

    private ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private int limit = 1000;
    private double refillRate = limit / 3600.0;

    public String checkRateLimit(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId,
                k -> new TokenBucket(limit, refillRate));

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after "
                    + bucket.getRetryAfterSeconds() + "s)";
        }
    }

    public Map<String, Object> getRateLimitStatus(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId,
                k -> new TokenBucket(limit, refillRate));

        Map<String, Object> status = new HashMap<>();
        int remaining = bucket.getRemainingTokens();
        status.put("used", limit - remaining);
        status.put("limit", limit);
        status.put("reset", System.currentTimeMillis() / 1000 + (remaining == 0 ?
                bucket.getRetryAfterSeconds() : 0));
        return status;
    }

    public static void main(String[] args) {

        Problem6 limiter = new Problem6();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}
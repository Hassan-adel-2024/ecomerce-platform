package com.ecommerce.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage rate limiters per IP address and endpoint
 */
@Service
public class RateLimiterService {
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    /**
     * Get or create a rate limiter bucket for a given IP address and endpoint
     * @param key Unique key combining IP address, endpoint path, and rate limit config
     * @param capacity Maximum number of requests allowed
     * @param windowInSeconds Time window in seconds
     * @return Bucket instance for rate limiting
     */
    public Bucket resolveBucket(String key, int capacity, int windowInSeconds) {
        return cache.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(capacity)
                    .refillIntervally(capacity, Duration.ofSeconds(windowInSeconds))
                    .build();
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }
    
    /**
     * Check if a request should be allowed
     * @param ipAddress The client's IP address
     * @param endpointPath The endpoint path
     * @param capacity Maximum number of requests allowed
     * @param windowInSeconds Time window in seconds
     * @return true if request is allowed, false otherwise
     */
    public boolean tryConsume(String ipAddress, String endpointPath, int capacity, int windowInSeconds) {
        String key = String.format("%s:%s:%d:%d", ipAddress, endpointPath, capacity, windowInSeconds);
        Bucket bucket = resolveBucket(key, capacity, windowInSeconds);
        return bucket.tryConsume(1);
    }
}


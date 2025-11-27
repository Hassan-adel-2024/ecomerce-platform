package com.ecommerce.interceptor;

import com.ecommerce.annotation.RateLimit;
import com.ecommerce.exceptions.RateLimitExceededException;
import com.ecommerce.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to enforce rate limiting on endpoints annotated with @RateLimit
 */
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    
    private final RateLimiterService rateLimiterService;
    
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        
        // Only process if handler is a method (not a resource handler, etc.)
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        
        // Check if method has @RateLimit annotation
        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return true; // No rate limiting required
        }
        
        // Get client IP address
        String clientIp = getClientIpAddress(request);
        
        // Get endpoint path for creating unique rate limit buckets per endpoint
        String endpointPath = request.getRequestURI();
        
        // Check rate limit
        boolean allowed = rateLimiterService.tryConsume(
                clientIp,
                endpointPath,
                rateLimit.capacity(),
                rateLimit.windowInSeconds()
        );
        
        if (!allowed) {
            throw new RateLimitExceededException(
                    String.format("Rate limit exceeded: Maximum %d requests per %d seconds allowed", 
                            rateLimit.capacity(), rateLimit.windowInSeconds())
            );
        }
        
        return true;
    }
    
    /**
     * Extract client IP address from request, considering proxy headers
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // Take the first IP in the chain
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}


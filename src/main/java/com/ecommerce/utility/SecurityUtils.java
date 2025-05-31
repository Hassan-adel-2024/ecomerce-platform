package com.ecommerce.utility;

import com.ecommerce.security.CustomUserDetails;
import com.ecommerce.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@RequiredArgsConstructor
public class SecurityUtils {
    private final JwtService jwtService;
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }
    public Long getUserIdFromJwt(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Remove "Bearer " prefix
            return jwtService.extractId(jwtToken);
        }
        return null;
    }
}

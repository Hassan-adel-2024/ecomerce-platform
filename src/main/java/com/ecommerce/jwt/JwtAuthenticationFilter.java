package com.ecommerce.jwt;

import com.ecommerce.security.CustomUserDetails;
import com.ecommerce.service.CustomUserDetailsService;
import com.ecommerce.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;  // Handles JWT parsing/validation
    private final CustomUserDetailsService userDetailsService;  // Loads user-specific data

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract Authorization Header
        // Note: We expect format "Bearer <token>". Header absence doesn't fail the request
        // to allow other authentication mechanisms or public endpoints
        try {
            String authHeader = request.getHeader("Authorization");

            // 2. Skip JWT processing if:
            // - No Authorization header present
            // - Header doesn't start with "Bearer " prefix
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);  // Proceed without authentication
                return;
            }
            // 3. Extract raw JWT token (remove "Bearer " prefix)
            // Note: substring(7) is safe because we already verified the prefix
            final String jwtToken = authHeader.substring(7);

            try {
                // 4. Extract username from JWT (stateless verification)
                final String username = jwtService.extractUsername(jwtToken);

                // 5. Proceed only if:
                // - Token contains a username
                // - No existing authentication in Spring Security context (prevent re-authentication)
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 6. Load user details from database
                    // Security Note: This implements the "validate what you accept" principle by
                    // cross-checking JWT claims with database state
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

                    // 7. Validate token against user details (checks: username match, expiration, signature)
                    if (jwtService.validateToken(jwtToken, userDetails)) {

                        // 8. Create fully authenticated token (null credentials since JWT is credential)
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,  // No credentials needed for JWT
                                        userDetails.getAuthorities()  // Granted authorities
                                );

                        // 9. Add request metadata (IP, session ID, etc.) for auditing/security checks
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // 10. Set authentication in SecurityContext
                        // Note: This is stored in thread-local storage and used by subsequent filters
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (Exception e) {
                // 11. JWT Validation Failed - Return 401 Unauthorized
                // Security Note: Generic error message prevents information leakage about exact failure reason
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
                return;  // Stop filter chain for invalid tokens
            }

        } catch (ExpiredJwtException expiredJwtException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT");
        } catch (JwtException | IllegalArgumentException ex) {
            logger.warn("Invalid JWT token for request " + request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT" + ex.getMessage());
        }
//        catch (Exception ex) {
//            logger.error("JWT processing failed for request " + request.getRequestURI(), ex);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT processing error");
//        }

        // 12. Continue filter chain (with or without authentication)
        filterChain.doFilter(request, response);
    }
}

package com.ecommerce.service;

import com.ecommerce.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expirationTime;
    // This class is responsible for generating and validating JWT tokens.
    // It uses the io.jsonwebtoken library to create and parse JWT tokens.
    // The secret key is used to sign the tokens, and the expiration time is set to 1 hour.
    public String generateToken(CustomUserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());
        String userId  = userDetails.getUserId().toString();
        return createToken(claims, userDetails.getUsername(), userId);

    }

    // This method is used to create a JWT token with the specified claims, username, and userId.


    private String createToken(Map<String, Object> claims, String username , String userId) {
        return Jwts.builder().
                subject(username).
                claims(claims).
                id(userId).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis()+expirationTime)).
                signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).
                compact();
    }

    public <T> T extractClaim(String token , Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long extractId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getId));
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String getRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get("role").toString());
    }

    // validate method checks if the token is valid by checking if it has expired
    // and if the username in the token matches the username of the userDetails object.

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}

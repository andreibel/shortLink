package com.andreibel.shortlink.security.jqt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT operations such as token generation, extraction, and validation.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;


    //Authorization -> Bearer <token>
    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the HTTP request
     * @return the JWT token if present, otherwise null
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        // Implement JWT token generation logic here
        // This is a placeholder; actual implementation will depend on your JWT library
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                .signWith(key())
                .compact();
    }
    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param authToken the JWT token to validate
     * @return true if the token is valid, false otherwise
     * @throws RuntimeException if the token is invalid or parsing fails
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey)key())
                    .build().parseSignedClaims(authToken);
            return true; // If parsing is successful, the token is valid
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}

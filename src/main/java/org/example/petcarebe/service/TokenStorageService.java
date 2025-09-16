package org.example.petcarebe.service;

import org.springframework.stereotype.Service;

/**
 * Service for temporarily storing JWT tokens
 * This is useful for testing or development purposes
 */
@Service
public class TokenStorageService {
    
    private String currentToken;
    private String currentUsername;
    private String currentRole;
    private Long tokenExpirationTime;
    
    /**
     * Store the current JWT token
     */
    public void storeToken(String token, String username, String role) {
        this.currentToken = token;
        this.currentUsername = username;
        this.currentRole = role;
        // Token expires in 30 days (as configured in JwtUtil)
        this.tokenExpirationTime = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30);
    }
    
    /**
     * Get the current stored token
     */
    public String getCurrentToken() {
        if (isTokenExpired()) {
            clearToken();
            return null;
        }
        return currentToken;
    }
    
    /**
     * Get the Bearer token format
     */
    public String getBearerToken() {
        String token = getCurrentToken();
        return token != null ? "Bearer " + token : null;
    }
    
    /**
     * Get current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    /**
     * Get current user role
     */
    public String getCurrentRole() {
        return currentRole;
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired() {
        return tokenExpirationTime != null && System.currentTimeMillis() > tokenExpirationTime;
    }
    
    /**
     * Check if token exists and is valid
     */
    public boolean hasValidToken() {
        return currentToken != null && !isTokenExpired();
    }
    
    /**
     * Clear stored token
     */
    public void clearToken() {
        this.currentToken = null;
        this.currentUsername = null;
        this.currentRole = null;
        this.tokenExpirationTime = null;
    }
    
    /**
     * Get token info as string
     */
    public String getTokenInfo() {
        if (!hasValidToken()) {
            return "No valid token stored";
        }
        
        long remainingTime = tokenExpirationTime - System.currentTimeMillis();
        long remainingDays = remainingTime / (1000L * 60 * 60 * 24);
        
        return String.format("Token stored for user: %s, role: %s, expires in: %d days", 
                currentUsername, currentRole, remainingDays);
    }
}

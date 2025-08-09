package com.codyssey.api.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Extended authentication details that includes user ID
 * <p>
 * This class extends WebAuthenticationDetails to include additional
 * information like user ID that can be easily accessed in controllers.
 */
@Getter
public class JwtAuthenticationDetails extends WebAuthenticationDetails {

    private final String userId;

    /**
     * Constructor
     *
     * @param request the HTTP request
     * @param userId the user ID from JWT token
     */
    public JwtAuthenticationDetails(HttpServletRequest request, String userId) {
        super(request);
        this.userId = userId;
    }
}

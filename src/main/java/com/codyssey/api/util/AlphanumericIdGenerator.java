package com.codyssey.api.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Custom ID generator that produces 15-character case-sensitive alphanumeric IDs
 * <p>
 * Generates IDs using uppercase letters, lowercase letters, and digits.
 * Example: "A3bC9dE2fG1hI4j"
 */
public class AlphanumericIdGenerator implements IdentifierGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return generateAlphanumericId();
    }

    /**
     * Generates a 15-character alphanumeric ID
     * 
     * @return A random 15-character string containing letters (both cases) and digits
     */
    public static String generateAlphanumericId() {
        StringBuilder id = new StringBuilder(ID_LENGTH);
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            id.append(CHARACTERS.charAt(randomIndex));
        }
        
        return id.toString();
    }

    /**
     * Validates if a string is a valid 15-character alphanumeric ID
     * 
     * @param id The ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidId(String id) {
        if (id == null || id.length() != ID_LENGTH) {
            return false;
        }
        
        return id.matches("[A-Za-z0-9]{" + ID_LENGTH + "}");
    }
}
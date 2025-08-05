package com.codyssey.api.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Custom ID generator for Label entities
 * <p>
 * Generates IDs with prefix "LBL-" followed by 6 random characters.
 * Example: "LBL-A3bC9d"
 */
public class LabelIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "LBL-";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SUFFIX_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return generateLabelId();
    }

    /**
     * Generates a label ID with format LBL-XXXXXX
     * 
     * @return A random label ID
     */
    public static String generateLabelId() {
        StringBuilder id = new StringBuilder(PREFIX);
        
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            id.append(CHARACTERS.charAt(randomIndex));
        }
        
        return id.toString();
    }

    /**
     * Validates if a string is a valid label ID
     * 
     * @param id The ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidLabelId(String id) {
        if (id == null || id.length() != PREFIX.length() + SUFFIX_LENGTH) {
            return false;
        }
        
        return id.matches("LBL-[A-Za-z0-9]{" + SUFFIX_LENGTH + "}");
    }
}
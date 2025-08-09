package com.codyssey.api.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Custom ID generator for MCQCategory entities
 * Generates IDs in the format: MQC-xxxxxx (where x are random digits)
 */
public class MCQCategoryIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "MQC-";
    private static final SecureRandom random = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String id;
        do {
            // Generate a 6-digit random number
            int randomNumber = 100000 + random.nextInt(900000);
            id = PREFIX + randomNumber;
            
            // Check if this ID already exists
            String query = "SELECT 1 FROM MCQCategory WHERE id = :id";
            Object existingEntity = session.createQuery(query).setParameter("id", id).uniqueResult();
            if (existingEntity == null) {
                break;
            }
        } while (true);
        
        return id;
    }
}

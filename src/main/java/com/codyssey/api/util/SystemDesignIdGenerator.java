package com.codyssey.api.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Custom ID generator for SystemDesign entities
 * <p>
 * Generates unique IDs in the format: SYS-XXXXXXXX
 * where X represents alphanumeric characters
 */
public class SystemDesignIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "SYS-";
    private static final int ID_LENGTH = 8;
    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String randomPart = IntStream.range(0, ID_LENGTH)
                .map(i -> RANDOM.nextInt(ALLOWED_CHARS.length()))
                .mapToObj(ALLOWED_CHARS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());

        return PREFIX + randomPart;
    }
}

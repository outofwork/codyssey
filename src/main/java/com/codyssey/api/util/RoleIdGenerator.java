package com.codyssey.api.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Custom ID generator for Role entities
 * <p>
 * Generates IDs in format: ROL-100001, ROL-100002, etc.
 * Ensures uniqueness by querying the database for the next available number.
 */
public class RoleIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "ROL-";
    private static final int STARTING_NUMBER = 100001;

    /**
     * Validates if a string is a valid Role ID
     *
     * @param id The ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRoleId(String id) {
        if (id == null) {
            return false;
        }
        return id.matches("^ROL-\\d{6}$");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Connection connection;
        try {
            connection = session.getJdbcConnectionAccess().obtainConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to obtain database connection", e);
        }

        try {
            // Get the maximum existing number for roles
            String sql = "SELECT MAX(CAST(SUBSTRING(id, 5) AS INTEGER)) FROM roles WHERE id LIKE 'ROL-%'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int nextNumber = STARTING_NUMBER;
            if (resultSet.next()) {
                Integer maxNumber = (Integer) resultSet.getObject(1);
                if (maxNumber != null) {
                    nextNumber = maxNumber + 1;
                }
            }

            resultSet.close();
            statement.close();

            return PREFIX + String.format("%06d", nextNumber);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to generate Role ID", e);
        } finally {
            try {
                session.getJdbcConnectionAccess().releaseConnection(connection);
            } catch (SQLException e) {
                // Log error but don't fail the transaction
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }
}
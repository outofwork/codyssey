-- Migration to update ID columns from BIGINT to VARCHAR(15) for alphanumeric IDs
-- This migration will:
-- 1. Create new tables with VARCHAR(15) IDs
-- 2. Migrate existing data with generated alphanumeric IDs
-- 3. Drop old tables
-- 4. Rename new tables

-- Create temporary function to generate 15-character alphanumeric IDs
CREATE OR REPLACE FUNCTION generate_alphanumeric_id() 
RETURNS VARCHAR(15) AS $$
DECLARE
    chars VARCHAR(62) := 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    result VARCHAR(15) := '';
    i INTEGER;
BEGIN
    FOR i IN 1..15 LOOP
        result := result || substr(chars, floor(random() * 62 + 1)::int, 1);
    END LOOP;
    RETURN result;
END;
$$ LANGUAGE plpgsql;

-- Step 1: Create new roles table with VARCHAR(15) ID
CREATE TABLE roles_new (
    id VARCHAR(15) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Step 2: Create new users table with VARCHAR(15) ID
CREATE TABLE users_new (
    id VARCHAR(15) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Step 3: Create new user_roles table with VARCHAR(15) IDs
CREATE TABLE user_roles_new (
    user_id VARCHAR(15) NOT NULL,
    role_id VARCHAR(15) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users_new(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles_new(id) ON DELETE CASCADE
);

-- Step 4: Migrate existing roles data
INSERT INTO roles_new (id, name, description, created_at, updated_at, version, deleted)
SELECT 
    generate_alphanumeric_id(),
    name,
    description,
    COALESCE(created_at, CURRENT_TIMESTAMP),
    COALESCE(updated_at, CURRENT_TIMESTAMP),
    COALESCE(version, 0),
    COALESCE(deleted, FALSE)
FROM roles
ORDER BY id;

-- Step 5: Migrate existing users data
INSERT INTO users_new (id, username, email, password, first_name, last_name, enabled, created_at, updated_at, version, deleted)
SELECT 
    generate_alphanumeric_id(),
    username,
    email,
    password,
    first_name,
    last_name,
    COALESCE(enabled, TRUE),
    COALESCE(created_at, CURRENT_TIMESTAMP),
    COALESCE(updated_at, CURRENT_TIMESTAMP),
    COALESCE(version, 0),
    COALESCE(deleted, FALSE)
FROM users
ORDER BY id;

-- Step 6: Migrate user_roles relationship data
-- Create a mapping table for old to new IDs
CREATE TEMP TABLE user_id_mapping AS
SELECT 
    row_number() OVER (ORDER BY u_old.id) as old_id,
    u_new.id as new_id
FROM users u_old
JOIN users_new u_new ON u_old.username = u_new.username;

CREATE TEMP TABLE role_id_mapping AS
SELECT 
    row_number() OVER (ORDER BY r_old.id) as old_id,
    r_new.id as new_id
FROM roles r_old
JOIN roles_new r_new ON r_old.name = r_new.name;

-- Insert user_roles with new IDs
INSERT INTO user_roles_new (user_id, role_id)
SELECT 
    u_map.new_id,
    r_map.new_id
FROM user_roles ur_old
JOIN user_id_mapping u_map ON ur_old.user_id = u_map.old_id
JOIN role_id_mapping r_map ON ur_old.role_id = r_map.old_id;

-- Step 7: Drop old tables
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Step 8: Rename new tables to original names
ALTER TABLE roles_new RENAME TO roles;
ALTER TABLE users_new RENAME TO users;
ALTER TABLE user_roles_new RENAME TO user_roles;

-- Step 9: Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_enabled ON users(enabled);
CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

-- Step 10: Drop the temporary function
DROP FUNCTION generate_alphanumeric_id();

-- Step 11: Update sequences (if any auto-increment was used, remove them)
-- Since we're now using custom alphanumeric IDs, we don't need sequences

-- Add comment for documentation
COMMENT ON COLUMN users.id IS '15-character alphanumeric unique identifier';
COMMENT ON COLUMN roles.id IS '15-character alphanumeric unique identifier';
COMMENT ON COLUMN user_roles.user_id IS 'References users.id';
COMMENT ON COLUMN user_roles.role_id IS 'References roles.id';
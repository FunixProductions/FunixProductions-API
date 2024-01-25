-- Add country_name column with default value
ALTER TABLE api_users
    ADD COLUMN country_name VARCHAR(255) NOT NULL DEFAULT 'France';

-- Add country_code column with default value
ALTER TABLE api_users
    ADD COLUMN country_code INT NOT NULL DEFAULT 250;

-- Add country_code_2_chars column with default value
ALTER TABLE api_users
    ADD COLUMN country_code_2_chars VARCHAR(2) NOT NULL DEFAULT 'FR';

-- Add country_code_3_chars column with default value
ALTER TABLE api_users
    ADD COLUMN country_code_3_chars VARCHAR(3) NOT NULL DEFAULT 'FRA';

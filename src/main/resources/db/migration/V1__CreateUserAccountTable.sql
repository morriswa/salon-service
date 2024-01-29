
-- Creating a new table named user_account
CREATE TABLE user_account(
    -- The name, datatype, and rules for each column in table
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    -- For every new record, this column will be populated with the current date/time
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
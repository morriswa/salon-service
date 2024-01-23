
-- Creating a new table named user_profile
CREATE TABLE user_profile(
    -- The name, datatype, and rules for each column in table
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

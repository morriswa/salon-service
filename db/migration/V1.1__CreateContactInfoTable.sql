DELIMITER //

-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-01-29
-- PURPOSE: Creates a new table named contact_info to store all app user's contact information
CREATE TABLE contact_info(
    -- The name, datatype, and rules for each column in table
    user_id BIGINT PRIMARY KEY,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    phone_num CHAR(10) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    addr_one VARCHAR(50) NOT NULL,
    addr_two VARCHAR(50),
    city VARCHAR(50) NOT NULL,
    state_code CHAR(2) NOT NULL,
    zip_code CHAR(10) NOT NULL,
    contact_pref CHAR(5) NOT NULL,
    -- For every new record, this column will be populated with the current date/time
    -- Every insert/update will also modify this field with the timestamp
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

//
DELIMITER ;
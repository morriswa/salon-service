/**
  COPY OF db/migration files modified for compatibility with test runner
 */


-- AUTHOR: William A. Morris, Kevin Rivers
-- CREATED: 2024-01-21
-- PURPOSE: Creates a new table named user_account to store important user authentication information
CREATE TABLE user_account(
    -- The name, datatype, and rules for each column in table
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    -- For every new record, this column will be populated with the current date/time
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-01-29
-- PURPOSE: Creates a new table named contact_info to store all app user's contact information
CREATE TABLE contact_info(
    -- The name, datatype, and rules for each column in table
    user_id BIGINT PRIMARY KEY,
    -- all entries should be associated with an account in user_account table
    FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    phone_num CHAR(10) NOT NULL UNIQUE,
    pronouns CHAR(1) NOT NULL DEFAULT 'T',
    CONSTRAINT pronoun_values CHECK ( pronouns in ('H', 'S', 'T', 'N') ),
    email VARCHAR(100) NOT NULL UNIQUE,
    addr_one VARCHAR(50) NOT NULL,
    addr_two VARCHAR(50),
    city VARCHAR(50) NOT NULL,
    state_code CHAR(2) NOT NULL,
    zip_code CHAR(5) NOT NULL,
    contact_pref CHAR(5) NOT NULL,
    CONSTRAINT contact_pref_values CHECK ( contact_pref IN ('EMAIL', 'PCALL', 'PTEXT') ),
    -- For every new record, this column will be populated with the current date/time
    -- Every insert/update will also modify this field with the timestamp
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX name_search_idx ON contact_info(first_name, last_name);



-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-03-04
-- Create tables to store employee specific information
CREATE TABLE employee(
    employee_id BIGINT PRIMARY KEY,
    -- all entries should be associated with an account in user_account table
    FOREIGN KEY (employee_id) REFERENCES user_account (user_id) ON DELETE CASCADE,
    start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bio VARCHAR(256)
);


-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-03-04
-- Create tables to store client specific information
create table client(
    client_id BIGINT PRIMARY KEY,
    -- all entries should be associated with an account in user_account table
    FOREIGN KEY (client_id) REFERENCES user_account (user_id) ON DELETE CASCADE,
    birthday DATE
);


-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-02-07
-- PURPOSE: Creates a new table named provided_service to store all offered services
CREATE TABLE provided_service(
    -- The name, datatype, and rules for each column in table
    service_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    -- all entries should be associated with an employee in the table
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE,
    provided_service_name VARCHAR(128) NOT NULL,
    -- FULLTEXT provided_service_name_idx (provided_service_name),
    default_cost DECIMAL(5, 2) NOT NULL,
    CONSTRAINT default_cost_range CHECK ( default_cost BETWEEN 0.01 AND 999.99 ),
    default_length SMALLINT UNSIGNED NOT NULL DEFAULT 2,
    CONSTRAINT default_length_range CHECK ( default_length BETWEEN 1 AND 32 ),
    offered CHAR(1) NOT NULL DEFAULT 'Y',
    CONSTRAINT offered_values CHECK ( offered IN ('Y', 'N') )
);

-- needed for testing with h2
CREATE INDEX provided_service_name_idx ON provided_service(provided_service_name);


-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-02-07
-- PURPOSE: Creates a new table to store appointments
CREATE TABLE appointment(
    -- The name, datatype, and rules for each column in table
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    -- all entries should be associated with a client in the table
    FOREIGN KEY (client_id) REFERENCES client (client_id) ON DELETE CASCADE,
    appointment_time TIMESTAMP NOT NULL,
    service_id BIGINT NOT NULL,
    -- all entries should be associated with a service in the provided_service table
    FOREIGN KEY (service_id) REFERENCES provided_service (service_id) ON DELETE CASCADE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_due TIMESTAMP NOT NULL,
    actual_amount DECIMAL(5,2) NOT NULL,
    tip_amount DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT tip_amount_range CHECK ( tip_amount BETWEEN 0.00 AND 999.99 ),
    status CHAR(6) NOT NULL DEFAULT 'OKGOOD',
    CONSTRAINT status_values CHECK ( status IN ('OKGOOD', 'CANCEL', 'MISSED') ),
    length SMALLINT UNSIGNED NOT NULL,
    CONSTRAINT length_range CHECK ( length BETWEEN 1 AND 32 )
);


-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-02-23
-- Create a table to store ids of all provided service related content in AWS S3
CREATE TABLE provided_service_content(
    content_id VARCHAR(36) PRIMARY KEY,
    service_id BIGINT NOT NULL,
    -- all entries should be associated with a service in the provided_service table
    FOREIGN KEY (service_id) REFERENCES provided_service (service_id) ON DELETE CASCADE
);


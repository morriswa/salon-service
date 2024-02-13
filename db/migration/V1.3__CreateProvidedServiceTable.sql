DELIMITER //

-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-02-07
-- PURPOSE: Creates a new table named provided_service to store all offered services
CREATE TABLE provided_service(
    -- The name, datatype, and rules for each column in table
    service_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    provided_service_name VARCHAR(128) NOT NULL,
    default_cost DECIMAL(5, 2) NOT NULL,
    CONSTRAINT default_cost_range CHECK ( default_cost BETWEEN 0.01 AND 999.99),
    default_length SMALLINT UNSIGNED NOT NULL DEFAULT 60,
    CONSTRAINT default_length_range CHECK ( default_length BETWEEN 1 AND 32 )
);

//
DELIMITER ;
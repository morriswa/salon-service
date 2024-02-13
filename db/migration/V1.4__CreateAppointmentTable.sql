DELIMITER //

-- AUTHOR: Makenna Loewenherz, Kevin Rivers, and William Morris
-- CREATED: 2024-02-07
-- PURPOSE: Creates a new table to store appointments
CREATE TABLE appointment(
    -- The name, datatype, and rules for each column in table
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    service_id BIGINT NOT NULL,
    reminder_preference CHAR(5),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_due TIMESTAMP NOT NULL,
    discount_applied DECIMAL(3,2),
    status CHAR(6) NOT NULL DEFAULT 'OKGOOD',
    CONSTRAINT status_values CHECK ( status IN ('OKGOOD', 'CANCEL', 'MISSED')),
    length SMALLINT UNSIGNED NOT NULL,
    CONSTRAINT length_range CHECK ( length BETWEEN 1 AND 32 )
);

//
DELIMITER ;
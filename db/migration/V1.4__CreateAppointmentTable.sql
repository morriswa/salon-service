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
    discount_applied decimal(3,2)
);

//
DELIMITER ;
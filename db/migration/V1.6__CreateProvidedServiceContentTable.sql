DELIMITER //
-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-02-23

-- Create a table to store ids of all provided service related content in AWS S3
CREATE TABLE provided_service_content(
    content_id VARCHAR(36) PRIMARY KEY,
    service_id BIGINT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES provided_service (service_id)
);

-- Add foreign key constraints to all existing tables to maintain referential integrity
ALTER TABLE contact_info
ADD FOREIGN KEY (user_id) REFERENCES user_account (user_id);

ALTER TABLE provided_service
ADD FOREIGN KEY (employee_id) REFERENCES user_account (user_id);

ALTER TABLE appointment
ADD FOREIGN KEY (client_id) REFERENCES user_account (user_id),
ADD FOREIGN KEY (employee_id) REFERENCES user_account (user_id),
ADD FOREIGN KEY (service_id) REFERENCES provided_service (service_id);

//
DELIMITER ;
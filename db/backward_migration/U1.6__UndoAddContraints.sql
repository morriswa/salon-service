DELIMITER //

DROP TABLE IF EXISTS provided_service_content;

-- Add foreign key constraints to all existing tables to maintain referential integrity
ALTER TABLE contact_info
DROP FOREIGN KEY contact_info_ibfk_1;

ALTER TABLE provided_service
DROP FOREIGN KEY provided_service_ibfk_1;

ALTER TABLE appointment
DROP FOREIGN KEY appointment_ibfk_1,
DROP FOREIGN KEY appointment_ibfk_2,
DROP FOREIGN KEY appointment_ibfk_3;

//
DELIMITER ;
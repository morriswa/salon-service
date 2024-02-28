DELIMITER //
-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-02-27

DROP TRIGGER IF EXISTS on_promote_update_timestamp_trigger;

ALTER TABLE user_account
DROP CONSTRAINT account_type_values,
DROP COLUMN account_type,
DROP COLUMN promoter,
DROP COLUMN promote_date,
ADD COLUMN access_client CHAR(1) NOT NULL DEFAULT 'N',
ADD COLUMN access_employee CHAR(1) NOT NULL DEFAULT 'N',
ADD COLUMN access_admin CHAR(1) NOT NULL DEFAULT 'N',
ADD CONSTRAINT access_control_values CHECK (
        access_client IN ( 'Y', 'N' )
    AND access_employee IN ( 'Y', 'N' )
    AND access_admin IN ( 'Y', 'N' ));

//
DELIMITER ;
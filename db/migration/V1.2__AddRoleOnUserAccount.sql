DELIMITER //

-- AUTHOR: William A. Morris
-- CREATED: 2024-01-30
-- PURPOSE: As role (client/employee/admin) is directly related to 
-- what a user has access to, and because it is simpler than maintaining
-- separate tables, I have decided to add role column to user_account

ALTER TABLE user_account
ADD account_type CHAR(3) NOT NULL DEFAULT 'USR',
ADD CONSTRAINT account_type_values CHECK ( account_type IN ('USR', 'CLT', 'EMP', 'ADM')),
ADD promoter BIGINT,
ADD promote_date TIMESTAMP;

-- Rather than managing promote_date directly, I have added update trigger
CREATE TRIGGER on_promote_update_timestamp_trigger
BEFORE UPDATE ON user_account FOR EACH ROW BEGIN
    IF OLD.account_type <> NEW.account_type THEN
        -- update the promote date with the current timestamp
        SET NEW.promote_date = CURRENT_TIMESTAMP;
    -- if account_type column will not change, do nothing
    END IF;
END;

//
DELIMITER ;
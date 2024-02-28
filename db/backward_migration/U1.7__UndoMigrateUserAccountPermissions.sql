DELIMITER //
-- AUTHOR: William A. Morris
-- DATE CREATED: 2024-02-27

DROP TRIGGER IF EXISTS on_promote_update_timestamp_trigger;

ALTER TABLE user_account
    ADD account_type CHAR(3) NOT NULL DEFAULT 'USR',
    ADD CONSTRAINT account_type_values CHECK ( account_type IN ('USR', 'CLT', 'EMP', 'ADM') ),
    ADD promoter BIGINT,
    ADD promote_date TIMESTAMP,
DROP COLUMN access_client,
DROP COLUMN access_employee,
DROP COLUMN access_admin,
DROP CONSTRAINT access_control_values;

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
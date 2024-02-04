
ALTER TABLE user_account
DROP COLUMN account_type,
DROP COLUMN promoter,
DROP COLUMN promote_date;

DROP TRIGGER IF EXISTS promoteTimestampTrigger;

DELETE FROM flyway_schema_history WHERE VERSION = '1.2';

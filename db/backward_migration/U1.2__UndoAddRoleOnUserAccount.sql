
ALTER TABLE user_account
DROP COLUMN account_type,
DROP COLUMN promoter,
DROP COLUMN promote_date;

DROP TRIGGER IF EXISTS on_promote_update_timestamp_trigger;

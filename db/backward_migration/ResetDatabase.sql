
-- Completely resets all migrations, returns database to newly created state
-- Run this script if unable to troubleshoot migrations

-- Drop all tables created during migrations
drop table if exists user_account;
drop table if exists contact_info;
drop table if exists provided_service;
drop table if exists appointment;

-- Drop all triggers
drop trigger if exists on_promote_update_timestamp_trigger;

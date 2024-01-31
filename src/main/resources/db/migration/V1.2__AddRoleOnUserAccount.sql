-- AUTHOR: William A. Morris
-- PURPOSE: As role (client/employee/admin) is directly related to 
-- what a user has access to, and because it is simpler than maintaining
-- separate tables, I have decided to add role column to user_account

ALTER TABLE user_account
ADD account_type CHAR(3) NOT NULL DEFAULT 'USR';
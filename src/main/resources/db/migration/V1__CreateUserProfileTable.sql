-- This is a comment

-- Creating a new table for user_profile
CREATE TABLE user_profile(
    
 -- The attrs for each column in table
  user_id BIGINT UNIQUE NOT NULL AUTO_INCREMENT PRIMARY KEY, -- COL 1
  username VARCHAR(64) UNIQUE NOT NULL, -- COL 2
  password VARCHAR(128) NOT NULL -- COL 3
);



-- creates service user
create user 'dev_dynasty_service' identified by 'password';

-- create the database
create database dev_dynasty_salon;

-- grant all of the required permissions
grant   select, update, insert, delete, -- all necessary CRUD operations performed by the service
        create, alter, trigger, drop -- all operations needed during migrations
        -- on created database to created user
        on dev_dynasty_salon.* to dev_dynasty_service;


create table user_profile(
    user_id bigint unique not null auto_increment primary key,
    username varchar(64) unique not null,
    password varchar(256) not null
);
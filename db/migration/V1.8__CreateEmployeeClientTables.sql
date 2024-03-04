DELIMITER //

ALTER TABLE user_account
    DROP COLUMN access_client,
    DROP COLUMN access_employee,
    DROP COLUMN access_admin,
    DROP CONSTRAINT access_control_values;

create table employee(
    employee_id bigint primary key,
    FOREIGN KEY (employee_id) references user_account (user_id),
    bio varchar(256)
);

create table client(
    client_id bigint primary key,
    FOREIGN KEY (client_id) references user_account(user_id),
    birthday date
);

ALTER TABLE provided_service
    DROP FOREIGN KEY provided_service_ibfk_1,
    ADD FOREIGN KEY (employee_id) REFERENCES employee (employee_id);


ALTER TABLE appointment
    DROP FOREIGN KEY appointment_ibfk_1,
    ADD FOREIGN KEY (client_id) REFERENCES client (client_id),
    DROP FOREIGN KEY appointment_ibfk_2,
    DROP COLUMN employee_id;

//
DELIMITER ;
/**
    create testing data before each test
 */

insert into user_account (user_id, username, password, date_created)
values
    (1, 'test_nuser_1', 'password', CURRENT_TIMESTAMP),
    (2, 'test_nuser_2', 'password', CURRENT_TIMESTAMP),
    (3, 'test_nuser_3', 'password', CURRENT_TIMESTAMP),
    (4, 'test_nuser_4', 'password', CURRENT_TIMESTAMP),
    (5, 'test_nuser_5', 'password', CURRENT_TIMESTAMP),
    (31, 'test_user_1', 'password', CURRENT_TIMESTAMP),
    (32, 'test_user_2', 'password', CURRENT_TIMESTAMP),
    (33, 'test_user_3', 'password', CURRENT_TIMESTAMP),
    (34, 'test_user_4', 'password', CURRENT_TIMESTAMP),
    (35, 'test_user_5', 'password', CURRENT_TIMESTAMP),
    (11, 'test_client_1', 'password', CURRENT_TIMESTAMP),
    (12, 'test_client_2', 'password', CURRENT_TIMESTAMP),
    (13, 'test_client_3', 'password', CURRENT_TIMESTAMP),
    (14, 'test_client_4', 'password', CURRENT_TIMESTAMP),
    (15, 'test_client_5', 'password', CURRENT_TIMESTAMP),
    (21, 'test_employee_1', 'password', CURRENT_TIMESTAMP),
    (22, 'test_employee_2', 'password', CURRENT_TIMESTAMP),
    (23, 'test_employee_3', 'password', CURRENT_TIMESTAMP),
    (24, 'test_employee_4', 'password', CURRENT_TIMESTAMP),
    (25, 'test_employee_5', 'password', CURRENT_TIMESTAMP);

insert into contact_info (user_id, first_name, last_name, pronouns, phone_num, email, addr_one, addr_two, city, state_code, zip_code, contact_pref, last_updated)
values
    (31, 'Test User', 'One', 'T',
     '1111111131', 'user1@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (32, 'Test 2 User', 'Two', 'H',
     '1111111132', 'user2@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (33, 'Test 3 User', 'Three', 'S',
     '1111111133', 'user3@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (34, 'Test 4 User', 'Four', 'N',
     '1111111134', 'user4@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (35, 'Test 5 User', 'Five', 'H',
     '1111111135', 'user5@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (11, 'Test Client', 'One', 'T',
     '1111111111', 'client1@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (12, 'Test 2 Client', 'Two', 'H',
     '1111111112', 'client2@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (13, 'Test 3 Client', 'Three', 'S',
     '1111111113', 'client3@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (14, 'Test 4 Client', 'Four', 'N',
     '1111111114', 'client4@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (15, 'Test 5 Client', 'Five', 'H',
     '1111111115', 'client5@morriswa.org',
     '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
     'EMAIL', CURRENT_TIMESTAMP),
    (21, 'Test Employee', 'One', 'T',
        '1111111121', 'employee1@morriswa.org',
        '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
        'EMAIL', CURRENT_TIMESTAMP),
    (22, 'Test 2 Employee', 'Two', 'H',
        '1111111122', 'employee2@morriswa.org',
        '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
        'EMAIL', CURRENT_TIMESTAMP),
    (23, 'Test 3 Employee', 'Three', 'S',
        '1111111123', 'employee3@morriswa.org',
        '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
        'EMAIL', CURRENT_TIMESTAMP),
    (24, 'Test 4 Employee', 'Four', 'N',
        '1111111124', 'employee4@morriswa.org',
        '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
        'EMAIL', CURRENT_TIMESTAMP),
    (25, 'Test 5 Employee', 'Five', 'H',
        '1111111125', 'employee5@morriswa.org',
        '1234 Main St.', NULL, 'Jamestown', 'DC', '55555',
        'EMAIL', CURRENT_TIMESTAMP);

insert into client (client_id)
values (11), (12), (13), (14), (15);

insert into employee (employee_id)
values (21), (22), (23), (24), (25);

insert into provided_service (service_id, employee_id, provided_service_name, default_cost, default_length)
values (251, 25, 'Test 5 Employee Service 1', 123.45, 1);
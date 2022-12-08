INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$v32h8OfSAeWV9KX.FnJ/kuaedWsaptVUyDDJE52rAaRXnwhcmgTwS', 'admin', 'admin', 'admin@admin.a', '+000000000000', true, false,'https://www.pngitem.com/pimgs/m/247-2472306_admin-anonymous-person-icon-hd-png-download.png');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tzp.n0FfFjWMoh6ojeiQPOQDSw.IUdGUJGxVJ0f8uNeSMWUux2X3K', 'Asel', 'Asel', 'Asel@admin.a', '+1542134213213', true, false,'https://www.pngitem.com/pimgs/m/247-2472306_admin-anonymous-person-icon-hd-png-download.png');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tzp.n0FfFjWMoh6ojeiQPOQDSw.IUdGUJGxVJ0f8uNeSMWUux2X3K', 'Adelina', 'Adelina', 'Adelina@admin.a', '+42424124213321', true, false,'https://www.pngitem.com/pimgs/m/247-2472306_admin-anonymous-person-icon-hd-png-download.png');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tzp.n0FfFjWMoh6ojeiQPOQDSw.IUdGUJGxVJ0f8uNeSMWUux2X3K', 'Kanay', 'Kanay', 'Kanay@admin.a', '+42142142142133', true, false,'https://www.pngitem.com/pimgs/m/247-2472306_admin-anonymous-person-icon-hd-png-download.png');

INSERT INTO roles (user_id, roles) VALUES (1, 'ROLE_SUPERADMIN');
INSERT INTO roles (user_id, roles) VALUES (2, 'ROLE_ADMIN');
INSERT INTO roles (user_id, roles) VALUES (3, 'ROLE_ADMIN');
INSERT INTO roles (user_id, roles) VALUES (4, 'ROLE_ADMIN');

INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Dwayne', 'Johnson', 'therock@benomad.kg', null, true, false,
        'https://cdn.britannica.com/60/222660-050-064DBA89/Dwayne-Johnson-AKA-The-Rock-2019.jpg?w=600&h=450&c=crop&q=80');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'George', 'Wallace', 'wallace@benomad.kg', null, true, false,
        'https://netstorage-tuko.akamaized.net/images/7837e1645580ee67.jpg?&imwidth=1200');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Alexander', 'Predko', 'alex.predko@benomad.kg', null, true, false,
        'https://rees.sas.upenn.edu/sites/default/files/styles/spotlight_node/public/Platt%20%282%29_0.jpg?itok=0uhnnjsL');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Scarlett', 'Gomez', 'sc.gomez@benomad.kg', null, true, false,
        'https://cdn.lifehack.org/wp-content/uploads/2015/03/beautiful-woman-smiling.jpg');

INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Donovan', 'Mendes', 'mitchell@benomad.kg', null, true, false,
        'https://images.unsplash.com/photo-1605032187764-ae0c3c2e16fd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8bWV4aWNhbiUyMG1hbnxlbnwwfHwwfHw%3D&w=1000&q=80');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Kyle', 'Bonner', 'bonner558@benomad.kg', null, true, false,
        'https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8bWFsZSUyMHByb2ZpbGV8ZW58MHx8MHx8&w=1000&q=80');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Sofia', 'Keller', 'skeller@benomad.kg', null, true, false,
        'https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg.jpg?fit=640,427');
INSERT INTO users (id, password, first_name, last_name, email, phone_number, is_activated, is_deleted,image_url)
VALUES (nextval('users_id_seq'), '$2a$12$tFsqLljxr2ah9XBtFG4KOuCo1m52vjuovdh4iL6N.lvG.Tu1kr9iS',
        'Amanda', 'Frost', 'frostttt@benomad.kg', null, true, false,
        'https://media.istockphoto.com/id/1381221247/photo/beautiful-afro-girl-with-curly-hairstyle.jpg?b=1&s=170667a&w=0&k=20&c=0x91osZOkL8EfhTEEGNa2EeCGN2gdMTNULOsUFW_0i4=');

INSERT INTO roles (user_id, roles) VALUES (5, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (6, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (7, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (8, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (9, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (10, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (11, 'ROLE_USER');
INSERT INTO roles (user_id, roles) VALUES (12, 'ROLE_USER');
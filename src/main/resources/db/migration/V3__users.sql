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

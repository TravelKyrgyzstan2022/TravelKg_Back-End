create table articles (id  bigserial not null, body varchar(10000) not null, image_url varchar(255) not null, title varchar(500) not null, user_id int8, primary key (id));
create table blog_comments (blog_id int8 not null, comment_id int8 not null, primary key (blog_id, comment_id));
create table blog_likes (blog_id int8 not null, user_id int8 not null, primary key (blog_id, user_id));
create table blogs (id  bigserial not null, body varchar(10000) not null, creation_date date, is_deleted boolean not null, review_status varchar(255), title varchar(500) not null, update_date date, author_id int8, deletion_info_id int8, primary key (id));
create table comment_likes (comment_id int8 not null, liked_users_id int8 not null, primary key (comment_id, liked_users_id));
create table comments (id  bigserial not null, body varchar(2000) not null, creation_date date, is_deleted boolean not null, update_date date, deletion_info_id int8, user_id int8 not null, primary key (id));
create table deletion_info (id  bigserial not null, deletion_date date, reason varchar(255), responsible_user_id int8, primary key (id));
create table favorite_places (user_id int8 not null, place_id int8 not null, primary key (user_id, place_id));
create table place_comments (place_id int8 not null, comment_id int8 not null, primary key (place_id, comment_id));
create table place_ratings (id  bigserial not null, rating int4 not null, place_id int8, user_id int8, primary key (id));
create table places (id  bigserial not null, address varchar(255), description varchar(2000) not null, image_url varchar(255) not null, link_url varchar(255) not null, name varchar(255) not null, place_type varchar(255), region varchar(255), primary key (id));
create table plans (id  bigserial not null, end_date date, note varchar(500), start_date date, place_id int8, user_id int8, primary key (id));
create table refreshtokens (id  bigserial not null, expiry_date timestamp not null, token varchar(255) not null, user_id int8, primary key (id));
create table roles (user_id int8 not null, roles varchar(255));
create table users (id  bigserial not null, registration_date date, email varchar(255) not null, first_name varchar(255) not null, is_activated boolean, is_deleted boolean not null, last_name varchar(255) not null, last_visit_date timestamp, password varchar(255) not null, phone_number varchar(255), deletion_info_id int8, primary key (id));
alter table blog_comments add constraint UK_ny0y7e9s53y6l4sfgpksye42j unique (comment_id);
alter table place_comments add constraint UK_ja9pdfmnuagk0g89e29gpbw4n unique (comment_id);
alter table refreshtokens add constraint UK_1lfxk1odre7ch1v66hsfi5xq6 unique (token);
alter table users add constraint uk_email unique (email);
alter table users add constraint uk_phone_number unique (phone_number);
alter table articles add constraint FKlc3sm3utetrj1sx4v9ahwopnr foreign key (user_id) references users;
alter table blog_comments add constraint FK155d2e10b59q2a6nbcvg9ihi4 foreign key (comment_id) references comments;
alter table blog_comments add constraint FKnadbu7mnnulxvib0jc5dqn7to foreign key (blog_id) references blogs;
alter table blog_likes add constraint FKfqaey79qg6v969ymdp40ft3b foreign key (user_id) references users;
alter table blog_likes add constraint FKojo8uaqhkmbtqa8duxtbq56w3 foreign key (blog_id) references blogs;
alter table blogs add constraint FKt8g0udj2fq40771g38t2t011n foreign key (author_id) references users;
alter table blogs add constraint FKbwvw231xkvh004engns4mt18n foreign key (deletion_info_id) references deletion_info;
alter table comment_likes add constraint FKjhjsx3g4rmc9ye8k5xafeprrk foreign key (liked_users_id) references users;
alter table comment_likes add constraint FK3wa5u7bs1p1o9hmavtgdgk1go foreign key (comment_id) references comments;
alter table comments add constraint FKhj6bbu6kfeoq8j011r34671tv foreign key (deletion_info_id) references deletion_info;
alter table comments add constraint FK8omq0tc18jd43bu5tjh6jvraq foreign key (user_id) references users;
alter table deletion_info add constraint FK9gkdtwx9f6g37rs74oxq027jb foreign key (responsible_user_id) references users;
alter table favorite_places add constraint FK4pf98npou01hnsgywly8isn0u foreign key (place_id) references places;
alter table favorite_places add constraint FK5tmdgtn9286q309p0x4hv0cwc foreign key (user_id) references users;
alter table place_comments add constraint FKg9cjbcwnns5kmqxakxod2dwxp foreign key (comment_id) references comments;
alter table place_comments add constraint FK9gtx2cxplxjc9ef3l63ho39n4 foreign key (place_id) references places;
alter table place_ratings add constraint FKkfg5s7jd8h47f6s14g3xysurp foreign key (place_id) references places;
alter table place_ratings add constraint FKhu9y3jlohhtigrluc8c4gwgwd foreign key (user_id) references users;
alter table plans add constraint FKfvplurce46plnvh6vhf2irhko foreign key (place_id) references places;
alter table plans add constraint FKbybv5po44ssyv6svnv062dwrf foreign key (user_id) references users;
alter table refreshtokens add constraint FKg71xhi5ujnqbgw2rcvtxyrc8s foreign key (user_id) references users;
alter table roles add constraint fk_users_role foreign key (user_id) references users;
alter table users add constraint FKoc9wtxet5svvyiitpexiptlwh foreign key (deletion_info_id) references deletion_info;
-- bcrypt algorithm
insert into users (username, password, enabled) values ('admin', '{bcrypt}$2a$10$VBUN5a2xj4WyqaGpiDqN2uDVN4TCjiac7Pw/zZ0rp2EncN4dCWT6q', true);
insert into users (username, password, enabled) values ('user', '{bcrypt}$2a$10$/rDUhF2xjr6amdNJeDg/BuvDVX6ajjNpvMj7Jf.eaUsQGYcdUPZp6', true);

-- PBKDF2 algorithm
-- insert into users (username, password, enabled) values ('admin', '{pbkdf2}83e12b82b3c2fc0eaa1c375446d9b860245ee809a848a25e113922166d400804b78dea2363a6377a', true);
-- insert into users (username, password, enabled) values ('user', '{pbkdf2}82ce0b8549150b3d03461d182749c844c0c7d553a44ca85ad2095ff1d74cf873426639feedfdbc09', true);

insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('admin', 'ROLE_USER');
insert into authorities (username, authority) values ('user', 'ROLE_USER');
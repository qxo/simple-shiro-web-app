create table PERMISSIONS (name varchar(30) not null, description varchar(255), primary key (name));
create table ROLES (name varchar(20) not null, description varchar(255), primary key (name));
create table ROLES_PERMISSIONS (role_name varchar(20) not null, permission varchar(30) not null);
create table USERS (username varchar(15) not null, email varchar(100), name varchar(65), password varchar(255) not null, primary key (username));
create table USERS_ROLES (username varchar(15) not null, role_name varchar(20) not null);
--alter table ROLES_PERMISSIONS add index RP_1 (role_name) add constraint RP_1 foreign key (role_name) references ROLES (name);
--alter table ROLES_PERMISSIONS add index RP_2 (permission), add constraint RP_2 foreign key (permission) references PERMISSIONS (name);
--alter table USERS_ROLES add index UR_1 (username), add constraint UR_1 foreign key (username) references USERS (username);
--alter table USERS_ROLES add index UR_2 (role_name), add constraint UR_2 foreign key (role_name) references ROLES (name);

-- insert users
-- The password values are the output of Shiro's command line hasher:
-- java -jar shiro-tools-hasher-1.2.0-cli.jar -p
-- using a plaintext password of 123qwe
INSERT INTO USERS (username, name, email, password) VALUES('admin', 'Administrator', 'admin@example.com', '$shiro1$SHA-256$500000$QmLtx8PaCMe72i+yVuqH+A==$P5ohK5uWi30u38ujuTnmmeUK2gPwqhxTnke2wd9fZXw=');
INSERT INTO USERS (username, name, email, password) VALUES('u1', 'User P1', 'u1@example.com', '$shiro1$SHA-256$500000$QmLtx8PaCMe72i+yVuqH+A==$P5ohK5uWi30u38ujuTnmmeUK2gPwqhxTnke2wd9fZXw=');
INSERT INTO USERS (username, name, email, password) VALUES('u2', 'User P2', 'u2@example.com', '$shiro1$SHA-256$500000$QmLtx8PaCMe72i+yVuqH+A==$P5ohK5uWi30u38ujuTnmmeUK2gPwqhxTnke2wd9fZXw=');

--# insert roles
INSERT INTO ROLES (name, description) VALUES('ADMIN', 'Administrator role');
INSERT INTO ROLES (name, description) VALUES('USER_P1', 'Perfil 1');
INSERT INTO ROLES (name, description) VALUES('USER_P2', 'Perfil 2');

--# insert relationships
INSERT INTO USERS_ROLES (username, role_name) VALUES('admin', 'ADMIN');
INSERT INTO USERS_ROLES (username, role_name) VALUES('u1', 'USER_P1');
INSERT INTO USERS_ROLES (username, role_name) VALUES('u2', 'USER_P2');

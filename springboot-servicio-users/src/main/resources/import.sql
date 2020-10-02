insert into users(username,password,enabled,name,surname,email) values('enzo','$2a$10$Qye/7j317figm1NLt49G6.a2HF1RwyQ7krhFFObAdpHkivp6.1l42',true,'enzo','roselli','enzo@gmail.com');
insert into users(username,password,enabled,name,surname,email) values('admin','$2a$10$s2AHiShijVmbojexL89lNO/N/vpRLchW3Annpf/5MLCEXyVKT6Fhy',true,'admin','admin','admin@gmail.com');

--'ROLE-USER' y 'ROLE_ADMIN' son un estandar en Spring Security, los roles tiene que llamarse 'ROLE_'
insert into roles(name) values('ROLE_USER');
insert into roles(name) values('ROLE_ADMIN');

insert into users_to_roles(user_id, role_id) values(1,1);
insert into users_to_roles(user_id, role_id) values(2,1);
insert into users_to_roles(user_id, role_id) values(2,2);
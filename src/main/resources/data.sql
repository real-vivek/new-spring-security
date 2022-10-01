insert into users(username,password,enabled) values('user','pass',true);
insert into users(username,password,enabled) values('root','root',true);

insert into authorities(username,authority) values('user','ROLE_USER');
insert into authorities(username,authority) values('root','ROLE_ADMIN');
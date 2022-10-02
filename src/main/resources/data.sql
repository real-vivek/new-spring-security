insert into my_users(uname,pass,enabled) values('user','pass',true);
insert into my_users(uname,pass,enabled) values('root','root',true);

insert into my_authorities(uname,authority) values('user','ROLE_USER');
insert into my_authorities(uname,authority) values('root','ROLE_ADMIN');
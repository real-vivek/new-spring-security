insert into my_users(uname,pass,enabled) values('user','pass',true);
insert into my_users(uname,pass,enabled) values('root','root',true);

insert into my_authorities(uname,authority) values('user','ROLE_USER');
insert into my_authorities(uname,authority) values('root','ROLE_ADMIN');

insert into customer(id,email,pwd,role) values(123,'real-vivek@github.com','pass@123','ROLE_ADMIN');
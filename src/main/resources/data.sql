insert into my_users(uname,pass,enabled) values('user','$2a$12$ztUSg/lyDPYoQBr/WSmMqugIqMt0okHuKG/IKlNvRYMW6wkFSSfzW',true);
insert into my_users(uname,pass,enabled) values('root','$2a$12$6jRaw1SfRvFIPZbzS1Sph.h6yDpeQqfgJRVts8aldqkmj7aZKYN6i',true);

insert into my_authorities(uname,authority) values('user','ROLE_USER');
insert into my_authorities(uname,authority) values('root','ROLE_ADMIN');

insert into customer(id,email,pwd,role) values(123,'real-vivek@github.com','$2a$12$vSr1aoQh.GqIqkaKzd5J5OBCJzHdQNxydNKFxSqLCmeqHtpn1JJKG','ROLE_ADMIN');
drop database if exists dbzoo;
drop user if exists 'dbzoo'@'localhost';

create database dbzoo;
create user 'dbzoo'@'localhost';

grant all privileges on dbzoo.* to 'dbzoo'@'localhost';

create table cages (
 id int primary key auto_increment,
 size int not null
 );
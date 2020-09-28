drop database if exists dbzoo;
drop user if exists 'dbzoo'@'localhost'

create database dbzoo;
create user 'dbzoo'@'localhost';

grant all privileges on dbzoo.* to 'dbzoo'@'localhost';
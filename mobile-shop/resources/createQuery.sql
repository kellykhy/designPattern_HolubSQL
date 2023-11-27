create database Dbase

create table restaurant \
	(id int, name varchar, primary key(id))


create table menu \
	(id int, restaurant_id int, name varchar, type varchar, price int, primary key(id))
create database Dbase

create table restaurant \
	(id int, name varchar, primary key(id))




insert into restaurant values( 0,'도미노 피자')
insert into restaurant values( 1, '피자스쿨')
insert into restaurant values( 2, '피자헛')
insert into restaurant values( 3, '미스터 피자')

create table menu \
(id int, restaurant_id int, name varchar, type varchar, price int, primary key(id))

    insert into menu values(0, 0, 'Hawaiian Pizza', 'PIZZA', 20000)
insert into menu values(1, 2, 'Pepperoni Pizza', 'PIZZA', 18000)
insert into menu values(2, 1, 'Chicken Alfredo Pasta', 'PASTA', 22000)
insert into menu values(3, 3, 'Caesar Salad', 'SALAD', 12000)
insert into menu values(4, 0, 'Vegetarian Pizza', 'PIZZA', 19000)
insert into menu values(5, 1, 'Spaghetti Bolognese', 'PASTA', 21000)
insert into menu values(6, 2, 'Margarita Pizza', 'PIZZA', 17000)
insert into menu values(7, 3, 'Shrimp Alfredo Pasta', 'PASTA', 23000)
insert into menu values(8, 0, 'BBQ Chicken Pizza', 'PIZZA', 21000)
insert into menu values(9, 1, 'Fettuccine Alfredo', 'PASTA', 20000)
insert into menu values(10, 2, 'Greek Salad', 'SALAD', 15000)
insert into menu values(11, 3, 'Meat Lovers Pizza', 'PIZZA', 22000)
insert into menu values(12, 0, 'Penne alla Vodka', 'PASTA', 19000)
insert into menu values(13, 1, 'Caprese Salad', 'SALAD', 16000)
insert into menu values(14, 2, 'Buffalo Chicken Pizza', 'PIZZA', 20000)
insert into menu values(15, 3, 'Seafood Alfredo', 'PASTA', 24000)
insert into menu values(16, 0, 'Vegetable Pasta Primavera', 'PASTA', 18000)
insert into menu values(17, 1, 'Spinach and Strawberry Salad', 'SALAD', 14000)
insert into menu values(18, 2, 'Pesto Chicken Pizza', 'PIZZA', 21000)
insert into menu values(19, 3, 'Lobster Ravioli', 'PASTA', 25000)
insert into menu values(20, 0, 'Margherita Flatbread', 'PIZZA', 16000)
insert into menu values(21, 1, 'Carbonara Pasta', 'PASTA', 23000)
insert into menu values(22, 2, 'Cobb Salad', 'SALAD', 17000)
insert into menu values(23, 3, 'Truffle Mushroom Pizza', 'PIZZA', 22000)
insert into menu values(24, 0, 'Chicken Pesto Pasta', 'PASTA', 20000)
insert into menu values(25, 1, 'Caesar Wrap', 'SALAD', 13000)
insert into menu values(26, 2, 'Supreme Pizza', 'PIZZA', 23000)
insert into menu values(27, 3, 'Lemon Garlic Shrimp Pasta', 'PASTA', 24000)

insert into menu values(28, 0, '오리지날', 'DOUGH', 0)
insert into menu values(29, 0, '나폴리', 'DOUGH', 2000)
insert into menu values(30, 0, '치즈크러스트', 'DOUGH', 2000)
insert into menu values(31, 0, '고구마', 'DOUGH', 2000)
insert into menu values(32, 0, '고구마 치즈크러스트', 'DOUGH', 3000)

insert into menu values(33, 1, '오리지날', 'DOUGH', 0)
insert into menu values(34, 1, '나폴리', 'DOUGH', 2000)
insert into menu values(35, 1, '치즈크러스트', 'DOUGH', 2000)
insert into menu values(36, 1, '고구마', 'DOUGH', 2000)
insert into menu values(37, 1, '고구마 치즈크러스트', 'DOUGH', 3000)

insert into menu values(38, 2, '오리지날', 'DOUGH', 0)
insert into menu values(39, 2, '나폴리', 'DOUGH', 2000)
insert into menu values(40, 2, '치즈크러스트', 'DOUGH', 2000)
insert into menu values(41, 2, '고구마', 'DOUGH', 2000)
insert into menu values(32, 2, '고구마 치즈크러스트', 'DOUGH', 3000)

insert into menu values(38, 3, '오리지날', 'DOUGH', 0)
insert into menu values(39, 3, '나폴리', 'DOUGH', 2000)
insert into menu values(40, 3, '치즈크러스트', 'DOUGH', 2000)
insert into menu values(41, 3, '고구마', 'DOUGH', 2000)
insert into menu values(32, 3, '고구마 치즈크러스트', 'DOUGH', 3000)



select * \
from menu
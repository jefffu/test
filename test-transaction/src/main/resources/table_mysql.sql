drop table if exists users;
drop table if exists address;

create table users(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name varchar(8) not null,
  email varchar(128) not null,
  address_id int not null
);

create table address(
  id INT PRIMARY KEY AUTO_INCREMENT,
  detail varchar(1024) not null,
  postcode int not null
);

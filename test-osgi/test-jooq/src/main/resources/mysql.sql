create database library;
grant all privileges on library.* to 'test'@'localhost' identified by '123456' with grant option;
grant all privileges on library.* to 'test'@'%' identified by '123456' with grant option;

use library;
CREATE TABLE `author` (
  `id` int NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

create database if not exists writerapp;
use writerapp;
INSERT IGNORE INTO writer (id, firstName, lastName) VALUES (1, 'John', 'Doe');
INSERT IGNORE INTO writer (id, firstName, lastName) VALUES (2, 'Jane', 'Brown');
INSERT IGNORE INTO writer (id, firstName, lastName) VALUES (3, 'Jim', 'Fisher');
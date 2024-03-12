create database if not exists writerapp;
use writerapp;
insert ignore into poststatus (status) values ('DRAFT'), ('ACTIVE'), ('UNDER_REVIEW'), ('DELETED');

create database if not exists writerapp;
use writerapp;
insert ignore into poststatus (status) values ('DRAFT'), ('ACTIVE'), ('UNDER_REVIEW'), ('DELETED');
#       ACTIVE(1),
#       UNDER_REVIEW(2),
#       DELETED(3),
#       DRAFT(4);
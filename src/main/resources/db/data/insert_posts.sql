create database if not exists writerapp;
use writerapp;
INSERT IGNORE INTO post (id, content, created, updated, status, writer_id)
VALUES (1, 'Post1 content', NOW(), NOW(), 'ACTIVE', 1);
INSERT IGNORE INTO post (id, content, created, updated, status, writer_id)
VALUES (2, 'Post2 content', NOW(), NOW(), 'UNDER_REVIEW', 2);
INSERT IGNORE INTO post (id, content, created, updated, status, writer_id)
VALUES (3, 'Post3 content', NOW(), NOW(), 'DELETED', 3);
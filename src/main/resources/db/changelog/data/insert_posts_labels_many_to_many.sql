create database if not exists writerapp;
use writerapp;
INSERT IGNORE INTO post_labels (post_id, label_id)
VALUES (1, 1), (1, 2), (2, 2), (3, 3);
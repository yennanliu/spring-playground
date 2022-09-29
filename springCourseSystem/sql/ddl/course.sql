-- DDL for course (mysql)
-- DB : course_system

-- create database course_system;

CREATE TABLE IF NOT EXISTS course(
    course_no varchar(50) PRIMARY KEY,
    course_name varchar(100) not null,
    course_hours int not null,
    type_id int not null,
    course_status varchar(1) not null,
    course_reqs varchar(20) not null,
    course_points decimal(3,1),
    course_memo varchar(1000),
    course_textbook_pic mediumblob,
    constraint FK_COURSE_TYPE FOREIGN KEY (type_id) references course_type(type_id)
);
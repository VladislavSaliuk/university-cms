create table public.faculties (
    faculty_id bigserial not null,
    faculty_name varchar(255) not null unique,
    primary key (faculty_id)
);

create table public.groups (
    group_id bigserial not null,
    group_name varchar(255) not null unique,
    primary key (group_id)
);

create table public.students (
    student_id bigserial not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255) not null unique,
    password varchar(255) not null,
    primary key (student_id)
);

create table public.subjects (
    time time(6) not null,
    subject_id bigserial not null,
    subject_description varchar(255) not null,
    subject_name varchar(255) not null unique,
    primary key (subject_id)
);

create table public.teachers (
    teacher_id bigserial not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255) not null unique,
    password varchar(255) not null,
    primary key (teacher_id)
);

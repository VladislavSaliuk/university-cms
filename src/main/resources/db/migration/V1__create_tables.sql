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
    age integer,
    faculty_id bigint,
    group_id bigint,
    student_id bigserial not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255) not null unique,
    password varchar(255) not null,
    phone_number varchar(255) not null unique,
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
    age integer,
    faculty_id bigint,
    group_id bigint,
    teacher_id bigserial not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255) not null unique,
    password varchar(255) not null,
    phone_number varchar(255) not null unique,
    primary key (teacher_id)
);

alter table if exists public.students
add constraint Fk_students_faculty_id foreign key (faculty_id)
references public.faculties;

alter table if exists public.students
add constraint Fk_students_group_id foreign key (group_id)
references public.groups;

alter table if exists public.teachers
add constraint Fk_teachers_faculty_id foreign key (faculty_id)
references public.faculties;

alter table if exists public.teachers
add constraint Fk_teachers_group_id
foreign key (group_id) references public.groups;
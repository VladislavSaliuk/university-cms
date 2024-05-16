create table if not exists public.faculties (
    faculty_id bigserial not null,
    teacher_teacher_id bigint,
    faculty_name varchar(255),
    primary key (faculty_id)
);

create table if not exists public.groups (
    group_id bigserial not null,
    teacher_teacher_id bigint,
    group_name varchar(255),
    primary key (group_id)
);

create table if not exists public.schedule (
    schedule_id bigserial not null,
    primary key (schedule_id)
);

create table if not exists public.students (
    student_id bigserial not null,
    login varchar(255),
    password varchar(255),
    email varchar(255),
    phone_number varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    age integer,
    faculty_id bigint unique,
    group_id bigint unique,
    primary key (student_id)
);

create table if not exists public.subjects (
    subject_id bigserial not null,
    subject_name varchar(255),
    subject_description varchar(255),
    time time(6),
    schedule_schedule_id bigint,
    primary key (subject_id)
);

create table if not exists public.teachers (
    teacher_id bigserial not null,
    login varchar(255),
    password varchar(255),
    email varchar(255),
    phone_number varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    age integer,
    primary key (teacher_id)
);

alter table if exists public.faculties
    add constraint Fk_teacher_id_faculties
    foreign key (teacher_teacher_id)
    references public.teachers;

alter table if exists public.groups
    add constraint Fk_teacher_id_groups
    foreign key (teacher_teacher_id)
    references public.teachers;

alter table if exists public.students
    add constraint Fk_faculty_id_students
    foreign key (faculty_id) references
    public.faculties;

alter table if exists public.students
    add constraint Fk_group_id_students
    foreign key (group_id)
    references public.groups;

alter table if exists public.subjects
    add constraint Fk_schedule_id_subjects
    foreign key (schedule_schedule_id)
    references public.schedule;
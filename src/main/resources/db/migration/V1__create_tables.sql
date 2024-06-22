
create table public.roles (
    role_id bigserial not null,
    role_name varchar(255) not null unique,
    primary key (role_id)
);

create table public.courses (
    course_id bigserial not null,
    course_name varchar(255) not null unique,
    course_description varchar(255) not null,
    primary key (course_id)
);

create table public.groups (
    group_id bigserial not null,
    group_name varchar(255)
    not null unique,
    primary key (group_id)
);

create table public.users (
    user_id bigserial not null,
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    role_id bigint not null,
    primary key (user_id)
);

create table public.groups_courses (
    course_id bigint not null,
    group_id bigint not null,
    primary key (course_id, group_id)
);

create table public.users_courses (
    course_id bigint not null,
    user_id bigint not null,
    primary key (course_id, user_id)
);

alter table if exists public.users
    add constraint Fk_users_role_id foreign key (role_id)
    references public.roles;

alter table if exists public.groups_courses
    add constraint Fk_groups_courses_course_id foreign key (course_id)
    references public.courses;

alter table if exists public.groups_courses
    add constraint Fk_groups_courses_group_id foreign key (group_id)
    references public.groups;

alter table if exists public.users_courses
    add constraint Fk_users_courses_course_id foreign key (course_id)
    references public.courses;

alter table if exists public.users_courses
    add constraint Fk_users_courses_user_id foreign key (user_id)
    references public.users;
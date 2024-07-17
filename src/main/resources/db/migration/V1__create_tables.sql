
create table public.roles (
    role_id bigserial not null,
    role_name varchar(255) not null unique,
    primary key (role_id)
);

create table public.courses (
    end_course_time time(6),
    start_course_time time(6),
    course_id bigserial not null,
    course_description varchar(255) not null,
    course_name varchar(255) not null unique,
    day_of_week varchar(255),
    primary key (course_id)
);

create table public.groups (
    group_id bigserial not null,
    group_name varchar(255) not null unique,
    primary key (group_id)
);

create table public.user_statuses (
    user_status_id bigserial not null,
    user_status_name varchar(255) not null unique,
    primary key (user_status_id)
);

create table public.users (
    group_id bigint,
    role_id bigint not null,
    user_id bigserial not null,
    user_status_id bigint not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255) not null,
    username varchar(255) not null unique,
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

alter table if exists public.users
    add constraint Fk_users_user_status_id foreign key (user_status_id)
    references public.user_statuses;

alter table if exists public.users
    add constraint Fk_users_group_id foreign key (group_id)
    references public.groups;

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
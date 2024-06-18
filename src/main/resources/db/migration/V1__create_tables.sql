
create table public.courses (
    course_id bigserial not null,
    course_name varchar(255) not null unique,
    course_description varchar(255) not null,
    primary key (course_id)
);

create table public.roles (
    role_id bigserial not null,
    role_name varchar(255) not null unique,
    primary key (role_id)
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

alter table if exists public.users
    add constraint Fk_users_role_id foreign key (role_id)
    references public.roles;
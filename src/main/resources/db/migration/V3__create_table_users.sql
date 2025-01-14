
create table public.users (
    user_id bigserial not null,
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    firstname varchar(255),
    lastname varchar(255),
    role varchar(255) not null check (role in ('STUDENT','TEACHER','STUFF','ADMIN')),
    status varchar(255) not null check (status in ('ACTIVE','BANNED')),
    group_id bigint,
    primary key (user_id)
);

alter table if exists public.users
    add constraint Fk_users_group_id foreign key (group_id)
    references public.groups;
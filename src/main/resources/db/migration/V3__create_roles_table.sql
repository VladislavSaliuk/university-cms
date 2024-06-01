create table public.roles (
    role_id bigserial not null,
    role_name varchar(255) not null unique,
    primary key (role_id)
);
create table public.admins (
    admin_id bigserial not null,
    role_id bigint,
    login varchar(255) not null unique,
    password varchar(255) not null,
    primary key (admin_id)
);

alter table if exists public.admins
add constraint Fk_admins_role_id foreign key (role_id)
references public.roles;


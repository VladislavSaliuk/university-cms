
create table public.groups (
    group_id bigserial not null,
    name varchar(255) not null unique,
    primary key (group_id)
);



create table public.groups (
    group_id bigserial not null,
    group_name varchar(255) not null unique,
    primary key (group_id)
);


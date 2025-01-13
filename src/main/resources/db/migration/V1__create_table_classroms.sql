
create table public.classrooms (
    classroom_id bigserial not null,
    number bigint not null unique,
    primary key (classroom_id)
);


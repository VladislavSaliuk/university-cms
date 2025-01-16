
create table public.classrooms (
    classroom_id bigserial not null,
    number bigint not null unique,
    description varchar(255),
    primary key (classroom_id)
);


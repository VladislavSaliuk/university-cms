
create table public.classrooms (
    classroom_id bigserial not null,
    classroom_number bigint not null unique,
    classroom_description varchar(255),
    primary key (classroom_id)
);



create table public.courses (
    course_id bigserial not null,
    course_name varchar(255) not null unique,
    description varchar(255),
    user_id bigint,
    primary key (course_id)
);

alter table if exists public.courses
    add constraint Fk_courses_user_id foreign key (user_id)
    references public.users;
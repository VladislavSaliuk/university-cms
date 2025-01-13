
create table public.lessons (
    lesson_id bigserial not null,
    course_id bigint not null,
    group_id bigint not null,
    start_time time(6) not null,
    end_time time(6) not null,
    day_of_week varchar(255) check (day_of_week in ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY')) not null,
    classroom_id bigint not null,
    primary key (lesson_id)
);

alter table if exists public.lessons
    add constraint Fk_lessons_course_id foreign key (course_id)
    references public.courses;

alter table if exists public.lessons
    add constraint Fk_lessons_group_id foreign key (group_id)
    references public.groups;

alter table if exists public.lessons
    add constraint Fk_lessons_classroom_id foreign key (classroom_id)
    references public.classrooms;
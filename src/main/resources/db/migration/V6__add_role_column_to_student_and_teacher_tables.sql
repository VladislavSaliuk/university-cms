alter table if exists public.students
add column role_id bigint;

alter table if exists public.teachers
add column role_id bigint;

alter table if exists public.students
add constraint Fk_students_role_id foreign key (role_id)
references public.roles;

alter table if exists public.teachers
add constraint Fk_teachers_role_id foreign key (role_id)
references public.roles;
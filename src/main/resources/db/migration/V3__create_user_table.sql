create table public.user (user_id bigint not null,
     login varchar(255),
     password varchar(255),
     role varchar(255),
     primary key (user_id)
)
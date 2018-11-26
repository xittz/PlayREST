# --- !Ups

create table "users" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "username" varchar not null,
  "password" varchar not null
);

insert into "users" values (1, "xittz", "1233ds");
insert into "users" values (1, "admin", "asddsa13ds");
insert into "users" values (1, "kostya77", "apple808s");
insert into "users" values (1, "iwbbtt", "dasdass");

# --- !Downs

drop table "users" if exists;

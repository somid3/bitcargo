# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cargo (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  created_on                datetime,
  kill_on                   datetime,
  directory                 varchar(255),
  manage_uri                varchar(255),
  read_only_uri             varchar(255),
  read_write_uri            varchar(255),
  secret_read_only          varchar(255),
  secret_read_write         varchar(255),
  secret_encrypted          varchar(255),
  constraint pk_cargo primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  created_on                datetime,
  constraint pk_user primary key (id))
;

alter table cargo add constraint fk_cargo_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_cargo_user_1 on cargo (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table cargo;

drop table user;

SET FOREIGN_KEY_CHECKS=1;


drop table news;
drop table sources;
drop table lessons;
drop table cells;
drop table columns;
drop table schedules;
-- for telegram
drop table users;
drop table subscriptions;

create table news
(
    id           integer primary key autoincrement,
    title        text,
    caption      text,
    article_link text,
    img_link     text,
    parsing_date text
);

create table lessons
(
    id      integer primary key autoincrement,
    cell_id integer,
    foreign key (cell_id) references cells (id)
);

create table sources
(
    id             integer primary key autoincrement,
    value          text,
    link_parameter text,
    type           text,
    parsing_date   text,
    lesson_id      integer,
    user_id        integer,
    foreign key (lesson_id) references lessons (id),
    foreign key (user_id) references users (id)
);

create table cells
(
    id        integer primary key autoincrement,
    column_id id integer,
    foreign key (column_id) references columns (id)
);

create table columns
(
    id          integer primary key autoincrement,
    is_approved text,
    schedule_id integer,
    day_number  integer,
    foreign key (schedule_id) references schedules (id)
);

create table schedules
(
    id           integer primary key,
    parsing_date text,
    foreign key (id) references sources (id)
);

-- for telegram
create table users
(
    id                          integer primary key,
    firstname                   text,
    lastname                    text,
    state                       text,
    selected_source_category    text,
    selected_source_subcategory text
);

create table subscriptions
(
    id        integer primary key autoincrement,
    user_id   integer,
    source_id integer,
    foreign key (user_id) references users (id),
    foreign key (source_id) references sources (id)
);

select *
from users;
select *
from sources
order by type;

select *
from schedules;

select *
from subscriptions;
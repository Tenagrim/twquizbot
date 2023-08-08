create table USER_STATE
(
    ID      integer primary key not null,
    SYSNAME varchar(20)
);

create table Q_USER
(
    ID          serial primary key,
    EXTERNAL_ID varchar(50),
    CHAT_ID     varchar(50),
    NICKNAME    varchar(50),
    STATE_ID    integer references USER_STATE (ID) not null,
    CREATE_DATE timestamp default now()
);

create table QUESTION
(
    ID   serial primary key,
    TEXT text
);

create table Q_SESSION
(
    ID               serial primary key,
    USER_ID          integer references Q_USER (ID),
    CREATE_DATE      timestamp default now(),
    LAST_QUESTION_ID integer references QUESTION (ID)
);

alter table Q_USER add column ACTIVE_SESSION_ID integer references Q_SESSION(ID);

create table ANSWER
(
    ID           serial primary key,
    QUESTION_ID  integer references QUESTION (ID),
    RIGHT_ANSWER boolean,
    TEXT         text
);

create table SESSION_ANSWER
(
    SESSION_ID integer references Q_SESSION (ID),
    ANSWER_ID  integer references ANSWER (ID),
    CREATE_DATE timestamp default now()
);


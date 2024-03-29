DROP TABLE IF EXISTS FILM CASCADE;
DROP TABLE IF EXISTS FAVOURITE CASCADE;
DROP TABLE IF EXISTS FILM_GENRE CASCADE;
DROP TABLE IF EXISTS FILMORATE_USER CASCADE;
DROP TABLE IF EXISTS FRIEND CASCADE;
DROP TABLE IF EXISTS FILM_DIRECTOR CASCADE;
DROP TABLE IF EXISTS GENRE CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;
DROP TABLE IF EXISTS DIRECTOR CASCADE;
DROP TABLE IF EXISTS REVIEW CASCADE;
DROP TABLE IF EXISTS REVIEW_LIKE CASCADE;
DROP TABLE IF EXISTS FEED CASCADE;

create table if not exists GENRE
(
    GENRE_ID INTEGER not null auto_increment,
    GENRE_NAME     CHARACTER LARGE OBJECT,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID    INTEGER not null auto_increment,
    MPA_NAME      CHARACTER LARGE OBJECT,
    constraint "RATING_pk"
        primary key (MPA_ID)
);

create table if not exists DIRECTOR
(
    DIRECTOR_ID    INTEGER not null auto_increment,
    DIRECTOR_NAME      CHARACTER LARGE OBJECT,
    constraint "DIRECTOR_pk"
        primary key (DIRECTOR_ID)
);

create table if not exists FILM
(
    FILM_ID      INTEGER not null auto_increment,
    NAME         CHARACTER LARGE OBJECT not null,
    DESCRIPTION  CHARACTER LARGE OBJECT not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID    INTEGER                   not null,
    constraint "FILM_pk"
        primary key (FILM_ID),
    constraint "FILM_RATING_RATING_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRE
(
    FILM_ID      INTEGER not null,
    GENRE_ID     INTEGER not null,
    constraint "FILM_GENRE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint "FILM_GENRE_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
            on delete cascade,
    constraint "FILM_GENRE_FILM_ID_GENRE_ID_pk"
        primary key (FILM_ID, GENRE_ID)
);

create table if not exists FILM_DIRECTOR
(
    FILM_ID         INTEGER not null,
    DIRECTOR_ID     INTEGER not null,
    constraint "FILM_DIRECTOR_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint "FILM_DIRECTOR_DIRECTOR_DIRECTOR_ID_fk"
        foreign key (DIRECTOR_ID) references DIRECTOR
            on delete cascade,
    constraint "FILM_DIRECTOR_FILM_ID_DIRECTOR_ID_pk"
        primary key (FILM_ID, DIRECTOR_ID)
);

create table if not exists FILMORATE_USER
(
    USER_ID  INTEGER not null auto_increment,
    EMAIL    CHARACTER LARGE OBJECT not null,
    NAME     CHARACTER LARGE OBJECT,
    LOGIN    CHARACTER LARGE OBJECT not null,
    BIRTHDAY DATE                   not null,
    constraint "FILMORATE_USER_pk"
        primary key (USER_ID)
);

create table if not exists FRIEND
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    BOOLEAN default FALSE,
    constraint "FRIEND_USER_USER_ID_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on delete cascade,
    constraint "FRIEND_USER_USER_ID_fk2"
        foreign key (FRIEND_ID) references FILMORATE_USER
            on delete cascade,
    constraint "FRIEND_USER_ID_FRIEND_ID_pk"
        primary key (USER_ID, FRIEND_ID)
);

create table if not exists FAVOURITE
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "FAVOURITE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint "FAVOURITE_USER_USER_ID_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on delete cascade,
    constraint "FAVOURITE_FILM_ID_USER_ID_pk"
        primary key (FILM_ID, USER_ID)
);

create table if not exists REVIEW
(
    REVIEW_ID   INTEGER not null AUTO_INCREMENT,
    CONTENT     CHARACTER LARGE OBJECT not null,
    FILM_ID     INTEGER not null,
    USER_ID     INTEGER not null,
    IS_POSITIVE BOOLEAN not null,
    USEFUL      INTEGER not null default 0,
    constraint "REVIEW_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint "REVIEW_USER_ID_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on delete cascade,
    constraint "REVIEW_ID_pk"
        primary key (REVIEW_ID)
);

create table if not exists REVIEW_LIKE
(
    REVIEW_ID INTEGER not null,
    USER_ID   INTEGER not null,
    IS_LIKE   BOOLEAN not null,
    constraint "REVIEW_LIKE_USER_ID_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on delete cascade,
    constraint "REVIEW_LIKE_REVIEW_ID_fk"
            foreign key (REVIEW_ID) references REVIEW
                on delete cascade,
    constraint "REVIEW_ID_FILM_ID_USER_ID_pk"
        primary key (REVIEW_ID, USER_ID)
);


create table if not exists FEED
(
    EVENT_ID INTEGER not null auto_increment,
    USER_ID INTEGER not null,
    TIMESTAMP BIGINT not null,
    EVENT_TYPE CHARACTER LARGE OBJECT not null,
    OPERATION CHARACTER LARGE OBJECT not null,
    ENTITY_ID INTEGER not null,
    constraint "FEED_USER_ID_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on delete cascade,
    constraint "EVENT_ID_pk"
        primary key (EVENT_ID)
);



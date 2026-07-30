--liquibase formatted sql

--changeset dawidzubrowski:002-2026-07-30-224309-remove-favorite-sport-position
alter table profile_favorite_sports
    drop constraint profile_favorite_sports_pkey;

alter table profile_favorite_sports
    drop column position;

alter table profile_favorite_sports
    add constraint pk_profile_favorite_sports
        primary key (profile_id, favorite_sport);

--liquibase formatted sql

--changeset dawidzubrowski:001-2026-07-30-223325-init-profile-schema
create table profiles (
    id uuid primary key,
    name varchar(255) not null,
    profile_image_url varchar(255) not null
);

create table profile_favorite_sports (
    profile_id uuid not null,
    position integer not null,
    favorite_sport varchar(255) not null,
    primary key (profile_id, position),
    constraint fk_profile_favorite_sports_profile
        foreign key (profile_id) references profiles(id)
        on delete cascade
);

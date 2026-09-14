--liquibase formatted sql

--changeset copilot:002-2026-09-14-213000-add-notifications
create table notifications (
    id varchar(255) primary key,
    profile_id uuid not null,
    title varchar(255) not null,
    message text not null,
    read boolean not null default false,
    created_at timestamp with time zone not null,
    constraint fk_notifications_profile
        foreign key (profile_id) references profiles(id)
        on delete cascade
);

create index idx_notifications_profile_created_at on notifications(profile_id, created_at desc, id desc);

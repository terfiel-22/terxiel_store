create table users
(
    id       bigint auto_increment
        primary key,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) not null
);

create table addresses
(
    id                 bigint auto_increment
        primary key,
    house_number       varchar(255) null,
    street_subdivision varchar(255) null,
    barangay           varchar(255) not null,
    city_municipality  varchar(255) not null,
    province           varchar(255) not null,
    zip_code           varchar(255) not null,
    user_id            bigint       not null,
    constraint addresses_users_id_fk
        foreign key (user_id) references users (id)
);


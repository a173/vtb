CREATE TABLE atm(
    id bigserial PRIMARY KEY,
    address varchar(255) not null,
    latitude numeric(8, 6) not null,
    longitude numeric(8, 6) not null,
    all_day boolean not null
);

CREATE TABLE atm_service(
    id bigserial PRIMARY KEY,
    atm_id bigint references atm(id) ON DELETE CASCADE not null unique ,
    data jsonb not null
);

CREATE TABLE office(
    id bigserial PRIMARY KEY,
    sale_point_name varchar(255) not null,
    sale_point_format varchar(255) not null,
    address varchar(255) not null,
    open_hours jsonb not null,
    rko varchar(150),
    open_hours_individual jsonb not null,
    office_type varchar(150) not null,
    latitude numeric(8, 6) not null,
    longitude numeric(8, 6) not null
);
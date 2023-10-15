CREATE TABLE registration(
    id bigserial primary key,
    office_id bigint references office(id) ON DELETE CASCADE not null,
    client_id bigint not null,
    date_time timestamptz not null,
    identifier int not null,
    service varchar(150) not null
)
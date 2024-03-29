CREATE TABLE b3_info_cases
(
    id              varchar(255) not null primary key,
    customer_id     varchar(255) not null
);

CREATE TABLE b3_info_requests
(
    id          varchar(255) not null primary key,
    case_id     varchar(255) not null references b3_info_cases (id) on delete cascade,
    comment     varchar(255) not null
);

CREATE TABLE c1_info_cases
(
    id              varchar(255) not null primary key,
    customer_id     varchar(255) not null,
    request_comment varchar(255) default null
);

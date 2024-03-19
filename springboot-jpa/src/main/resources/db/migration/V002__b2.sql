CREATE TABLE b2_info_cases
(
    id          text NOT NULL PRIMARY KEY,
    customer_id text NOT NULL
);

CREATE TABLE b2_info_requests
(
    id      text NOT NULL PRIMARY KEY,
    case_id text NOT NULL REFERENCES b2_info_cases (id) ON DELETE CASCADE,
    comment text NOT NULL
);

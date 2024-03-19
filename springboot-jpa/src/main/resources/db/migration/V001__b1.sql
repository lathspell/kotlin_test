CREATE TABLE b1_info_cases
(
    id          uuid NOT NULL PRIMARY KEY,
    customer_id text NOT NULL
);

CREATE TABLE b1_info_requests
(
    id      uuid NOT NULL PRIMARY KEY,
    case_id uuid NOT NULL REFERENCES b1_info_cases (id) ON DELETE CASCADE,
    comment text NOT NULL
);

CREATE TABLE d1_tasks
(
    uuid uuid NOT NULL PRIMARY KEY,
    name text NOT NULL
);

CREATE TABLE d1_info
(
    uuid      uuid NOT NULL PRIMARY KEY,
    task_uuid uuid NOT NULL,
    info      text NOT NULL
);

ALTER TABLE d1_info ADD FOREIGN KEY (task_uuid) REFERENCES d1_tasks (uuid);

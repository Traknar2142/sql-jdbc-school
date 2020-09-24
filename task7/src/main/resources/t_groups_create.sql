CREATE TABLE school.t_groups
(
    group_id integer NOT NULL,
    group_name character(1) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
)

TABLESPACE pg_default;

ALTER TABLE school.t_groups
    OWNER to postgres;
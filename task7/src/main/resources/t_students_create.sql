CREATE TABLE school.t_students
(
    student_id integer NOT NULL,
    group_id character(100) COLLATE pg_catalog."default" NOT NULL,
    first_name character(15) COLLATE pg_catalog."default" NOT NULL,
    last_name character(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_students_pkey PRIMARY KEY (student_id)
)

TABLESPACE pg_default;

ALTER TABLE school.t_students
    OWNER to postgres;
CREATE TABLE school.t_courses
(
    course_id integer NOT NULL,
    course_name character(1) COLLATE pg_catalog."default" NOT NULL,
    course_description character(1) COLLATE pg_catalog."default" NOT NULL
)

TABLESPACE pg_default;

ALTER TABLE school.t_courses
    OWNER to postgres;
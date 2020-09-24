DROP TABLE IF EXISTS postgres.school.t_courses_student;
DROP TABLE IF EXISTS postgres.school.t_groups;
DROP TABLE IF EXISTS postgres.school.t_courses;
DROP TABLE IF EXISTS postgres.school.t_students;


CREATE TABLE school.t_courses
(
    course_id integer NOT NULL,
    course_name character(20) COLLATE pg_catalog."default" NOT NULL,
    course_description character(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_courses_pkey PRIMARY KEY (course_id)
)

TABLESPACE pg_default;

ALTER TABLE school.t_courses
    OWNER to postgres;
    
CREATE TABLE school.t_groups
(
    group_id integer NOT NULL,
    group_name character(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
)

TABLESPACE pg_default;

ALTER TABLE school.t_groups
    OWNER to postgres;
    
CREATE TABLE school.t_students
(
    student_id integer NOT NULL,
    group_id character(100) COLLATE pg_catalog."default" NOT NULL,
    first_name character(20) COLLATE pg_catalog."default" NOT NULL,
    last_name character(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_students_pkey PRIMARY KEY (student_id)
)

TABLESPACE pg_default;

ALTER TABLE school.t_students
    OWNER to postgres;
    
CREATE TABLE school.t_courses_student
(
    course_id integer  NOT NULL,
    student_id integer NOT NULL,
    FOREIGN KEY (course_id) REFERENCES school.t_courses(course_id),
    FOREIGN KEY (student_id) REFERENCES school.t_students(student_id)
)
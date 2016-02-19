CREATE TABLE batch_job_execution
(
  job_execution_id           BIGINT PRIMARY KEY NOT NULL,
  version                    BIGINT,
  job_instance_id            BIGINT             NOT NULL,
  create_time                TIMESTAMP          NOT NULL,
  start_time                 TIMESTAMP,
  end_time                   TIMESTAMP,
  status                     VARCHAR(10),
  exit_code                  VARCHAR(2500),
  exit_message               VARCHAR(2500),
  last_updated               TIMESTAMP,
  job_configuration_location VARCHAR(2500)
);
CREATE TABLE batch_job_execution_context
(
  job_execution_id   BIGINT PRIMARY KEY NOT NULL,
  short_context      VARCHAR(2500)      NOT NULL,
  serialized_context TEXT
);
CREATE TABLE batch_job_execution_params
(
  job_execution_id BIGINT       NOT NULL,
  type_cd          VARCHAR(6)   NOT NULL,
  key_name         VARCHAR(100) NOT NULL,
  string_val       VARCHAR(250),
  date_val         TIMESTAMP,
  long_val         BIGINT,
  double_val       DOUBLE PRECISION,
  identifying      CHAR         NOT NULL
);
CREATE TABLE batch_job_instance
(
  job_instance_id BIGINT PRIMARY KEY NOT NULL,
  version         BIGINT,
  job_name        VARCHAR(100)       NOT NULL,
  job_key         VARCHAR(32)        NOT NULL
);
CREATE TABLE batch_step_execution
(
  step_execution_id  BIGINT PRIMARY KEY NOT NULL,
  version            BIGINT             NOT NULL,
  step_name          VARCHAR(100)       NOT NULL,
  job_execution_id   BIGINT             NOT NULL,
  start_time         TIMESTAMP          NOT NULL,
  end_time           TIMESTAMP,
  status             VARCHAR(10),
  commit_count       BIGINT,
  read_count         BIGINT,
  filter_count       BIGINT,
  write_count        BIGINT,
  read_skip_count    BIGINT,
  write_skip_count   BIGINT,
  process_skip_count BIGINT,
  rollback_count     BIGINT,
  exit_code          VARCHAR(2500),
  exit_message       VARCHAR(2500),
  last_updated       TIMESTAMP
);
CREATE TABLE batch_step_execution_context
(
  step_execution_id  BIGINT PRIMARY KEY NOT NULL,
  short_context      VARCHAR(2500)      NOT NULL,
  serialized_context TEXT
);
ALTER TABLE batch_job_execution ADD FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance (job_instance_id);
ALTER TABLE batch_job_execution_context ADD FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id);
ALTER TABLE batch_job_execution_params ADD FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id);
CREATE UNIQUE INDEX job_inst_un ON batch_job_instance (job_name, job_key);
ALTER TABLE batch_step_execution ADD FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id);
ALTER TABLE batch_step_execution_context ADD FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution (step_execution_id);


CREATE SEQUENCE batch_job_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 6
CACHE 1;
ALTER TABLE batch_job_seq
OWNER TO budget;


CREATE SEQUENCE batch_job_execution_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 6
CACHE 1;
ALTER TABLE batch_job_execution_seq
OWNER TO budget;


CREATE SEQUENCE batch_step_execution_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 6
CACHE 1;
ALTER TABLE batch_step_execution_seq
OWNER TO budget;

-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA scheduler;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE scheduler.jobs (
  id                 VARCHAR(100)  NOT NULL,
  name               VARCHAR(100)  NOT NULL,
  scheduling_pattern VARCHAR(100)  NOT NULL,
  job_class          VARCHAR(1000) NOT NULL,
  enabled            BOOLEAN       NOT NULL,
  status             INTEGER       NOT NULL,
  execution_attempts INTEGER,
  lock_name          VARCHAR(100),
  last_executed      TIMESTAMP,
  next_execution     TIMESTAMP,
  created            TIMESTAMP     NOT NULL,
  updated            TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN scheduler.jobs.id IS 'The ID uniquely identifying the job';

COMMENT ON COLUMN scheduler.jobs.name IS 'The name of the job';

COMMENT ON COLUMN scheduler.jobs.scheduling_pattern IS 'The cron-style scheduling pattern for the job';

COMMENT ON COLUMN scheduler.jobs.job_class IS 'The fully qualified name of the Java class that implements the job';

COMMENT ON COLUMN scheduler.jobs.enabled IS 'Is the job enabled for execution';

COMMENT ON COLUMN scheduler.jobs.status IS 'The status of the job';

COMMENT ON COLUMN scheduler.jobs.execution_attempts IS 'The number of times the current execution of the job has been attempted';

COMMENT ON COLUMN scheduler.jobs.lock_name IS 'The name of the entity that has locked the job for execution';

COMMENT ON COLUMN scheduler.jobs.last_executed IS 'The date and time the job was last executed';

COMMENT ON COLUMN scheduler.jobs.next_execution IS 'The date and time the job will next be executed';

COMMENT ON COLUMN scheduler.jobs.created IS 'The date and time the job was created';

COMMENT ON COLUMN scheduler.jobs.updated IS 'The date and time the job was last updated';


CREATE TABLE scheduler.job_parameters (
  job_id  VARCHAR(100)  NOT NULL,
  name    VARCHAR(100)  NOT NULL,
  value   VARCHAR(4000) NOT NULL,
  created TIMESTAMP     NOT NULL,
  updated TIMESTAMP,

  PRIMARY KEY (job_id, name),
  CONSTRAINT job_parameters_job_fk FOREIGN KEY (job_id) REFERENCES scheduler.jobs(id) ON DELETE CASCADE
);

CREATE INDEX job_parameters_job_id_ix ON scheduler.job_parameters(job_id);

CREATE INDEX job_parameters_name_ix ON scheduler.job_parameters(name);

COMMENT ON COLUMN scheduler.job_parameters.job_id IS 'The ID uniquely identifying the job';

COMMENT ON COLUMN scheduler.job_parameters.name IS 'The name of the job parameter';

COMMENT ON COLUMN scheduler.job_parameters.value IS 'The value of the job parameter';

COMMENT ON COLUMN scheduler.job_parameters.created IS 'The date and time the job parameter was created';

COMMENT ON COLUMN scheduler.job_parameters.updated IS 'The date and time the job parameter was last updated';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------



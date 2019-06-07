-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA error;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE error.error_reports (
  id                  UUID          NOT NULL,
  application_id      VARCHAR(100)  NOT NULL,
  application_version VARCHAR(50)   NOT NULL,
  description         VARCHAR(4000) NOT NULL,
  detail              VARCHAR(4000) NOT NULL,
  created             TIMESTAMP     NOT NULL,
  who                 VARCHAR(1000),
  device_id           VARCHAR(50),
  feedback            VARCHAR(4000),
  data                BLOB,

  PRIMARY KEY (id)
);

CREATE INDEX error_reports_application_id_ix ON error.error_reports(application_id);

CREATE INDEX error_reports_created_ix ON error.error_reports(created);

CREATE INDEX error_reports_who_ix ON error.error_reports(who);

COMMENT ON COLUMN error.error_reports.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN error.error_reports.application_id IS 'The ID used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN error.error_reports.application_version IS 'The version of the application that generated the error report';

COMMENT ON COLUMN error.error_reports.description IS 'The description of the error';

COMMENT ON COLUMN error.error_reports.detail IS 'The error detail e.g. a stack trace';

COMMENT ON COLUMN error.error_reports.created IS 'The date and time the error report was created';

COMMENT ON COLUMN error.error_reports.who IS 'The optional username identifying the user associated with the error report';

COMMENT ON COLUMN error.error_reports.device_id IS 'The optional ID used to uniquely identify the device the error report originated from';

COMMENT ON COLUMN error.error_reports.feedback IS 'The optional feedback provided by the user for the error';

COMMENT ON COLUMN error.error_reports.data IS 'The optional data associated with the error report';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reporting;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reporting.report_definitions (
  id       VARCHAR(100) NOT NULL,
  name     VARCHAR(100) NOT NULL,
  template BLOB         NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN reporting.report_definitions.id IS 'The ID uniquely identifying the report definition';

COMMENT ON COLUMN reporting.report_definitions.name IS 'The name of the report definition';

COMMENT ON COLUMN reporting.report_definitions.template IS 'The JasperReports template for the report definition';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------


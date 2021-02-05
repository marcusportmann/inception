-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reporting;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reporting.report_definitions
(
    id       VARCHAR(100) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    template BLOB         NOT NULL,
    created  TIMESTAMP    NOT NULL,
    updated  TIMESTAMP,

    PRIMARY KEY (id)
);

COMMENT
ON COLUMN reporting.report_definitions.id IS 'The ID for the report definition';

COMMENT
ON COLUMN reporting.report_definitions.name IS 'The name of the report definition';

COMMENT
ON COLUMN reporting.report_definitions.template IS 'The JasperReports template for the report definition';

COMMENT
ON COLUMN reporting.report_definitions.created IS 'The date and time the report definition was created';

COMMENT
ON COLUMN reporting.report_definitions.updated IS 'The date and time the report definition was last updated';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------


-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA configuration;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE configuration.configuration (
  key         VARCHAR(100)  NOT NULL,
  value       VARCHAR(4000) NOT NULL,
  description VARCHAR(100)  NOT NULL,
  created     TIMESTAMP     NOT NULL,
  updated     TIMESTAMP,

  PRIMARY KEY (key)
);

COMMENT ON COLUMN configuration.configuration.key IS 'The key for the configuration value';

COMMENT ON COLUMN configuration.configuration.value IS 'The value for the configuration value';

COMMENT ON COLUMN configuration.configuration.description IS 'The description for the configuration value';

COMMENT ON COLUMN configuration.configuration.created IS 'The date and time the configuration was created';

COMMENT ON COLUMN configuration.configuration.updated IS 'The date and time the configuration was last updated';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------





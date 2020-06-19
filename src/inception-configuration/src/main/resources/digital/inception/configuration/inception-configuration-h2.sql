-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA configuration;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE configuration.configuration (
  key         VARCHAR(100) NOT NULL,
  value       VARCHAR(4000) NOT NULL,
  description VARCHAR(100) NOT NULL,

  PRIMARY KEY (key)
);

COMMENT ON COLUMN configuration.configuration.key IS 'The key uniquely identifying the configuration value';

COMMENT ON COLUMN configuration.configuration.value IS 'The value for the configuration value';

COMMENT ON COLUMN configuration.configuration.description IS 'The description for the configuration value';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------





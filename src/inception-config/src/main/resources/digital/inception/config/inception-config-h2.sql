-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA config;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE config.config (
  key         VARCHAR(100)  NOT NULL,
  value       VARCHAR(4000) NOT NULL,
  description VARCHAR(100)  NOT NULL,
  created     TIMESTAMP     NOT NULL,
  updated     TIMESTAMP,

  PRIMARY KEY (key)
);

COMMENT ON COLUMN config.config.key IS 'The key for the config';

COMMENT ON COLUMN config.config.value IS 'The value for the config';

COMMENT ON COLUMN config.config.description IS 'The description for the config';

COMMENT ON COLUMN config.config.created IS 'The date and time the config was created';

COMMENT ON COLUMN config.config.updated IS 'The date and time the config was last updated';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------





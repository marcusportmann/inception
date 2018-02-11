-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA IDGENERATOR;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE IDGENERATOR.IDGENERATOR (
  NAME     VARCHAR(4000) NOT NULL,
  CURRENT  BIGINT DEFAULT 0,

  PRIMARY KEY (NAME)
);

COMMENT ON COLUMN IDGENERATOR.IDGENERATOR.NAME
IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN IDGENERATOR.IDGENERATOR.CURRENT
IS 'The current ID for the type';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

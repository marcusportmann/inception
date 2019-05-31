-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA idgenerator;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE idgenerator.idgenerator (
  name    VARCHAR(4000) NOT NULL,
  current BIGINT DEFAULT 0,

  PRIMARY KEY (name)
);

COMMENT ON COLUMN idgenerator.idgenerator.name IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN idgenerator.idgenerator.current IS 'The current ID for the type';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA TEST;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE TEST.TEST_DATA (
  ID     VARCHAR(4000) NOT NULL,
  NAME   VARCHAR(4000) NOT NULL,
  VALUE  VARCHAR(4000) NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN TEST.TEST_DATA.ID
IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN TEST.TEST_DATA.NAME
IS 'The name for the test data';

COMMENT ON COLUMN TEST.TEST_DATA.VALUE
IS 'The value for the test data';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');





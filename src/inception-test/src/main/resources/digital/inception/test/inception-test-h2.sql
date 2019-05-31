-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA test;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE test.test_data (
  id    VARCHAR(4000) NOT NULL,
  name  VARCHAR(4000) NOT NULL,
  value VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN test.test_data.id IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN test.test_data.name IS 'The name for the test data';

COMMENT ON COLUMN test.test_data.value IS 'The value for the test data';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO test.test_data (id, name, value)
  VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO test.test_data (id, name, value)
  VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO test.test_data (id, name, value)
  VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO test.test_data (id, name, value)
  VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO test.test_data (id, name, value)
  VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO test.test_data (id, name, value)
  VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO test.test_data (id, name, value)
  VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO test.test_data (id, name, value)
  VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO test.test_data (id, name, value)
  VALUES (9, 'Sample Name 9', 'Sample Value 9');





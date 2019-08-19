-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA codes;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE codes.code_categories (
  id      VARCHAR(100) NOT NULL,
  name    VARCHAR(100) NOT NULL,
  data    CLOB,
  updated TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN codes.code_categories.id IS 'The ID used to uniquely identify the code category';

COMMENT ON COLUMN codes.code_categories.name IS 'The name of the code category';

COMMENT ON COLUMN codes.code_categories.data IS 'The code data for the code category';

COMMENT ON COLUMN codes.code_categories.updated IS 'The date and time the code category was last updated';


CREATE TABLE codes.codes (
  id               VARCHAR(100)  NOT NULL,
  code_category_id VARCHAR(100)  NOT NULL,
  name             VARCHAR(100)  NOT NULL,
  value            VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id, code_category_id),
  CONSTRAINT codes_code_category_fk FOREIGN KEY (code_category_id) REFERENCES codes.code_categories(id) ON DELETE CASCADE
);

CREATE INDEX codes_code_category_id_ix ON codes.codes(code_category_id);

COMMENT ON COLUMN codes.codes.id IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN codes.codes.code_category_id IS 'The ID used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN codes.codes.name IS 'The name of the code';

COMMENT ON COLUMN codes.codes.value IS 'The value for the code';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
-- See: https://en.wikipedia.org/wiki/ISO/IEC_5218
CREATE TABLE reference.gender (
  code        VARCHAR(10)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  name        VARCHAR(20)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

COMMENT ON COLUMN reference.gender.code IS 'The code for the gender';

COMMENT ON COLUMN reference.gender.locale_id IS 'The Unicode locale identifier';

COMMENT ON COLUMN reference.gender.name IS 'The name of the gender';

COMMENT ON COLUMN reference.gender.description IS 'The description for the gender';





-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('0', 'en-US', 'Not known', 'Not known');
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('1', 'en-US', 'Male', 'Male');
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('2', 'en-US', 'Female', 'Female');
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('3', 'en-US', 'Transgender', 'Transgender');
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('4', 'en-US', 'Non-binary', 'Non-binary');
INSERT INTO reference.gender (code, locale_id, name, description)
  VALUES ('9', 'en-US', 'Not applicable', 'Not applicable');



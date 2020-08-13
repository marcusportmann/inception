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
  language    VARCHAR(3)   NOT NULL DEFAULT '',
  country     VARCHAR(3)   NOT NULL DEFAULT '',
  name        VARCHAR(20)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, language, country)
);

COMMENT ON COLUMN reference.gender.code IS 'The code for the gender';

COMMENT ON COLUMN reference.gender.language IS 'The ISO 639 alpha-2 or alpha-3 language code';

COMMENT ON COLUMN reference.gender.country IS 'The ISO 3166-1 alpha-2 country code or UN M.49 numeric-3 area code for the language variant';

COMMENT ON COLUMN reference.gender.name IS 'The name of the gender';

COMMENT ON COLUMN reference.gender.description IS 'The description for the gender';










-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('0', 'en', '', 'Not known', 'Not known');
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('1', 'en', '', 'Male', 'Male');
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('2', 'en', '', 'Female', 'Female');
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('3', 'en', '', 'Transgender', 'Transgender');
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('4', 'en', '', 'Non-binary', 'Non-binary');
INSERT INTO reference.gender (code, language, country, name, description)
  VALUES ('9', 'en', '', 'Not applicable', 'Not applicable');



-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reference.countries (
  code            CHAR(2)      NOT NULL,
  iso3_code       CHAR(3)      NOT NULL,
  locale_id       VARCHAR(10)  NOT NULL,
  sort_index      INTEGER      NOT NULL,
  name            VARCHAR(50)  NOT NULL,
  short_name      VARCHAR(30)  NOT NULL,
  description     VARCHAR(200) NOT NULL DEFAULT '',
  sovereign_state CHAR(2)      NOT NULL,
  nationality     VARCHAR(50)  NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX countries_iso3_code_ix ON reference.countries(iso3_code);

CREATE INDEX countries_locale_id_ix ON reference.countries(locale_id);

COMMENT ON COLUMN reference.countries.code IS 'The ISO 3166-1 alpha-2 code for the country';

COMMENT ON COLUMN reference.countries.iso3_code IS 'The ISO 3166-1 alpha-3 code for the country';

COMMENT ON COLUMN reference.countries.locale_id IS 'The Unicode locale identifier for the country';

COMMENT ON COLUMN reference.countries.sort_index IS 'The sort index for the country';

COMMENT ON COLUMN reference.countries.name IS 'The name of the country';

COMMENT ON COLUMN reference.countries.short_name IS 'The short name for the country';

COMMENT ON COLUMN reference.countries.description IS 'The description for the country';

COMMENT ON COLUMN reference.countries.sovereign_state IS 'The ISO 3166-1 alpha-2 code for the sovereign state the country is associated with';

COMMENT ON COLUMN reference.countries.nationality IS 'The nationality for the country';


CREATE TABLE reference.languages (
  code        VARCHAR(2)  NOT NULL,
  iso3_code   CHAR(3)      NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  short_name  VARCHAR(30)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX languages_locale_id_ix ON reference.languages(locale_id);

COMMENT ON COLUMN reference.languages.code IS 'The ISO 639-1 alpha-2 code for the language';

COMMENT ON COLUMN reference.languages.iso3_code IS 'The ISO 639-2 alpha-3 code for the language';

COMMENT ON COLUMN reference.languages.locale_id IS 'The Unicode locale identifier for the language';

COMMENT ON COLUMN reference.languages.sort_index IS 'The sort index for the language';

COMMENT ON COLUMN reference.languages.name IS 'The name of the language';

COMMENT ON COLUMN reference.languages.short_name IS 'The short name for the language';

COMMENT ON COLUMN reference.languages.description IS 'The description for the language';


CREATE TABLE reference.measurement_systems (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX measurement_systems_locale_id_ix ON reference.measurement_systems(locale_id);

COMMENT ON COLUMN reference.measurement_systems.code IS 'The code for the measurement system';

COMMENT ON COLUMN reference.measurement_systems.locale_id IS 'The Unicode locale identifier for the measurement system';

COMMENT ON COLUMN reference.measurement_systems.sort_index IS 'The sort index for the measurement system';

COMMENT ON COLUMN reference.measurement_systems.name IS 'The name of the measurement system';

COMMENT ON COLUMN reference.measurement_systems.description IS 'The description for the measurement system';


CREATE TABLE reference.measurement_unit_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX measurement_unit_types_locale_id_ix ON reference.measurement_unit_types(locale_id);

COMMENT ON COLUMN reference.measurement_unit_types.code IS 'The code for the measurement unit type';

COMMENT ON COLUMN reference.measurement_unit_types.locale_id IS 'The Unicode locale identifier for the measurement unit type';

COMMENT ON COLUMN reference.measurement_unit_types.sort_index IS 'The sort index for the measurement unit type';

COMMENT ON COLUMN reference.measurement_unit_types.name IS 'The name of the measurement unit type';

COMMENT ON COLUMN reference.measurement_unit_types.description IS 'The description for the measurement unit type';


CREATE TABLE reference.measurement_units (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',
  system      VARCHAR(30)  NOT NULL,
  type        VARCHAR(30)  NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT measurement_units_measurement_system_fk FOREIGN KEY (system, locale_id) REFERENCES reference.measurement_systems(code, locale_id) ON DELETE CASCADE,
  CONSTRAINT measurement_units_measurement_unit_type_fk FOREIGN KEY (type, locale_id) REFERENCES reference.measurement_unit_types(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX measurement_units_locale_id_ix ON reference.measurement_units(locale_id);

COMMENT ON COLUMN reference.measurement_units.code IS 'The code for the measurement unit';

COMMENT ON COLUMN reference.measurement_units.locale_id IS 'The Unicode locale identifier for the measurement unit';

COMMENT ON COLUMN reference.measurement_units.sort_index IS 'The sort index for the measurement unit';

COMMENT ON COLUMN reference.measurement_units.name IS 'The name of the measurement unit';

COMMENT ON COLUMN reference.measurement_units.description IS 'The description for the measurement unit';

COMMENT ON COLUMN reference.measurement_units.system IS 'The code for the measurement system';

COMMENT ON COLUMN reference.measurement_units.type IS 'The code for the measurement unit type';


CREATE TABLE reference.regions (
  country     CHAR(2)      NOT NULL,
  code        VARCHAR(3)   NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (country, code, locale_id),
  CONSTRAINT regions_country_fk FOREIGN KEY (country, locale_id) REFERENCES reference.countries(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX regions_country_ix ON reference.regions(country);

CREATE INDEX regions_locale_id_ix ON reference.regions(locale_id);

COMMENT ON COLUMN reference.regions.country IS 'The ISO 3166-1 alpha-2 code for the country the region is associated with';

COMMENT ON COLUMN reference.regions.code IS 'The ISO 3166-2 subdivision code for the region';

COMMENT ON COLUMN reference.regions.locale_id IS 'The Unicode locale identifier for the region';

COMMENT ON COLUMN reference.regions.sort_index IS 'The sort index for the region';

COMMENT ON COLUMN reference.regions.name IS 'The name of the region';

COMMENT ON COLUMN reference.regions.description IS 'The description for the region';




-- ADD FOREIGN KEYS
--
-- FINISH SIC CODES

-- NOTE: SIC CODES NEED TO HAVE: AN ASSOCIATED COUNTRY (OR MANY COUNTRIES E.G. EU?), DIVISION, MAJOR GROUP, INDUSTYR GROUP (https://en.wikipedia.org/wiki/Standard_Industrial_Classification)



-- CREATE TABLE reference.standard_industry_codes (
--   code        VARCHAR(30)  NOT NULL,
--   locale_id   VARCHAR(10)  NOT NULL,
--   sort_index  INTEGER      NOT NULL,
--   name        VARCHAR(50)  NOT NULL,
--   description VARCHAR(200) NOT NULL DEFAULT '',
--
--   PRIMARY KEY (code, locale_id)
-- );
--
-- CREATE INDEX standard_industry_codes_locale_id_ix ON reference.standard_industry_codes(locale_id);
--
-- COMMENT ON COLUMN reference.standard_industry_codes.code IS 'The code for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.locale_id IS 'The Unicode locale identifier for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.sort_index IS 'The sort index for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.name IS 'The name of the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.description IS 'The description for the standard industry code';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'AFG', 'en-US', 1, 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'ALB', 'en-US', 2, 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'DZA', 'en-US', 3, 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'ASM', 'en-US', 4, 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'AND', 'en-US', 5, 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'AGO', 'en-US', 6, 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'AIA', 'en-US', 7, 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'ATA', 'en-US', 8, 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'ATG', 'en-US', 9, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'ARG', 'en-US', 10, 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'ARM', 'en-US', 11, 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'ABW', 'en-US', 12, 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'AUS', 'en-US', 13, 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'AUT', 'en-US', 14, 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'AZE', 'en-US', 15, 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'BHS', 'en-US', 16, 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'BHR', 'en-US', 17, 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'BGD', 'en-US', 18, 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'BRB', 'en-US', 19, 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'BLR', 'en-US', 20, ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'BEL', 'en-US', 21, 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'BLZ', 'en-US', 22, 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'BEN', 'en-US', 23, 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'BMU', 'en-US', 24, 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'BTN', 'en-US', 25, 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'BOL', 'en-US', 26, 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'BES', 'en-US', 27, 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'BIH', 'en-US', 28, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'BWA', 'en-US', 29, 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'BVT', 'en-US', 30, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'BRA', 'en-US', 31, 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'BRN', 'en-US', 32, 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'BGR', 'en-US', 33, 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'BFA', 'en-US', 34, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'BDI', 'en-US', 35, 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'CPV', 'en-US', 36, 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'KHM', 'en-US', 37, 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'CMR', 'en-US', 38, 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'CAN', 'en-US', 39, 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'CYM', 'en-US', 40, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'CAF', 'en-US', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'TCD', 'en-US', 42, 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'CHL', 'en-US', 43, 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'CHN', 'en-US', 44, 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'CXR', 'en-US', 45, 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'CCK', 'en-US', 46, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'COL', 'en-US', 47, 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'COM', 'en-US', 48, 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'COG', 'en-US', 49, 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'COD', 'en-US', 50, 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'COK', 'en-US', 51, 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'CRI', 'en-US', 52, 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'HRV', 'en-US', 53, 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'CUB', 'en-US', 54, 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'CUW', 'en-US', 55, 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'CYP', 'en-US', 56, 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'CZE', 'en-US', 57, 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'CIV', 'en-US', 58, 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'DNK', 'en-US', 59, 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'DJI', 'en-US', 60, 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'DMA', 'en-US', 61, ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'DOM', 'en-US', 62, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'ECU', 'en-US', 63, 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'EGY', 'en-US', 64, 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'SLV', 'en-US', 65, 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'GNQ', 'en-US', 66, 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'ERI', 'en-US', 67, 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'EST', 'en-US', 68, 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'ETH', 'en-US', 69, 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'FLK', 'en-US', 70, 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'FRO', 'en-US', 71, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'FJI', 'en-US', 72, 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'FIN', 'en-US', 73, 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'FRA', 'en-US', 74, 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'GUF', 'en-US', 75, 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'PYF', 'en-US', 76, 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'ATF', 'en-US', 77, 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'GAB', 'en-US', 78, 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'GMB', 'en-US', 79, 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'GEO', 'en-US', 80, 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'DEU', 'en-US', 81, 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'GHA', 'en-US', 82, 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'GIB', 'en-US', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'GRC', 'en-US', 84, 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'GRL', 'en-US', 85, 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'GRD', 'en-US', 86, 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'GLP', 'en-US', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'GUM', 'en-US', 88, 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'GTM', 'en-US', 89, 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'GGY', 'en-US', 90, 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'GIN', 'en-US', 91, 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'GNB', 'en-US', 92, 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'GUY', 'en-US', 93, 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'HTI', 'en-US', 94, 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'VAT', 'en-US', 95, 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'HND', 'en-US', 96, 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'HKG', 'en-US', 97, 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'HUN', 'en-US', 98, 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'ISL', 'en-US', 99, 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'IND', 'en-US', 100, 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'IDN', 'en-US', 101, 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'IRN', 'en-US', 102, 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'IRQ', 'en-US', 103, 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'IRL', 'en-US', 104, 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'IMN', 'en-US', 105, 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'ISR', 'en-US', 106, 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'ITA', 'en-US', 107, 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'JAM', 'en-US', 108, 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'JPN', 'en-US', 109, 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'JEY', 'en-US', 110, 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'JOR', 'en-US', 111, 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'KAZ', 'en-US', 112, 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'KEN', 'en-US', 113, 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'KIR', 'en-US', 114, 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'PRK', 'en-US', 115, 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'KOR', 'en-US', 116, 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'KWT', 'en-US', 117, 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'KGZ', 'en-US', 118, 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'LAO', 'en-US', 119, 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'LVA', 'en-US', 120, 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'LBN', 'en-US', 121, 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'LSO', 'en-US', 122, 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'LBR', 'en-US', 123, 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'LBY', 'en-US', 124, 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'LIE', 'en-US', 125, 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'LTU', 'en-US', 126, 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'LUX', 'en-US', 127, 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'MAC', 'en-US', 128, 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'MDG', 'en-US', 129, 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'MWI', 'en-US', 130, 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'MYS', 'en-US', 131, 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'MDV', 'en-US', 132, 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'MLI', 'en-US', 133, 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'MLT', 'en-US', 134, 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'MHL', 'en-US', 135, 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'MTQ', 'en-US', 136, 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'MRT', 'en-US', 137, 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'MUS', 'en-US', 138, 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'MYT', 'en-US', 139, 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'MEX', 'en-US', 140, 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'FSM', 'en-US', 141, 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'MDA', 'en-US', 142, 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'MCO', 'en-US', 143, ' Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'MNG', 'en-US', 144, 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'MNE', 'en-US', 145, 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'MSR', 'en-US', 146, 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'MAR', 'en-US', 147, 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'MOZ', 'en-US', 148, 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'MMR', 'en-US', 149, 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'NAM', 'en-US', 150, 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'NRU', 'en-US', 151, 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'NPL', 'en-US', 152, 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'NLD', 'en-US', 153, 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'NCL', 'en-US', 154, 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'NZL', 'en-US', 155, 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'NIC', 'en-US', 156, 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'NER', 'en-US', 157, 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'NGA', 'en-US', 158, 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'NIU', 'en-US', 159, 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'NFK', 'en-US', 160, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'MNP', 'en-US', 161, 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'NOR', 'en-US', 162, 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'OMN', 'en-US', 163, 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'PAK', 'en-US', 164, 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'PLW', 'en-US', 165, 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'PSE', 'en-US', 166, 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'PAN', 'en-US', 167, 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'PNG', 'en-US', 168, 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'PRY', 'en-US', 169, 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'PER', 'en-US', 170, 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'PHL', 'en-US', 171, 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'PCN', 'en-US', 172, 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'POL', 'en-US', 173, 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'PRT', 'en-US', 174, 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'PRI', 'en-US', 175, 'Commonwealth of Puerto Rico', 'PRT Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'QAT', 'en-US', 176, 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'MKD', 'en-US', 177, 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'ROU', 'en-US', 178, 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'RUS', 'en-US', 179, 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'RWA', 'en-US', 180, 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'REU', 'en-US', 181, 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'BLM', 'en-US', 182, 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'KNA', 'en-US', 183, 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'LCA', 'en-US', 184, 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'MAF', 'en-US', 185, 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'SPM', 'en-US', 186, 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'VCT', 'en-US', 187, 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'WSM', 'en-US', 188, 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'SMR', 'en-US', 189, 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'STP', 'en-US', 190, 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'SAU', 'en-US', 191, 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'SEN', 'en-US', 192, 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'SRB', 'en-US', 193, 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'SYC', 'en-US', 194, 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'SLE', 'en-US', 195, 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'SGP', 'en-US', 196, 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'SXM', 'en-US', 197, 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'SVK', 'en-US', 198, 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'SVN', 'en-US', 199, 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'SLB', 'en-US', 200, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'SOM', 'en-US', 201, 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'ZAF', 'en-US', 202, 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'SSD', 'en-US', 203, 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'ESP', 'en-US', 204, 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'LKA', 'en-US', 205, 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'SDN', 'en-US', 206, 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'SUR', 'en-US', 207, 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'SJM', 'en-US', 208, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'SWZ', 'en-US', 209, 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'SWE', 'en-US', 210, 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'CHE', 'en-US', 211, 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'SYR', 'en-US', 212, 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'TWN', 'en-US', 213, 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'TJK', 'en-US', 214, 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'TZA', 'en-US', 215, 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'THA', 'en-US', 216, 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'TLS', 'en-US', 217, 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'TGO', 'en-US', 218, 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'TKL', 'en-US', 219, 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'TON', 'en-US', 220, 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'TTO', 'en-US', 221, 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'TUN', 'en-US', 222, 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'TUR', 'en-US', 223, 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'TKM', 'en-US', 224, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'TCA', 'en-US', 225, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'TUV', 'en-US', 226, 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'UGA', 'en-US', 227, 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'UKR', 'en-US', 228, 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'ARE', 'en-US', 229, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'GBR', 'en-US', 230, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'UMI', 'en-US', 231, 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'USA', 'en-US', 232, 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'URY', 'en-US', 233, 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'UZB', 'en-US', 234, 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'VUT', 'en-US', 235, 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'VEN', 'en-US', 236, 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'VNM', 'en-US', 237, 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'VGB', 'en-US', 238, 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'VIR', 'en-US', 239, 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'WLF', 'en-US', 240, 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'ESH', 'en-US', 241, 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'YEM', 'en-US', 242, 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'ZMB', 'en-US', 243, 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'ZWE', 'en-US', 244, 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'ALA', 'en-US', 245, 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZZ', 'ZZZ', 'en-US', 999, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');

INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'AFG', 'en-ZA', 1, 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'ALB', 'en-ZA', 2, 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'DZA', 'en-ZA', 3, 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'ASM', 'en-ZA', 4, 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'AND', 'en-ZA', 5, 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'AGO', 'en-ZA', 6, 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'AIA', 'en-ZA', 7, 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'ATA', 'en-ZA', 8, 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'ATG', 'en-ZA', 9, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'ARG', 'en-ZA', 10, 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'ARM', 'en-ZA', 11, 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'ABW', 'en-ZA', 12, 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'AUS', 'en-ZA', 13, 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'AUT', 'en-ZA', 14, 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'AZE', 'en-ZA', 15, 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'BHS', 'en-ZA', 16, 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'BHR', 'en-ZA', 17, 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'BGD', 'en-ZA', 18, 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'BRB', 'en-ZA', 19, 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'BLR', 'en-ZA', 20, ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'BEL', 'en-ZA', 21, 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'BLZ', 'en-ZA', 22, 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'BEN', 'en-ZA', 23, 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'BMU', 'en-ZA', 24, 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'BTN', 'en-ZA', 25, 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'BOL', 'en-ZA', 26, 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'BES', 'en-ZA', 27, 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'BIH', 'en-ZA', 28, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'BWA', 'en-ZA', 29, 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'BVT', 'en-ZA', 30, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'BRA', 'en-ZA', 31, 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'BRN', 'en-ZA', 32, 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'BGR', 'en-ZA', 33, 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'BFA', 'en-ZA', 34, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'BDI', 'en-ZA', 35, 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'CPV', 'en-ZA', 36, 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'KHM', 'en-ZA', 37, 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'CMR', 'en-ZA', 38, 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'CAN', 'en-ZA', 39, 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'CYM', 'en-ZA', 40, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'CAF', 'en-ZA', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'TCD', 'en-ZA', 42, 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'CHL', 'en-ZA', 43, 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'CHN', 'en-ZA', 44, 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'CXR', 'en-ZA', 45, 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'CCK', 'en-ZA', 46, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'COL', 'en-ZA', 47, 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'COM', 'en-ZA', 48, 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'COG', 'en-ZA', 49, 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'COD', 'en-ZA', 50, 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'COK', 'en-ZA', 51, 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'CRI', 'en-ZA', 52, 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'HRV', 'en-ZA', 53, 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'CUB', 'en-ZA', 54, 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'CUW', 'en-ZA', 55, 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'CYP', 'en-ZA', 56, 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'CZE', 'en-ZA', 57, 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'CIV', 'en-ZA', 58, 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'DNK', 'en-ZA', 59, 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'DJI', 'en-ZA', 60, 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'DMA', 'en-ZA', 61, ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'DOM', 'en-ZA', 62, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'ECU', 'en-ZA', 63, 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'EGY', 'en-ZA', 64, 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'SLV', 'en-ZA', 65, 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'GNQ', 'en-ZA', 66, 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'ERI', 'en-ZA', 67, 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'EST', 'en-ZA', 68, 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'ETH', 'en-ZA', 69, 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'FLK', 'en-ZA', 70, 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'FRO', 'en-ZA', 71, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'FJI', 'en-ZA', 72, 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'FIN', 'en-ZA', 73, 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'FRA', 'en-ZA', 74, 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'GUF', 'en-ZA', 75, 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'PYF', 'en-ZA', 76, 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'ATF', 'en-ZA', 77, 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'GAB', 'en-ZA', 78, 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'GMB', 'en-ZA', 79, 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'GEO', 'en-ZA', 80, 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'DEU', 'en-ZA', 81, 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'GHA', 'en-ZA', 82, 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'GIB', 'en-ZA', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'GRC', 'en-ZA', 84, 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'GRL', 'en-ZA', 85, 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'GRD', 'en-ZA', 86, 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'GLP', 'en-ZA', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'GUM', 'en-ZA', 88, 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'GTM', 'en-ZA', 89, 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'GGY', 'en-ZA', 90, 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'GIN', 'en-ZA', 91, 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'GNB', 'en-ZA', 92, 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'GUY', 'en-ZA', 93, 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'HTI', 'en-ZA', 94, 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'VAT', 'en-ZA', 95, 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'HND', 'en-ZA', 96, 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'HKG', 'en-ZA', 97, 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'HUN', 'en-ZA', 98, 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'ISL', 'en-ZA', 99, 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'IND', 'en-ZA', 100, 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'IDN', 'en-ZA', 101, 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'IRN', 'en-ZA', 102, 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'IRQ', 'en-ZA', 103, 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'IRL', 'en-ZA', 104, 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'IMN', 'en-ZA', 105, 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'ISR', 'en-ZA', 106, 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'ITA', 'en-ZA', 107, 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'JAM', 'en-ZA', 108, 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'JPN', 'en-ZA', 109, 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'JEY', 'en-ZA', 110, 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'JOR', 'en-ZA', 111, 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'KAZ', 'en-ZA', 112, 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'KEN', 'en-ZA', 113, 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'KIR', 'en-ZA', 114, 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'PRK', 'en-ZA', 115, 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'KOR', 'en-ZA', 116, 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'KWT', 'en-ZA', 117, 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'KGZ', 'en-ZA', 118, 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'LAO', 'en-ZA', 119, 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'LVA', 'en-ZA', 120, 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'LBN', 'en-ZA', 121, 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'LSO', 'en-ZA', 122, 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'LBR', 'en-ZA', 123, 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'LBY', 'en-ZA', 124, 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'LIE', 'en-ZA', 125, 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'LTU', 'en-ZA', 126, 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'LUX', 'en-ZA', 127, 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'MAC', 'en-ZA', 128, 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'MDG', 'en-ZA', 129, 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'MWI', 'en-ZA', 130, 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'MYS', 'en-ZA', 131, 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'MDV', 'en-ZA', 132, 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'MLI', 'en-ZA', 133, 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'MLT', 'en-ZA', 134, 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'MHL', 'en-ZA', 135, 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'MTQ', 'en-ZA', 136, 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'MRT', 'en-ZA', 137, 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'MUS', 'en-ZA', 138, 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'MYT', 'en-ZA', 139, 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'MEX', 'en-ZA', 140, 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'FSM', 'en-ZA', 141, 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'MDA', 'en-ZA', 142, 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'MCO', 'en-ZA', 143, ' Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'MNG', 'en-ZA', 144, 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'MNE', 'en-ZA', 145, 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'MSR', 'en-ZA', 146, 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'MAR', 'en-ZA', 147, 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'MOZ', 'en-ZA', 148, 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'MMR', 'en-ZA', 149, 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'NAM', 'en-ZA', 150, 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'NRU', 'en-ZA', 151, 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'NPL', 'en-ZA', 152, 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'NLD', 'en-ZA', 153, 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'NCL', 'en-ZA', 154, 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'NZL', 'en-ZA', 155, 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'NIC', 'en-ZA', 156, 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'NER', 'en-ZA', 157, 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'NGA', 'en-ZA', 158, 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'NIU', 'en-ZA', 159, 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'NFK', 'en-ZA', 160, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'MNP', 'en-ZA', 161, 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'NOR', 'en-ZA', 162, 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'OMN', 'en-ZA', 163, 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'PAK', 'en-ZA', 164, 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'PLW', 'en-ZA', 165, 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'PSE', 'en-ZA', 166, 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'PAN', 'en-ZA', 167, 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'PNG', 'en-ZA', 168, 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'PRY', 'en-ZA', 169, 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'PER', 'en-ZA', 170, 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'PHL', 'en-ZA', 171, 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'PCN', 'en-ZA', 172, 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'POL', 'en-ZA', 173, 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'PRT', 'en-ZA', 174, 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'PRI', 'en-ZA', 175, 'Commonwealth of Puerto Rico', 'PRT Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'QAT', 'en-ZA', 176, 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'MKD', 'en-ZA', 177, 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'ROU', 'en-ZA', 178, 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'RUS', 'en-ZA', 179, 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'RWA', 'en-ZA', 180, 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'REU', 'en-ZA', 181, 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'BLM', 'en-ZA', 182, 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'KNA', 'en-ZA', 183, 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'LCA', 'en-ZA', 184, 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'MAF', 'en-ZA', 185, 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'SPM', 'en-ZA', 186, 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'VCT', 'en-ZA', 187, 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'WSM', 'en-ZA', 188, 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'SMR', 'en-ZA', 189, 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'STP', 'en-ZA', 190, 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'SAU', 'en-ZA', 191, 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'SEN', 'en-ZA', 192, 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'SRB', 'en-ZA', 193, 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'SYC', 'en-ZA', 194, 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'SLE', 'en-ZA', 195, 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'SGP', 'en-ZA', 196, 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'SXM', 'en-ZA', 197, 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'SVK', 'en-ZA', 198, 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'SVN', 'en-ZA', 199, 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'SLB', 'en-ZA', 200, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'SOM', 'en-ZA', 201, 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'ZAF', 'en-ZA', 202, 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'SSD', 'en-ZA', 203, 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'ESP', 'en-ZA', 204, 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'LKA', 'en-ZA', 205, 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'SDN', 'en-ZA', 206, 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'SUR', 'en-ZA', 207, 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'SJM', 'en-ZA', 208, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'SWZ', 'en-ZA', 209, 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'SWE', 'en-ZA', 210, 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'CHE', 'en-ZA', 211, 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'SYR', 'en-ZA', 212, 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'TWN', 'en-ZA', 213, 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'TJK', 'en-ZA', 214, 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'TZA', 'en-ZA', 215, 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'THA', 'en-ZA', 216, 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'TLS', 'en-ZA', 217, 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'TGO', 'en-ZA', 218, 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'TKL', 'en-ZA', 219, 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'TON', 'en-ZA', 220, 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'TTO', 'en-ZA', 221, 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'TUN', 'en-ZA', 222, 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'TUR', 'en-ZA', 223, 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'TKM', 'en-ZA', 224, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'TCA', 'en-ZA', 225, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'TUV', 'en-ZA', 226, 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'UGA', 'en-ZA', 227, 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'UKR', 'en-ZA', 228, 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'ARE', 'en-ZA', 229, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'GBR', 'en-ZA', 230, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'UMI', 'en-ZA', 231, 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'USA', 'en-ZA', 232, 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'URY', 'en-ZA', 233, 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'UZB', 'en-ZA', 234, 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'VUT', 'en-ZA', 235, 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'VEN', 'en-ZA', 236, 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'VNM', 'en-ZA', 237, 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'VGB', 'en-ZA', 238, 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'VIR', 'en-ZA', 239, 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'WLF', 'en-ZA', 240, 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'ESH', 'en-ZA', 241, 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'YEM', 'en-ZA', 242, 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'ZMB', 'en-ZA', 243, 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'ZWE', 'en-ZA', 244, 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'ALA', 'en-ZA', 245, 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZZ', 'ZZZ', 'en-ZA', 999, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');


-- See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AB', 'ABK' , 'en-US', 1, 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AF', 'AFR' , 'en-US', 2, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AM', 'AMH' , 'en-US', 3, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AR', 'ARA' , 'en-US', 4, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AY', 'AYM' , 'en-US', 5, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AZ', 'AZE' , 'en-US', 6, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BE', 'BEL' , 'en-US', 7, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BG', 'BUL' , 'en-US', 8, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BI', 'BIS' , 'en-US', 9, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BN', 'BEN' , 'en-US', 10, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BS', 'BOS' , 'en-US', 11, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('CS', 'CZE' , 'en-US', 12, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DA', 'DAN' , 'en-US', 13, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DE', 'DEU' , 'en-US', 14, 'German', 'German', 'German');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DZ', 'DZO' , 'en-US', 15, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('EL', 'ELL' , 'en-US', 16, 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('EN', 'ENG' , 'en-US', 17, 'English', 'English', 'English');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ES', 'SPA' , 'en-US', 18, 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ET', 'EST' , 'en-US', 19, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FI', 'FIN' , 'en-US', 20, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FJ', 'FIJ' , 'en-US', 21, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FR', 'FRA' , 'en-US', 22, 'French', 'French', 'French');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('GN', 'GRN' , 'en-US', 23, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HE', 'HEB' , 'en-US', 24, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HI', 'HIN' , 'en-US', 25, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HO', 'HMO' , 'en-US', 26, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HR', 'HRV' , 'en-US', 27, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HT', 'HAT' , 'en-US', 28, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HU', 'HUN' , 'en-US', 29, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HY', 'HYE' , 'en-US', 30, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ID', 'IND' , 'en-US', 31, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('IS', 'ISL' , 'en-US', 32, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('IT', 'ITA' , 'en-US', 33, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('JA', 'JPN' , 'en-US', 34, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KA', 'KAT' , 'en-US', 35, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KK', 'KAZ' , 'en-US', 36, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KM', 'KHM' , 'en-US', 37, 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KO', 'KOR' , 'en-US', 38, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KY', 'KIR' , 'en-US', 39, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LB', 'LTZ' , 'en-US', 40, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LO', 'LAO' , 'en-US', 41, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LT', 'LIT' , 'en-US', 42, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LV', 'LAV' , 'en-US', 43, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MG', 'MLG' , 'en-US', 44, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MI', 'MRI' , 'en-US', 45, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MK', 'MKD' , 'en-US', 46, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MN', 'MON' , 'en-US', 47, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MR', 'MAR' , 'en-US', 48, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MS', 'MSA' , 'en-US', 49, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MT', 'MLT' , 'en-US', 50, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MY', 'MYA' , 'en-US', 51, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ND', 'NDE' , 'en-US', 52, 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NE', 'NEP' , 'en-US', 53, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NL', 'NLD' , 'en-US', 54, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NO', 'NOR' , 'en-US', 55, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NR', 'NBL' , 'en-US', 56, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NY', 'NYA' , 'en-US', 57, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('OS', 'OSS' , 'en-US', 58, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PL', 'POL' , 'en-US', 59, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PS', 'PUS' , 'en-US', 60, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PT', 'POR' , 'en-US', 61, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('QU', 'QUE' , 'en-US', 62, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RM', 'ROH' , 'en-US', 63, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RO', 'RON' , 'en-US', 64, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RU', 'RUS' , 'en-US', 65, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RW', 'KIN' , 'en-US', 66, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SE', 'SME' , 'en-US', 67, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SI', 'SIN' , 'en-US', 68, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SK', 'SLK' , 'en-US', 69, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SL', 'SLV' , 'en-US', 70, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SN', 'SNA' , 'en-US', 71, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SO', 'SQI' , 'en-US', 72, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SR', 'SRP' , 'en-US', 73, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ST', 'SOT' , 'en-US', 74, 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SV', 'SWE' , 'en-US', 75, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SW', 'SWA' , 'en-US', 76, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TA', 'TAM' , 'en-US', 77, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TG', 'TGK' , 'en-US', 78, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TH', 'THA' , 'en-US', 79, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TI', 'TIR' , 'en-US', 80, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TK', 'TUK' , 'en-US', 81, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TN', 'TSN' , 'en-US', 82, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TR', 'TUR' , 'en-US', 83, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TS', 'TSO' , 'en-US', 84, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UK', 'UKR' , 'en-US', 85, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UR', 'URD' , 'en-US', 86, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UZ', 'UZB' , 'en-US', 87, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('VE', 'VEN' , 'en-US', 88, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('VI', 'VIE' , 'en-US', 89, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('XH', 'XHO' , 'en-US', 90, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZH', 'ZHO' , 'en-US', 91, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZU', 'ZUL' , 'en-US', 92, 'Zulu', 'Zulu', 'Zulu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'ZZZ' , 'en-US', 999, 'Unknown', 'Unknown', 'Unknown');

INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AB', 'ABK' , 'en-ZA', 1, 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AF', 'AFR' , 'en-ZA', 2, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AM', 'AMH' , 'en-ZA', 3, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AR', 'ARA' , 'en-ZA', 4, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AY', 'AYM' , 'en-ZA', 5, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('AZ', 'AZE' , 'en-ZA', 6, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BE', 'BEL' , 'en-ZA', 7, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BG', 'BUL' , 'en-ZA', 8, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BI', 'BIS' , 'en-ZA', 9, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BN', 'BEN' , 'en-ZA', 10, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('BS', 'BOS' , 'en-ZA', 11, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('CS', 'CZE' , 'en-ZA', 12, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DA', 'DAN' , 'en-ZA', 13, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DE', 'DEU' , 'en-ZA', 14, 'German', 'German', 'German');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('DZ', 'DZO' , 'en-ZA', 15, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('EL', 'ELL' , 'en-ZA', 16, 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('EN', 'ENG' , 'en-ZA', 17, 'English', 'English', 'English');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ES', 'SPA' , 'en-ZA', 18, 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ET', 'EST' , 'en-ZA', 19, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FI', 'FIN' , 'en-ZA', 20, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FJ', 'FIJ' , 'en-ZA', 21, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('FR', 'FRA' , 'en-ZA', 22, 'French', 'French', 'French');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('GN', 'GRN' , 'en-ZA', 23, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HE', 'HEB' , 'en-ZA', 24, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HI', 'HIN' , 'en-ZA', 25, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HO', 'HMO' , 'en-ZA', 26, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HR', 'HRV' , 'en-ZA', 27, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HT', 'HAT' , 'en-ZA', 28, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HU', 'HUN' , 'en-ZA', 29, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('HY', 'HYE' , 'en-ZA', 30, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ID', 'IND' , 'en-ZA', 31, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('IS', 'ISL' , 'en-ZA', 32, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('IT', 'ITA' , 'en-ZA', 33, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('JA', 'JPN' , 'en-ZA', 34, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KA', 'KAT' , 'en-ZA', 35, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KK', 'KAZ' , 'en-ZA', 36, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KM', 'KHM' , 'en-ZA', 37, 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KO', 'KOR' , 'en-ZA', 38, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('KY', 'KIR' , 'en-ZA', 39, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LB', 'LTZ' , 'en-ZA', 40, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LO', 'LAO' , 'en-ZA', 41, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LT', 'LIT' , 'en-ZA', 42, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('LV', 'LAV' , 'en-ZA', 43, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MG', 'MLG' , 'en-ZA', 44, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MI', 'MRI' , 'en-ZA', 45, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MK', 'MKD' , 'en-ZA', 46, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MN', 'MON' , 'en-ZA', 47, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MR', 'MAR' , 'en-ZA', 48, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MS', 'MSA' , 'en-ZA', 49, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MT', 'MLT' , 'en-ZA', 50, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('MY', 'MYA' , 'en-ZA', 51, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ND', 'NDE' , 'en-ZA', 52, 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NE', 'NEP' , 'en-ZA', 53, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NL', 'NLD' , 'en-ZA', 54, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NO', 'NOR' , 'en-ZA', 55, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NR', 'NBL' , 'en-ZA', 56, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('NY', 'NYA' , 'en-ZA', 57, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('OS', 'OSS' , 'en-ZA', 58, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PL', 'POL' , 'en-ZA', 59, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PS', 'PUS' , 'en-ZA', 60, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('PT', 'POR' , 'en-ZA', 61, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('QU', 'QUE' , 'en-ZA', 62, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RM', 'ROH' , 'en-ZA', 63, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RO', 'RON' , 'en-ZA', 64, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RU', 'RUS' , 'en-ZA', 65, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('RW', 'KIN' , 'en-ZA', 66, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SE', 'SME' , 'en-ZA', 67, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SI', 'SIN' , 'en-ZA', 68, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SK', 'SLK' , 'en-ZA', 69, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SL', 'SLV' , 'en-ZA', 70, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SN', 'SNA' , 'en-ZA', 71, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SO', 'SQI' , 'en-ZA', 72, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SR', 'SRP' , 'en-ZA', 73, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ST', 'SOT' , 'en-ZA', 74, 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SV', 'SWE' , 'en-ZA', 75, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('SW', 'SWA' , 'en-ZA', 76, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TA', 'TAM' , 'en-ZA', 77, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TG', 'TGK' , 'en-ZA', 78, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TH', 'THA' , 'en-ZA', 79, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TI', 'TIR' , 'en-ZA', 80, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TK', 'TUK' , 'en-ZA', 81, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TN', 'TSN' , 'en-ZA', 82, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TR', 'TUR' , 'en-ZA', 83, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('TS', 'TSO' , 'en-ZA', 84, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UK', 'UKR' , 'en-ZA', 85, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UR', 'URD' , 'en-ZA', 86, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('UZ', 'UZB' , 'en-ZA', 87, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('VE', 'VEN' , 'en-ZA', 88, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('VI', 'VIE' , 'en-ZA', 89, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('XH', 'XHO' , 'en-ZA', 90, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZH', 'ZHO' , 'en-ZA', 91, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZU', 'ZUL' , 'en-ZA', 92, 'Zulu', 'Zulu', 'Zulu');
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'ZZZ' , 'en-ZA', 999, 'Unknown', 'Unknown', 'Unknown');


INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('metric', 'en-US', 1, 'Metric', 'Metric');
INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('imperial', 'en-US', 2, 'British Imperial', 'British Imperial');
INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('customary', 'en-US', 3, 'United States Customary', 'United States Customary');

INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('metric', 'en-ZA', 1, 'Metric', 'Metric');
INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('imperial', 'en-ZA', 2, 'British Imperial', 'British Imperial');
INSERT INTO reference.measurement_systems (code, locale_id, sort_index, name, description)
  VALUES ('customary', 'en-ZA', 3, 'United States Customary', 'United States Customary');


INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('length', 'en-US', 1, 'Length', 'Length');
INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('mass', 'en-US', 2, 'Mass', 'Mass');
INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('volume', 'en-US', 2, 'Volume', 'Volume');

INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('length', 'en-ZA', 1, 'Length', 'Length');
INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('mass', 'en-ZA', 2, 'Mass', 'Mass');
INSERT INTO reference.measurement_unit_types(code, locale_id, sort_index, name, description)
  VALUES ('volume', 'en-ZA', 2, 'Volume', 'Volume');


INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_centimeter', 'en-US', 101, 'Centimeter', 'Centimeter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_meter', 'en-US', 102, 'Meter', 'Meter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_kilogram', 'en-US', 103, 'Kilogram', 'Kilogram', 'metric', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_inch', 'en-US', 201, 'Inch', 'Inch', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_foot', 'en-US', 202, 'Foot', 'Foot', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_pound', 'en-US', 203, 'Pound', 'Pound', 'imperial', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_inch', 'en-US', 301, 'Inch', 'Inch', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_foot', 'en-US', 302, 'Foot', 'Foot', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_pound', 'en-US', 303, 'Pound', 'Pound', 'customary', 'mass');

INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_centimeter', 'en-ZA', 101, 'Centimeter', 'Centimeter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_meter', 'en-ZA', 102, 'Meter', 'Meter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('metric_kilogram', 'en-ZA', 103, 'Kilogram', 'Kilogram', 'metric', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_inch', 'en-ZA', 201, 'Inch', 'Inch', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_foot', 'en-ZA', 202, 'Foot', 'Foot', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('imperial_pound', 'en-ZA', 203, 'Pound', 'Pound', 'imperial', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_inch', 'en-ZA', 301, 'Inch', 'Inch', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_foot', 'en-ZA', 302, 'Foot', 'Foot', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, sort_index, name, description, system, type)
  VALUES ('customary_pound', 'en-ZA', 303, 'Pound', 'Pound', 'customary', 'mass');


INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'EC', 'en-US', 1, 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'FS', 'en-US', 1, 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'GP', 'en-US', 1, 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'LP', 'en-US', 1, 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'MP', 'en-US', 1, 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NC', 'en-US', 1, 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NW', 'en-US', 1, 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'WC', 'en-US', 1, 'Western Cape', 'Western Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'ZN', 'en-US', 1, 'KwaZulu-Natal', 'KwaZulu-Natal');

INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'EC', 'en-ZA', 1, 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'FS', 'en-ZA', 1, 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'GP', 'en-ZA', 1, 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'LP', 'en-ZA', 1, 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'MP', 'en-ZA', 1, 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NC', 'en-ZA', 1, 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NW', 'en-ZA', 1, 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'WC', 'en-ZA', 1, 'Western Cape', 'Western Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'ZN', 'en-ZA', 1, 'KwaZulu-Natal', 'KwaZulu-Natal');

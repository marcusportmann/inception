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
  sort_index      INTEGER,
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
  sort_index  INTEGER,
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
  sort_index  INTEGER,
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
  sort_index  INTEGER,
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
  sort_index  INTEGER,
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
  sort_index  INTEGER,
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
  VALUES ('ZZ', 'ZZZ', 'en-US', 0, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'AFG', 'en-US', 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'ALB', 'en-US', 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'DZA', 'en-US', 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'ASM', 'en-US', 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'AND', 'en-US', 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'AGO', 'en-US', 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'AIA', 'en-US', 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'ATA', 'en-US', 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'ATG', 'en-US', 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'ARG', 'en-US', 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'ARM', 'en-US', 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'ABW', 'en-US', 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'AUS', 'en-US', 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'AUT', 'en-US', 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'AZE', 'en-US', 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'BHS', 'en-US', 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'BHR', 'en-US', 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'BGD', 'en-US', 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'BRB', 'en-US', 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'BLR', 'en-US', ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'BEL', 'en-US', 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'BLZ', 'en-US', 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'BEN', 'en-US', 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'BMU', 'en-US', 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'BTN', 'en-US', 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'BOL', 'en-US', 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'BES', 'en-US', 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'BIH', 'en-US', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'BWA', 'en-US', 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'BVT', 'en-US', 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'BRA', 'en-US', 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'BRN', 'en-US', 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'BGR', 'en-US', 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'BFA', 'en-US', 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'BDI', 'en-US', 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'CPV', 'en-US', 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'KHM', 'en-US', 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'CMR', 'en-US', 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'CAN', 'en-US', 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'CYM', 'en-US', 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'CAF', 'en-US', 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'TCD', 'en-US', 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'CHL', 'en-US', 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'CHN', 'en-US', 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'CXR', 'en-US', 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'CCK', 'en-US', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'COL', 'en-US', 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'COM', 'en-US', 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'COG', 'en-US', 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'COD', 'en-US', 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'COK', 'en-US', 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'CRI', 'en-US', 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'HRV', 'en-US', 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'CUB', 'en-US', 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'CUW', 'en-US', 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'CYP', 'en-US', 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'CZE', 'en-US', 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'CIV', 'en-US', 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'DNK', 'en-US', 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'DJI', 'en-US', 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'DMA', 'en-US', ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'DOM', 'en-US', 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'ECU', 'en-US', 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'EGY', 'en-US', 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'SLV', 'en-US', 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'GNQ', 'en-US', 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'ERI', 'en-US', 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'EST', 'en-US', 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'ETH', 'en-US', 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'FLK', 'en-US', 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'FRO', 'en-US', 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'FJI', 'en-US', 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'FIN', 'en-US', 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'FRA', 'en-US', 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'GUF', 'en-US', 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'PYF', 'en-US', 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'ATF', 'en-US', 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'GAB', 'en-US', 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'GMB', 'en-US', 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'GEO', 'en-US', 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'DEU', 'en-US', 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'GHA', 'en-US', 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'GIB', 'en-US', 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'GRC', 'en-US', 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'GRL', 'en-US', 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'GRD', 'en-US', 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'GLP', 'en-US', 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'GUM', 'en-US', 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'GTM', 'en-US', 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'GGY', 'en-US', 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'GIN', 'en-US', 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'GNB', 'en-US', 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'GUY', 'en-US', 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'HTI', 'en-US', 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'VAT', 'en-US', 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'HND', 'en-US', 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'HKG', 'en-US', 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'HUN', 'en-US', 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'ISL', 'en-US', 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'IND', 'en-US', 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'IDN', 'en-US', 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'IRN', 'en-US', 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'IRQ', 'en-US', 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'IRL', 'en-US', 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'IMN', 'en-US', 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'ISR', 'en-US', 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'ITA', 'en-US', 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'JAM', 'en-US', 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'JPN', 'en-US', 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'JEY', 'en-US', 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'JOR', 'en-US', 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'KAZ', 'en-US', 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'KEN', 'en-US', 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'KIR', 'en-US', 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'PRK', 'en-US', 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'KOR', 'en-US', 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'KWT', 'en-US', 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'KGZ', 'en-US', 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'LAO', 'en-US', 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'LVA', 'en-US', 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'LBN', 'en-US', 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'LSO', 'en-US', 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'LBR', 'en-US', 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'LBY', 'en-US', 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'LIE', 'en-US', 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'LTU', 'en-US', 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'LUX', 'en-US', 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'MAC', 'en-US', 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'MDG', 'en-US', 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'MWI', 'en-US', 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'MYS', 'en-US', 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'MDV', 'en-US', 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'MLI', 'en-US', 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'MLT', 'en-US', 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'MHL', 'en-US', 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'MTQ', 'en-US', 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'MRT', 'en-US', 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'MUS', 'en-US', 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'MYT', 'en-US', 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'MEX', 'en-US', 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'FSM', 'en-US', 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'MDA', 'en-US', 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'MCO', 'en-US', 'Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'MNG', 'en-US', 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'MNE', 'en-US', 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'MSR', 'en-US', 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'MAR', 'en-US', 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'MOZ', 'en-US', 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'MMR', 'en-US', 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'NAM', 'en-US', 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'NRU', 'en-US', 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'NPL', 'en-US', 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'NLD', 'en-US', 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'NCL', 'en-US', 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'NZL', 'en-US', 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'NIC', 'en-US', 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'NER', 'en-US', 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'NGA', 'en-US', 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'NIU', 'en-US', 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'NFK', 'en-US', 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'MNP', 'en-US', 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'NOR', 'en-US', 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'OMN', 'en-US', 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'PAK', 'en-US', 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'PLW', 'en-US', 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'PSE', 'en-US', 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'PAN', 'en-US', 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'PNG', 'en-US', 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'PRY', 'en-US', 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'PER', 'en-US', 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'PHL', 'en-US', 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'PCN', 'en-US', 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'POL', 'en-US', 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'PRT', 'en-US', 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'PRI', 'en-US', 'Commonwealth of Puerto Rico', 'PRT Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'QAT', 'en-US', 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'MKD', 'en-US', 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'ROU', 'en-US', 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'RUS', 'en-US', 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'RWA', 'en-US', 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'REU', 'en-US', 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'BLM', 'en-US', 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'KNA', 'en-US', 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'LCA', 'en-US', 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'MAF', 'en-US', 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'SPM', 'en-US', 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'VCT', 'en-US', 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'WSM', 'en-US', 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'SMR', 'en-US', 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'STP', 'en-US', 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'SAU', 'en-US', 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'SEN', 'en-US', 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'SRB', 'en-US', 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'SYC', 'en-US', 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'SLE', 'en-US', 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'SGP', 'en-US', 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'SXM', 'en-US', 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'SVK', 'en-US', 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'SVN', 'en-US', 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'SLB', 'en-US', 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'SOM', 'en-US', 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'ZAF', 'en-US', 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'SSD', 'en-US', 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'ESP', 'en-US', 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'LKA', 'en-US', 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'SDN', 'en-US', 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'SUR', 'en-US', 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'SJM', 'en-US', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'SWZ', 'en-US', 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'SWE', 'en-US', 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'CHE', 'en-US', 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'SYR', 'en-US', 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'TWN', 'en-US', 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'TJK', 'en-US', 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'TZA', 'en-US', 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'THA', 'en-US', 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'TLS', 'en-US', 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'TGO', 'en-US', 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'TKL', 'en-US', 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'TON', 'en-US', 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'TTO', 'en-US', 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'TUN', 'en-US', 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'TUR', 'en-US', 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'TKM', 'en-US', 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'TCA', 'en-US', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'TUV', 'en-US', 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'UGA', 'en-US', 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'UKR', 'en-US', 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'ARE', 'en-US', 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'GBR', 'en-US', 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'UMI', 'en-US', 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'USA', 'en-US', 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'URY', 'en-US', 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'UZB', 'en-US', 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'VUT', 'en-US', 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'VEN', 'en-US', 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'VNM', 'en-US', 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'VGB', 'en-US', 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'VIR', 'en-US', 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'WLF', 'en-US', 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'ESH', 'en-US', 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'YEM', 'en-US', 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'ZMB', 'en-US', 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'ZWE', 'en-US', 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'ALA', 'en-US', 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');

INSERT INTO reference.countries (code, iso3_code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZZ', 'ZZZ', 'en-ZA', 0, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'AFG', 'en-ZA', 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'ALB', 'en-ZA', 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'DZA', 'en-ZA', 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'ASM', 'en-ZA', 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'AND', 'en-ZA', 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'AGO', 'en-ZA', 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'AIA', 'en-ZA', 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'ATA', 'en-ZA', 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'ATG', 'en-ZA', 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'ARG', 'en-ZA', 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'ARM', 'en-ZA', 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'ABW', 'en-ZA', 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'AUS', 'en-ZA', 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'AUT', 'en-ZA', 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'AZE', 'en-ZA', 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'BHS', 'en-ZA', 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'BHR', 'en-ZA', 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'BGD', 'en-ZA', 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'BRB', 'en-ZA', 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'BLR', 'en-ZA', ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'BEL', 'en-ZA', 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'BLZ', 'en-ZA', 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'BEN', 'en-ZA', 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'BMU', 'en-ZA', 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'BTN', 'en-ZA', 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'BOL', 'en-ZA', 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'BES', 'en-ZA', 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'BIH', 'en-ZA', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'BWA', 'en-ZA', 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'BVT', 'en-ZA', 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'BRA', 'en-ZA', 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'BRN', 'en-ZA', 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'BGR', 'en-ZA', 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'BFA', 'en-ZA', 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'BDI', 'en-ZA', 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'CPV', 'en-ZA', 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'KHM', 'en-ZA', 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'CMR', 'en-ZA', 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'CAN', 'en-ZA', 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'CYM', 'en-ZA', 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'CAF', 'en-ZA', 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'TCD', 'en-ZA', 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'CHL', 'en-ZA', 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'CHN', 'en-ZA', 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'CXR', 'en-ZA', 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'CCK', 'en-ZA', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'COL', 'en-ZA', 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'COM', 'en-ZA', 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'COG', 'en-ZA', 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'COD', 'en-ZA', 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'COK', 'en-ZA', 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'CRI', 'en-ZA', 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'HRV', 'en-ZA', 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'CUB', 'en-ZA', 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'CUW', 'en-ZA', 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'CYP', 'en-ZA', 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'CZE', 'en-ZA', 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'CIV', 'en-ZA', 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'DNK', 'en-ZA', 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'DJI', 'en-ZA', 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'DMA', 'en-ZA', ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'DOM', 'en-ZA', 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'ECU', 'en-ZA', 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'EGY', 'en-ZA', 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'SLV', 'en-ZA', 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'GNQ', 'en-ZA', 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'ERI', 'en-ZA', 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'EST', 'en-ZA', 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'ETH', 'en-ZA', 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'FLK', 'en-ZA', 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'FRO', 'en-ZA', 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'FJI', 'en-ZA', 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'FIN', 'en-ZA', 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'FRA', 'en-ZA', 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'GUF', 'en-ZA', 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'PYF', 'en-ZA', 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'ATF', 'en-ZA', 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'GAB', 'en-ZA', 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'GMB', 'en-ZA', 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'GEO', 'en-ZA', 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'DEU', 'en-ZA', 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'GHA', 'en-ZA', 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'GIB', 'en-ZA', 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'GRC', 'en-ZA', 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'GRL', 'en-ZA', 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'GRD', 'en-ZA', 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'GLP', 'en-ZA', 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'GUM', 'en-ZA', 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'GTM', 'en-ZA', 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'GGY', 'en-ZA', 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'GIN', 'en-ZA', 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'GNB', 'en-ZA', 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'GUY', 'en-ZA', 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'HTI', 'en-ZA', 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'VAT', 'en-ZA', 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'HND', 'en-ZA', 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'HKG', 'en-ZA', 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'HUN', 'en-ZA', 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'ISL', 'en-ZA', 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'IND', 'en-ZA', 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'IDN', 'en-ZA', 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'IRN', 'en-ZA', 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'IRQ', 'en-ZA', 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'IRL', 'en-ZA', 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'IMN', 'en-ZA', 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'ISR', 'en-ZA', 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'ITA', 'en-ZA', 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'JAM', 'en-ZA', 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'JPN', 'en-ZA', 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'JEY', 'en-ZA', 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'JOR', 'en-ZA', 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'KAZ', 'en-ZA', 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'KEN', 'en-ZA', 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'KIR', 'en-ZA', 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'PRK', 'en-ZA', 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'KOR', 'en-ZA', 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'KWT', 'en-ZA', 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'KGZ', 'en-ZA', 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'LAO', 'en-ZA', 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'LVA', 'en-ZA', 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'LBN', 'en-ZA', 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'LSO', 'en-ZA', 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'LBR', 'en-ZA', 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'LBY', 'en-ZA', 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'LIE', 'en-ZA', 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'LTU', 'en-ZA', 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'LUX', 'en-ZA', 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'MAC', 'en-ZA', 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'MDG', 'en-ZA', 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'MWI', 'en-ZA', 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'MYS', 'en-ZA', 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'MDV', 'en-ZA', 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'MLI', 'en-ZA', 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'MLT', 'en-ZA', 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'MHL', 'en-ZA', 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'MTQ', 'en-ZA', 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'MRT', 'en-ZA', 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'MUS', 'en-ZA', 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'MYT', 'en-ZA', 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'MEX', 'en-ZA', 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'FSM', 'en-ZA', 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'MDA', 'en-ZA', 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'MCO', 'en-ZA', 'Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'MNG', 'en-ZA', 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'MNE', 'en-ZA', 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'MSR', 'en-ZA', 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'MAR', 'en-ZA', 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'MOZ', 'en-ZA', 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'MMR', 'en-ZA', 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'NAM', 'en-ZA', 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'NRU', 'en-ZA', 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'NPL', 'en-ZA', 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'NLD', 'en-ZA', 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'NCL', 'en-ZA', 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'NZL', 'en-ZA', 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'NIC', 'en-ZA', 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'NER', 'en-ZA', 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'NGA', 'en-ZA', 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'NIU', 'en-ZA', 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'NFK', 'en-ZA', 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'MNP', 'en-ZA', 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'NOR', 'en-ZA', 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'OMN', 'en-ZA', 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'PAK', 'en-ZA', 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'PLW', 'en-ZA', 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'PSE', 'en-ZA', 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'PAN', 'en-ZA', 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'PNG', 'en-ZA', 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'PRY', 'en-ZA', 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'PER', 'en-ZA', 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'PHL', 'en-ZA', 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'PCN', 'en-ZA', 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'POL', 'en-ZA', 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'PRT', 'en-ZA', 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'PRI', 'en-ZA', 'Commonwealth of Puerto Rico', 'PRT Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'QAT', 'en-ZA', 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'MKD', 'en-ZA', 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'ROU', 'en-ZA', 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'RUS', 'en-ZA', 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'RWA', 'en-ZA', 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'REU', 'en-ZA', 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'BLM', 'en-ZA', 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'KNA', 'en-ZA', 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'LCA', 'en-ZA', 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'MAF', 'en-ZA', 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'SPM', 'en-ZA', 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'VCT', 'en-ZA', 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'WSM', 'en-ZA', 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'SMR', 'en-ZA', 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'STP', 'en-ZA', 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'SAU', 'en-ZA', 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'SEN', 'en-ZA', 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'SRB', 'en-ZA', 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'SYC', 'en-ZA', 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'SLE', 'en-ZA', 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'SGP', 'en-ZA', 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'SXM', 'en-ZA', 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'SVK', 'en-ZA', 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'SVN', 'en-ZA', 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'SLB', 'en-ZA', 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'SOM', 'en-ZA', 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'ZAF', 'en-ZA', 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'SSD', 'en-ZA', 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'ESP', 'en-ZA', 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'LKA', 'en-ZA', 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'SDN', 'en-ZA', 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'SUR', 'en-ZA', 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'SJM', 'en-ZA', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'SWZ', 'en-ZA', 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'SWE', 'en-ZA', 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'CHE', 'en-ZA', 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'SYR', 'en-ZA', 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'TWN', 'en-ZA', 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'TJK', 'en-ZA', 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'TZA', 'en-ZA', 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'THA', 'en-ZA', 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'TLS', 'en-ZA', 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'TGO', 'en-ZA', 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'TKL', 'en-ZA', 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'TON', 'en-ZA', 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'TTO', 'en-ZA', 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'TUN', 'en-ZA', 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'TUR', 'en-ZA', 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'TKM', 'en-ZA', 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'TCA', 'en-ZA', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'TUV', 'en-ZA', 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'UGA', 'en-ZA', 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'UKR', 'en-ZA', 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'ARE', 'en-ZA', 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'GBR', 'en-ZA', 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'UMI', 'en-ZA', 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'USA', 'en-ZA', 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'URY', 'en-ZA', 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'UZB', 'en-ZA', 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'VUT', 'en-ZA', 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'VEN', 'en-ZA', 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'VNM', 'en-ZA', 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'VGB', 'en-ZA', 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'VIR', 'en-ZA', 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'WLF', 'en-ZA', 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'ESH', 'en-ZA', 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'YEM', 'en-ZA', 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'ZMB', 'en-ZA', 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'ZWE', 'en-ZA', 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, iso3_code, locale_id, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'ALA', 'en-ZA', 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');


-- See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'ZZZ' , 'en-US', 0, 'Unknown', 'Unknown', 'Unknown');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AB', 'ABK' , 'en-US', 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AF', 'AFR' , 'en-US', 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AM', 'AMH' , 'en-US', 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AR', 'ARA' , 'en-US', 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HY', 'HYE' , 'en-US', 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AY', 'AYM' , 'en-US', 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AZ', 'AZE' , 'en-US', 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BE', 'BEL' , 'en-US', 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BN', 'BEN' , 'en-US', 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BI', 'BIS' , 'en-US', 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BS', 'BOS' , 'en-US', 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BG', 'BUL' , 'en-US', 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MY', 'MYA' , 'en-US', 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NY', 'NYA' , 'en-US', 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ZH', 'ZHO' , 'en-US', 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HR', 'HRV' , 'en-US', 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('CS', 'CZE' , 'en-US', 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DA', 'DAN' , 'en-US', 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DZ', 'DZO' , 'en-US', 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NL', 'NLD' , 'en-US', 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('EN', 'ENG' , 'en-US', 'English', 'English', 'English');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ET', 'EST' , 'en-US', 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FJ', 'FIJ' , 'en-US', 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FI', 'FIN' , 'en-US', 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FR', 'FRA' , 'en-US', 'French', 'French', 'French');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KA', 'KAT' , 'en-US', 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DE', 'DEU' , 'en-US', 'German', 'German', 'German');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('EL', 'ELL' , 'en-US', 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('GN', 'GRN' , 'en-US', 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HT', 'HAT' , 'en-US', 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HE', 'HEB' , 'en-US', 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HI', 'HIN' , 'en-US', 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HO', 'HMO' , 'en-US', 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HU', 'HUN' , 'en-US', 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('IS', 'ISL' , 'en-US', 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ID', 'IND' , 'en-US', 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('IT', 'ITA' , 'en-US', 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('JA', 'JPN' , 'en-US', 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KK', 'KAZ' , 'en-US', 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KM', 'KHM' , 'en-US', 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RW', 'KIN' , 'en-US', 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KO', 'KOR' , 'en-US', 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KY', 'KIR' , 'en-US', 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LO', 'LAO' , 'en-US', 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LV', 'LAV' , 'en-US', 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LT', 'LIT' , 'en-US', 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LB', 'LTZ' , 'en-US', 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MK', 'MKD' , 'en-US', 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MG', 'MLG' , 'en-US', 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MS', 'MSA' , 'en-US', 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MT', 'MLT' , 'en-US', 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MI', 'MRI' , 'en-US', 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MR', 'MAR' , 'en-US', 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MN', 'MON' , 'en-US', 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ND', 'NDE' , 'en-US', 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NE', 'NEP' , 'en-US', 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SE', 'SME' , 'en-US', 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NO', 'NOR' , 'en-US', 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('OS', 'OSS' , 'en-US', 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PS', 'PUS' , 'en-US', 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PL', 'POL' , 'en-US', 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PT', 'POR' , 'en-US', 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('QU', 'QUE' , 'en-US', 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RO', 'RON' , 'en-US', 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RM', 'ROH' , 'en-US', 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RU', 'RUS' , 'en-US', 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SR', 'SRP' , 'en-US', 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SN', 'SNA' , 'en-US', 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SI', 'SIN' , 'en-US', 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SK', 'SLK' , 'en-US', 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SL', 'SLV' , 'en-US', 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SO', 'SQI' , 'en-US', 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ST', 'SOT' , 'en-US', 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NR', 'NBL' , 'en-US', 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ES', 'SPA' , 'en-US', 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SW', 'SWA' , 'en-US', 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SV', 'SWE' , 'en-US', 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TG', 'TGK' , 'en-US', 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TA', 'TAM' , 'en-US', 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TH', 'THA' , 'en-US', 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TI', 'TIR' , 'en-US', 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TS', 'TSO' , 'en-US', 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TN', 'TSN' , 'en-US', 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TK', 'TUK' , 'en-US', 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TR', 'TUR' , 'en-US', 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UK', 'UKR' , 'en-US', 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UR', 'URD' , 'en-US', 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UZ', 'UZB' , 'en-US', 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('VE', 'VEN' , 'en-US', 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('VI', 'VIE' , 'en-US', 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('XH', 'XHO' , 'en-US', 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ZU', 'ZUL' , 'en-US', 'Zulu', 'Zulu', 'Zulu');

INSERT INTO reference.languages (code, iso3_code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'ZZZ' , 'en-ZA', 0, 'Unknown', 'Unknown', 'Unknown');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AB', 'ABK' , 'en-ZA', 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AF', 'AFR' , 'en-ZA', 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AM', 'AMH' , 'en-ZA', 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AR', 'ARA' , 'en-ZA', 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HY', 'HYE' , 'en-ZA', 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AY', 'AYM' , 'en-ZA', 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('AZ', 'AZE' , 'en-ZA', 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BE', 'BEL' , 'en-ZA', 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BN', 'BEN' , 'en-ZA', 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BI', 'BIS' , 'en-ZA', 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BS', 'BOS' , 'en-ZA', 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('BG', 'BUL' , 'en-ZA', 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MY', 'MYA' , 'en-ZA', 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NY', 'NYA' , 'en-ZA', 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ZH', 'ZHO' , 'en-ZA', 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HR', 'HRV' , 'en-ZA', 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('CS', 'CZE' , 'en-ZA', 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DA', 'DAN' , 'en-ZA', 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DZ', 'DZO' , 'en-ZA', 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NL', 'NLD' , 'en-ZA', 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('EN', 'ENG' , 'en-ZA', 'English', 'English', 'English');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ET', 'EST' , 'en-ZA', 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FJ', 'FIJ' , 'en-ZA', 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FI', 'FIN' , 'en-ZA', 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('FR', 'FRA' , 'en-ZA', 'French', 'French', 'French');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KA', 'KAT' , 'en-ZA', 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('DE', 'DEU' , 'en-ZA', 'German', 'German', 'German');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('EL', 'ELL' , 'en-ZA', 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('GN', 'GRN' , 'en-ZA', 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HT', 'HAT' , 'en-ZA', 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HE', 'HEB' , 'en-ZA', 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HI', 'HIN' , 'en-ZA', 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HO', 'HMO' , 'en-ZA', 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('HU', 'HUN' , 'en-ZA', 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('IS', 'ISL' , 'en-ZA', 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ID', 'IND' , 'en-ZA', 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('IT', 'ITA' , 'en-ZA', 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('JA', 'JPN' , 'en-ZA', 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KK', 'KAZ' , 'en-ZA', 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KM', 'KHM' , 'en-ZA', 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RW', 'KIN' , 'en-ZA', 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KO', 'KOR' , 'en-ZA', 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('KY', 'KIR' , 'en-ZA', 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LO', 'LAO' , 'en-ZA', 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LV', 'LAV' , 'en-ZA', 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LT', 'LIT' , 'en-ZA', 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('LB', 'LTZ' , 'en-ZA', 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MK', 'MKD' , 'en-ZA', 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MG', 'MLG' , 'en-ZA', 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MS', 'MSA' , 'en-ZA', 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MT', 'MLT' , 'en-ZA', 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MI', 'MRI' , 'en-ZA', 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MR', 'MAR' , 'en-ZA', 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('MN', 'MON' , 'en-ZA', 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ND', 'NDE' , 'en-ZA', 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NE', 'NEP' , 'en-ZA', 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SE', 'SME' , 'en-ZA', 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NO', 'NOR' , 'en-ZA', 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('OS', 'OSS' , 'en-ZA', 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PS', 'PUS' , 'en-ZA', 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PL', 'POL' , 'en-ZA', 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('PT', 'POR' , 'en-ZA', 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('QU', 'QUE' , 'en-ZA', 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RO', 'RON' , 'en-ZA', 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RM', 'ROH' , 'en-ZA', 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('RU', 'RUS' , 'en-ZA', 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SR', 'SRP' , 'en-ZA', 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SN', 'SNA' , 'en-ZA', 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SI', 'SIN' , 'en-ZA', 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SK', 'SLK' , 'en-ZA', 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SL', 'SLV' , 'en-ZA', 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SO', 'SQI' , 'en-ZA', 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ST', 'SOT' , 'en-ZA', 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('NR', 'NBL' , 'en-ZA', 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ES', 'SPA' , 'en-ZA', 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SW', 'SWA' , 'en-ZA', 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('SV', 'SWE' , 'en-ZA', 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TG', 'TGK' , 'en-ZA', 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TA', 'TAM' , 'en-ZA', 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TH', 'THA' , 'en-ZA', 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TI', 'TIR' , 'en-ZA', 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TS', 'TSO' , 'en-ZA', 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TN', 'TSN' , 'en-ZA', 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TK', 'TUK' , 'en-ZA', 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('TR', 'TUR' , 'en-ZA', 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UK', 'UKR' , 'en-ZA', 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UR', 'URD' , 'en-ZA', 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('UZ', 'UZB' , 'en-ZA', 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('VE', 'VEN' , 'en-ZA', 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('VI', 'VIE' , 'en-ZA', 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('XH', 'XHO' , 'en-ZA', 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, iso3_code, locale_id, name, short_name, description)
  VALUES ('ZU', 'ZUL' , 'en-ZA', 'Zulu', 'Zulu', 'Zulu');


INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('imperial', 'en-US', 'British Imperial', 'British Imperial');
INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('metric', 'en-US', 'Metric', 'Metric');
INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('customary', 'en-US', 'United States Customary', 'United States Customary');

INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('imperial', 'en-ZA', 'British Imperial', 'British Imperial');
INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('metric', 'en-ZA', 'Metric', 'Metric');
INSERT INTO reference.measurement_systems (code, locale_id, name, description)
  VALUES ('customary', 'en-ZA', 'United States Customary', 'United States Customary');


INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('length', 'en-US', 'Length', 'Length');
INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('mass', 'en-US', 'Mass', 'Mass');
INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('volume', 'en-US', 'Volume', 'Volume');

INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('length', 'en-ZA', 'Length', 'Length');
INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('mass', 'en-ZA', 'Mass', 'Mass');
INSERT INTO reference.measurement_unit_types(code, locale_id, name, description)
  VALUES ('volume', 'en-ZA', 'Volume', 'Volume');


INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_inch', 'en-US', 'Inch', 'Inch', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_foot', 'en-US', 'Foot', 'Foot', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_pound', 'en-US', 'Pound', 'Pound', 'customary', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_inch', 'en-US', 'Inch', 'Inch', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_foot', 'en-US', 'Foot', 'Foot', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_pound', 'en-US', 'Pound', 'Pound', 'imperial', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_centimeter', 'en-US', 'Centimeter', 'Centimeter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_meter', 'en-US', 'Meter', 'Meter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_kilogram', 'en-US', 'Kilogram', 'Kilogram', 'metric', 'mass');

INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_inch', 'en-ZA', 'Inch', 'Inch', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_foot', 'en-ZA', 'Foot', 'Foot', 'customary', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('customary_pound', 'en-ZA', 'Pound', 'Pound', 'customary', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_inch', 'en-ZA', 'Inch', 'Inch', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_foot', 'en-ZA', 'Foot', 'Foot', 'imperial', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('imperial_pound', 'en-ZA', 'Pound', 'Pound', 'imperial', 'mass');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_centimeter', 'en-ZA', 'Centimeter', 'Centimeter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_meter', 'en-ZA', 'Meter', 'Meter', 'metric', 'length');
INSERT INTO reference.measurement_units(code, locale_id, name, description, system, type)
  VALUES ('metric_kilogram', 'en-ZA', 'Kilogram', 'Kilogram', 'metric', 'mass');


INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'EC', 'en-US', 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'FS', 'en-US', 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'GP', 'en-US', 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'ZN', 'en-US', 'KwaZulu-Natal', 'KwaZulu-Natal');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'LP', 'en-US', 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'MP', 'en-US', 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'NC', 'en-US', 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'NW', 'en-US', 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'WC', 'en-US', 'Western Cape', 'Western Cape');

INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'EC', 'en-ZA', 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'FS', 'en-ZA', 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'GP', 'en-ZA', 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'ZN', 'en-ZA', 'KwaZulu-Natal', 'KwaZulu-Natal');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'LP', 'en-ZA', 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'MP', 'en-ZA', 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'NC', 'en-ZA', 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'NW', 'en-ZA', 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, name, description)
  VALUES ('ZA', 'WC', 'en-ZA', 'Western Cape', 'Western Cape');

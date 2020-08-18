-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reference.communication_methods (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX communication_methods_locale_ix ON reference.communication_methods(locale);

COMMENT ON COLUMN reference.communication_methods.code IS 'The code for the communication method';

COMMENT ON COLUMN reference.communication_methods.locale IS 'The Unicode locale identifier for the communication method';

COMMENT ON COLUMN reference.communication_methods.sort_index IS 'The sort index for the communication method';

COMMENT ON COLUMN reference.communication_methods.name IS 'The name of the communication method';

COMMENT ON COLUMN reference.communication_methods.description IS 'The description for the communication method';


CREATE TABLE reference.countries (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(100)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX countries_locale_ix ON reference.countries(locale);

COMMENT ON COLUMN reference.countries.code IS 'The code for the country';

COMMENT ON COLUMN reference.countries.locale IS 'The Unicode locale identifier for the country';

COMMENT ON COLUMN reference.countries.sort_index IS 'The sort index for the country';

COMMENT ON COLUMN reference.countries.name IS 'The name of the country';

COMMENT ON COLUMN reference.countries.description IS 'The description for the country';


CREATE TABLE reference.employment_statuses (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX employment_statuses_locale_ix ON reference.employment_statuses(locale);

COMMENT ON COLUMN reference.employment_statuses.code IS 'The code for the employment status';

COMMENT ON COLUMN reference.employment_statuses.locale IS 'The Unicode locale identifier for the employment status';

COMMENT ON COLUMN reference.employment_statuses.sort_index IS 'The sort index for the employment status';

COMMENT ON COLUMN reference.employment_statuses.name IS 'The name of the employment status';

COMMENT ON COLUMN reference.employment_statuses.description IS 'The description for the employment status';


CREATE TABLE reference.employment_types (
  employment_status VARCHAR(10)  NOT NULL,
  code              VARCHAR(10)  NOT NULL,
  locale            VARCHAR(10)  NOT NULL,
  sort_index        INTEGER NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  description       VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (employment_status, code, locale)
);

CREATE INDEX employment_types_employment_status_ix ON reference.employment_types(employment_status);

CREATE INDEX employment_types_locale_ix ON reference.employment_types(locale);

COMMENT ON COLUMN reference.employment_types.employment_status IS 'The code for the employment status the employment type is associated with';

COMMENT ON COLUMN reference.employment_types.code IS 'The code for the employment type';

COMMENT ON COLUMN reference.employment_types.locale IS 'The Unicode locale identifier for the employment type';

COMMENT ON COLUMN reference.employment_types.sort_index IS 'The sort index for the employment type';

COMMENT ON COLUMN reference.employment_types.name IS 'The name of the employment type';

COMMENT ON COLUMN reference.employment_types.description IS 'The description for the employment type';


-- See: https://en.wikipedia.org/wiki/ISO/IEC_5218
CREATE TABLE reference.genders (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX genders_locale_ix ON reference.genders(locale);

COMMENT ON COLUMN reference.genders.code IS 'The code for the gender';

COMMENT ON COLUMN reference.genders.locale IS 'The Unicode locale identifier for the gender';

COMMENT ON COLUMN reference.genders.sort_index IS 'The sort index for the gender';

COMMENT ON COLUMN reference.genders.name IS 'The name of the gender';

COMMENT ON COLUMN reference.genders.description IS 'The description for the gender';









-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 3, 'Not known', 'Not known');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'E-mail', 'E-mail');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Phone', 'Phone');

INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 3, 'Not known', 'Not known');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'E-mail', 'E-mail');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Phone', 'Phone');

INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AD', 'en-US', 1, 'Andorra', 'Andorra');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AE', 'en-US', 2, 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AF', 'en-US', 3, 'Afghanistan', 'Afghanistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AG', 'en-US', 4, 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AI', 'en-US', 5, 'Anguilla', 'Anguilla');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AL', 'en-US', 6, 'Albania', 'Albania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AM', 'en-US', 7, 'Armenia', 'Armenia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AO', 'en-US', 8, 'Angola', 'Angola');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AQ', 'en-US', 9, 'Antarctica', 'Antarctica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AR', 'en-US', 10, 'Argentina', 'Argentina');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AS', 'en-US', 11, 'American Samoa', 'American Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AT', 'en-US', 12, 'Austria', 'Austria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AU', 'en-US', 13, 'Australia', 'Australia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AW', 'en-US', 14, 'Aruba', 'Aruba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AX', 'en-US', 15, 'Åland Islands', 'Åland Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AZ', 'en-US', 16, 'Azerbaijan', 'Azerbaijan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BA', 'en-US', 17, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BB', 'en-US', 18, 'Barbados', 'Barbados');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BD', 'en-US', 19, 'Bangladesh', 'Bangladesh');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BE', 'en-US', 20, 'Belgium', 'Belgium');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BF', 'en-US', 21, 'Burkina Faso', 'Burkina Faso');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BG', 'en-US', 22, 'Bulgaria', 'Bulgaria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BH', 'en-US', 23, 'Bahrain', 'Bahrain');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BI', 'en-US', 24, 'Burundi', 'Burundi');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BJ', 'en-US', 25, 'Benin', 'Benin');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BL', 'en-US', 26, 'Saint Barthélemy', 'Saint Barthélemy');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BM', 'en-US', 27, 'Bermuda', 'Bermuda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BN', 'en-US', 28, 'Brunei Darussalam', 'Brunei Darussalam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BO', 'en-US', 29, 'Bolivia, Plurinational State of', 'Bolivia, Plurinational State of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BQ', 'en-US', 30, 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BR', 'en-US', 31, 'Brazil', 'Brazil');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BS', 'en-US', 32, 'Bahamas', 'Bahamas');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BT', 'en-US', 33, 'Bhutan', 'Bhutan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BV', 'en-US', 34, 'Bouvet Island', 'Bouvet Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BW', 'en-US', 35, 'Botswana', 'Botswana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BY', 'en-US', 36, 'Belarus', 'Belarus');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BZ', 'en-US', 37, 'Belize', 'Belize');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CA', 'en-US', 38, 'Canada', 'Canada');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CC', 'en-US', 39, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CD', 'en-US', 40, 'Congo, the Democratic Republic of the', 'Congo, the Democratic Republic of the');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CF', 'en-US', 41, 'Central African Republic', 'Central African Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CG', 'en-US', 42, 'Congo', 'Congo');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CH', 'en-US', 43, 'Switzerland', 'Switzerland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CI', 'en-US', 44, 'Côte d''Ivoire', 'Côte d''Ivoire');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CK', 'en-US', 45, 'Cook Islands', 'Cook Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CL', 'en-US', 46, 'Chile', 'Chile');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CM', 'en-US', 47, 'Cameroon', 'Cameroon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CN', 'en-US', 48, 'China', 'China');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CO', 'en-US', 49, 'Colombia', 'Colombia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CR', 'en-US', 50, 'Costa Rica', 'Costa Rica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CU', 'en-US', 51, 'Cuba', 'Cuba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CV', 'en-US', 52, 'Cabo Verde', 'Cabo Verde');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CW', 'en-US', 53, 'Curaçao', 'Curaçao');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CX', 'en-US', 54, 'Christmas Island', 'Christmas Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CY', 'en-US', 55, 'Cyprus', 'Cyprus');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CZ', 'en-US', 56, 'Czechia', 'Czechia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DE', 'en-US', 57, 'Germany', 'Germany');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DJ', 'en-US', 58, 'Djibouti', 'Djibouti');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DK', 'en-US', 59, 'Denmark', 'Denmark');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DM', 'en-US', 60, 'Dominica', 'Dominica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DO', 'en-US', 61, 'Dominican Republic', 'Dominican Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DZ', 'en-US', 62, 'Algeria', 'Algeria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EC', 'en-US', 63, 'Ecuador', 'Ecuador');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EE', 'en-US', 64, 'Estonia', 'Estonia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EG', 'en-US', 65, 'Egypt', 'Egypt');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EH', 'en-US', 66, 'Western Sahara', 'Western Sahara');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ER', 'en-US', 67, 'Eritrea', 'Eritrea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ES', 'en-US', 68, 'Spain', 'Spain');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ET', 'en-US', 69, 'Ethiopia', 'Ethiopia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FI', 'en-US', 70, 'Finland', 'Finland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FJ', 'en-US', 71, 'Fiji', 'Fiji');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FK', 'en-US', 72, 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FM', 'en-US', 73, 'Micronesia, Federated States of', 'Micronesia, Federated States of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FO', 'en-US', 74, 'Faroe Islands', 'Faroe Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FR', 'en-US', 75, 'France', 'France');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GA', 'en-US', 76, 'Gabon', 'Gabon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GB', 'en-US', 77, 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GD', 'en-US', 78, 'Grenada', 'Grenada');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GE', 'en-US', 79, 'Georgia', 'Georgia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GF', 'en-US', 80, 'French Guiana', 'French Guiana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GG', 'en-US', 81, 'Guernsey', 'Guernsey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GH', 'en-US', 82, 'Ghana', 'Ghana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GI', 'en-US', 83, 'Gibraltar', 'Gibraltar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GL', 'en-US', 84, 'Greenland', 'Greenland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GM', 'en-US', 85, 'Gambia', 'Gambia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GN', 'en-US', 86, 'Guinea', 'Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GP', 'en-US', 87, 'Guadeloupe', 'Guadeloupe');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GQ', 'en-US', 88, 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GR', 'en-US', 89, 'Greece', 'Greece');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GS', 'en-US', 90, 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GT', 'en-US', 91, 'Guatemala', 'Guatemala');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GU', 'en-US', 92, 'Guam', 'Guam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GW', 'en-US', 93, 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GY', 'en-US', 94, 'Guyana', 'Guyana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HK', 'en-US', 95, 'Hong Kong', 'Hong Kong');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HM', 'en-US', 96, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HN', 'en-US', 97, 'Honduras', 'Honduras');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HR', 'en-US', 98, 'Croatia', 'Croatia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HT', 'en-US', 99, 'Haiti', 'Haiti');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HU', 'en-US', 100, 'Hungary', 'Hungary');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ID', 'en-US', 101, 'Indonesia', 'Indonesia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IE', 'en-US', 102, 'Ireland', 'Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IL', 'en-US', 103, 'Israel', 'Israel');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IM', 'en-US', 104, 'Isle of Man', 'Isle of Man');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IN', 'en-US', 105, 'India', 'India');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IO', 'en-US', 106, 'British Indian Ocean Territory', 'British Indian Ocean Territory');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IQ', 'en-US', 107, 'Iraq', 'Iraq');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IR', 'en-US', 108, 'Iran, Islamic Republic of', 'Iran, Islamic Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IS', 'en-US', 109, 'Iceland', 'Iceland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IT', 'en-US', 110, 'Italy', 'Italy');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JE', 'en-US', 111, 'Jersey', 'Jersey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JM', 'en-US', 112, 'Jamaica', 'Jamaica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JO', 'en-US', 113, 'Jordan', 'Jordan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JP', 'en-US', 114, 'Japan', 'Japan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KE', 'en-US', 115, 'Kenya', 'Kenya');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KG', 'en-US', 116, 'Kyrgyzstan', 'Kyrgyzstan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KH', 'en-US', 117, 'Cambodia', 'Cambodia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KI', 'en-US', 118, 'Kiribati', 'Kiribati');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KM', 'en-US', 119, 'Comoros', 'Comoros');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KN', 'en-US', 120, 'Saint Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KP', 'en-US', 121, 'Korea, Democratic People''s Republic of', 'Korea, Democratic People''s Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KR', 'en-US', 122, 'Korea, Republic of', 'Korea, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KW', 'en-US', 123, 'Kuwait', 'Kuwait');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KY', 'en-US', 124, 'Cayman Islands', 'Cayman Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KZ', 'en-US', 125, 'Kazakhstan', 'Kazakhstan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LA', 'en-US', 126, 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LB', 'en-US', 127, 'Lebanon', 'Lebanon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LC', 'en-US', 128, 'Saint Lucia', 'Saint Lucia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LI', 'en-US', 129, 'Liechtenstein', 'Liechtenstein');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LK', 'en-US', 130, 'Sri Lanka', 'Sri Lanka');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LR', 'en-US', 131, 'Liberia', 'Liberia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LS', 'en-US', 132, 'Lesotho', 'Lesotho');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LT', 'en-US', 133, 'Lithuania', 'Lithuania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LU', 'en-US', 134, 'Luxembourg', 'Luxembourg');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LV', 'en-US', 135, 'Latvia', 'Latvia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LY', 'en-US', 136, 'Libya', 'Libya');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MA', 'en-US', 137, 'Morocco', 'Morocco');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MC', 'en-US', 138, 'Monaco', 'Monaco');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MD', 'en-US', 139, 'Moldova, Republic of', 'Moldova, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ME', 'en-US', 140, 'Montenegro', 'Montenegro');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MF', 'en-US', 141, 'Saint Martin (French part)', 'Saint Martin (French part)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MG', 'en-US', 142, 'Madagascar', 'Madagascar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MH', 'en-US', 143, 'Marshall Islands', 'Marshall Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MK', 'en-US', 144, 'Macedonia, the former Yugoslav Republic of', 'Macedonia, the former Yugoslav Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ML', 'en-US', 145, 'Mali', 'Mali');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MM', 'en-US', 146, 'Myanmar', 'Myanmar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MN', 'en-US', 147, 'Mongolia', 'Mongolia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MO', 'en-US', 148, 'Macao', 'Macao');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MP', 'en-US', 149, 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MQ', 'en-US', 150, 'Martinique', 'Martinique');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MR', 'en-US', 151, 'Mauritania', 'Mauritania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MS', 'en-US', 152, 'Montserrat', 'Montserrat');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MT', 'en-US', 153, 'Malta', 'Malta');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MU', 'en-US', 154, 'Mauritius', 'Mauritius');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MV', 'en-US', 155, 'Maldives', 'Maldives');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MW', 'en-US', 156, 'Malawi', 'Malawi');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MX', 'en-US', 157, 'Mexico', 'Mexico');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MY', 'en-US', 158, 'Malaysia', 'Malaysia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MZ', 'en-US', 159, 'Mozambique', 'Mozambique');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NA', 'en-US', 160, 'Namibia', 'Namibia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NC', 'en-US', 161, 'New Caledonia', 'New Caledonia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NE', 'en-US', 162, 'Niger', 'Niger');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NF', 'en-US', 163, 'Norfolk Island', 'Norfolk Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NG', 'en-US', 164, 'Nigeria', 'Nigeria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NI', 'en-US', 165, 'Nicaragua', 'Nicaragua');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NL', 'en-US', 166, 'Netherlands', 'Netherlands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NO', 'en-US', 167, 'Norway', 'Norway');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NP', 'en-US', 168, 'Nepal', 'Nepal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NR', 'en-US', 169, 'Nauru', 'Nauru');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NU', 'en-US', 170, 'Niue', 'Niue');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NZ', 'en-US', 171, 'New Zealand', 'New Zealand');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('OM', 'en-US', 172, 'Oman', 'Oman');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PA', 'en-US', 173, 'Panama', 'Panama');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PE', 'en-US', 174, 'Peru', 'Peru');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PF', 'en-US', 175, 'French Polynesia', 'French Polynesia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PG', 'en-US', 176, 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PH', 'en-US', 177, 'Philippines', 'Philippines');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PK', 'en-US', 178, 'Pakistan', 'Pakistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PL', 'en-US', 179, 'Poland', 'Poland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PM', 'en-US', 180, 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PN', 'en-US', 181, 'Pitcairn', 'Pitcairn');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PR', 'en-US', 182, 'Puerto Rico', 'Puerto Rico');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PS', 'en-US', 183, 'Palestine, State of', 'Palestine, State of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PT', 'en-US', 184, 'Portugal', 'Portugal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PW', 'en-US', 185, 'Palau', 'Palau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PY', 'en-US', 186, 'Paraguay', 'Paraguay');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('QA', 'en-US', 187, 'Qatar', 'Qatar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RE', 'en-US', 188, 'Réunion', 'Réunion');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RO', 'en-US', 189, 'Romania', 'Romania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RS', 'en-US', 190, 'Serbia', 'Serbia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RU', 'en-US', 191, 'Russian Federation', 'Russian Federation');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RW', 'en-US', 192, 'Rwanda', 'Rwanda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SA', 'en-US', 193, 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SB', 'en-US', 194, 'Solomon Islands', 'Solomon Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SC', 'en-US', 195, 'Seychelles', 'Seychelles');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SD', 'en-US', 196, 'Sudan', 'Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SE', 'en-US', 197, 'Sweden', 'Sweden');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SG', 'en-US', 198, 'Singapore', 'Singapore');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SH', 'en-US', 199, 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SI', 'en-US', 200, 'Slovenia', 'Slovenia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SJ', 'en-US', 201, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SK', 'en-US', 202, 'Slovakia', 'Slovakia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SL', 'en-US', 203, 'Sierra Leone', 'Sierra Leone');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SM', 'en-US', 204, 'San Marino', 'San Marino');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SN', 'en-US', 205, 'Senegal', 'Senegal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SO', 'en-US', 206, 'Somalia', 'Somalia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SR', 'en-US', 207, 'Suriname', 'Suriname');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SS', 'en-US', 208, 'South Sudan', 'South Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ST', 'en-US', 209, 'Sao Tome and Principe', 'Sao Tome and Principe');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SV', 'en-US', 210, 'El Salvador', 'El Salvador');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SX', 'en-US', 211, 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SY', 'en-US', 212, 'Syrian Arab Republic', 'Syrian Arab Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SZ', 'en-US', 213, 'Swaziland', 'Swaziland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TC', 'en-US', 214, 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TD', 'en-US', 215, 'Chad', 'Chad');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TF', 'en-US', 216, 'French Southern Territories', 'French Southern Territories');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TG', 'en-US', 217, 'Togo', 'Togo');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TH', 'en-US', 218, 'Thailand', 'Thailand');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TJ', 'en-US', 219, 'Tajikistan', 'Tajikistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TK', 'en-US', 220, 'Tokelau', 'Tokelau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TL', 'en-US', 221, 'Timor-Leste', 'Timor-Leste');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TM', 'en-US', 222, 'Turkmenistan', 'Turkmenistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TN', 'en-US', 223, 'Tunisia', 'Tunisia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TO', 'en-US', 224, 'Tonga', 'Tonga');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TR', 'en-US', 225, 'Turkey', 'Turkey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TT', 'en-US', 226, 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TV', 'en-US', 227, 'Tuvalu', 'Tuvalu');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TW', 'en-US', 228, 'Taiwan, Province of China', 'Taiwan, Province of China');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TZ', 'en-US', 229, 'Tanzania, United Republic of', 'Tanzania, United Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UA', 'en-US', 230, 'Ukraine', 'Ukraine');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UG', 'en-US', 231, 'Uganda', 'Uganda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UM', 'en-US', 232, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('US', 'en-US', 233, 'United States of America', 'United States of America');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UY', 'en-US', 234, 'Uruguay', 'Uruguay');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UZ', 'en-US', 235, 'Uzbekistan', 'Uzbekistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VA', 'en-US', 236, 'Holy See', 'Holy See');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VC', 'en-US', 237, 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VE', 'en-US', 238, 'Venezuela', 'Venezuela, Bolivarian Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VG', 'en-US', 239, 'Virgin Islands, British', 'Virgin Islands, British');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VI', 'en-US', 240, 'Virgin Islands, U.S.', 'Virgin Islands, U.S.');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VN', 'en-US', 241, 'Viet Nam', 'Viet Nam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VU', 'en-US', 242, 'Vanuatu', 'Vanuatu');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('WF', 'en-US', 243, 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('WS', 'en-US', 244, 'Samoa', 'Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('YE', 'en-US', 245, 'Yemen', 'Yemen');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('YT', 'en-US', 246, 'Mayotte', 'Mayotte');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZA', 'en-US', 247, 'South Africa', 'South Africa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZM', 'en-US', 248, 'Zambia', 'Zambia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZW', 'en-US', 249, 'Zimbabwe', 'Zimbabwe');
   
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AD', 'en-ZA', 1, 'Andorra', 'Andorra');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AE', 'en-ZA', 2, 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AF', 'en-ZA', 3, 'Afghanistan', 'Afghanistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AG', 'en-ZA', 4, 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AI', 'en-ZA', 5, 'Anguilla', 'Anguilla');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AL', 'en-ZA', 6, 'Albania', 'Albania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AM', 'en-ZA', 7, 'Armenia', 'Armenia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AO', 'en-ZA', 8, 'Angola', 'Angola');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AQ', 'en-ZA', 9, 'Antarctica', 'Antarctica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AR', 'en-ZA', 10, 'Argentina', 'Argentina');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AS', 'en-ZA', 11, 'American Samoa', 'American Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AT', 'en-ZA', 12, 'Austria', 'Austria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AU', 'en-ZA', 13, 'Australia', 'Australia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AW', 'en-ZA', 14, 'Aruba', 'Aruba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AX', 'en-ZA', 15, 'Åland Islands', 'Åland Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('AZ', 'en-ZA', 16, 'Azerbaijan', 'Azerbaijan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BA', 'en-ZA', 17, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BB', 'en-ZA', 18, 'Barbados', 'Barbados');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BD', 'en-ZA', 19, 'Bangladesh', 'Bangladesh');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BE', 'en-ZA', 20, 'Belgium', 'Belgium');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BF', 'en-ZA', 21, 'Burkina Faso', 'Burkina Faso');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BG', 'en-ZA', 22, 'Bulgaria', 'Bulgaria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BH', 'en-ZA', 23, 'Bahrain', 'Bahrain');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BI', 'en-ZA', 24, 'Burundi', 'Burundi');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BJ', 'en-ZA', 25, 'Benin', 'Benin');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BL', 'en-ZA', 26, 'Saint Barthélemy', 'Saint Barthélemy');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BM', 'en-ZA', 27, 'Bermuda', 'Bermuda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BN', 'en-ZA', 28, 'Brunei Darussalam', 'Brunei Darussalam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BO', 'en-ZA', 29, 'Bolivia, Plurinational State of', 'Bolivia, Plurinational State of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BQ', 'en-ZA', 30, 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BR', 'en-ZA', 31, 'Brazil', 'Brazil');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BS', 'en-ZA', 32, 'Bahamas', 'Bahamas');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BT', 'en-ZA', 33, 'Bhutan', 'Bhutan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BV', 'en-ZA', 34, 'Bouvet Island', 'Bouvet Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BW', 'en-ZA', 35, 'Botswana', 'Botswana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BY', 'en-ZA', 36, 'Belarus', 'Belarus');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('BZ', 'en-ZA', 37, 'Belize', 'Belize');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CA', 'en-ZA', 38, 'Canada', 'Canada');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CC', 'en-ZA', 39, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CD', 'en-ZA', 40, 'Congo, the Democratic Republic of the', 'Congo, the Democratic Republic of the');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CF', 'en-ZA', 41, 'Central African Republic', 'Central African Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CG', 'en-ZA', 42, 'Congo', 'Congo');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CH', 'en-ZA', 43, 'Switzerland', 'Switzerland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CI', 'en-ZA', 44, 'Côte d''Ivoire', 'Côte d''Ivoire');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CK', 'en-ZA', 45, 'Cook Islands', 'Cook Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CL', 'en-ZA', 46, 'Chile', 'Chile');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CM', 'en-ZA', 47, 'Cameroon', 'Cameroon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CN', 'en-ZA', 48, 'China', 'China');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CO', 'en-ZA', 49, 'Colombia', 'Colombia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CR', 'en-ZA', 50, 'Costa Rica', 'Costa Rica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CU', 'en-ZA', 51, 'Cuba', 'Cuba');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CV', 'en-ZA', 52, 'Cabo Verde', 'Cabo Verde');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CW', 'en-ZA', 53, 'Curaçao', 'Curaçao');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CX', 'en-ZA', 54, 'Christmas Island', 'Christmas Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CY', 'en-ZA', 55, 'Cyprus', 'Cyprus');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('CZ', 'en-ZA', 56, 'Czechia', 'Czechia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DE', 'en-ZA', 57, 'Germany', 'Germany');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DJ', 'en-ZA', 58, 'Djibouti', 'Djibouti');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DK', 'en-ZA', 59, 'Denmark', 'Denmark');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DM', 'en-ZA', 60, 'Dominica', 'Dominica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DO', 'en-ZA', 61, 'Dominican Republic', 'Dominican Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('DZ', 'en-ZA', 62, 'Algeria', 'Algeria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EC', 'en-ZA', 63, 'Ecuador', 'Ecuador');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EE', 'en-ZA', 64, 'Estonia', 'Estonia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EG', 'en-ZA', 65, 'Egypt', 'Egypt');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('EH', 'en-ZA', 66, 'Western Sahara', 'Western Sahara');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ER', 'en-ZA', 67, 'Eritrea', 'Eritrea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ES', 'en-ZA', 68, 'Spain', 'Spain');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ET', 'en-ZA', 69, 'Ethiopia', 'Ethiopia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FI', 'en-ZA', 70, 'Finland', 'Finland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FJ', 'en-ZA', 71, 'Fiji', 'Fiji');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FK', 'en-ZA', 72, 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FM', 'en-ZA', 73, 'Micronesia, Federated States of', 'Micronesia, Federated States of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FO', 'en-ZA', 74, 'Faroe Islands', 'Faroe Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('FR', 'en-ZA', 75, 'France', 'France');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GA', 'en-ZA', 76, 'Gabon', 'Gabon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GB', 'en-ZA', 77, 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GD', 'en-ZA', 78, 'Grenada', 'Grenada');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GE', 'en-ZA', 79, 'Georgia', 'Georgia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GF', 'en-ZA', 80, 'French Guiana', 'French Guiana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GG', 'en-ZA', 81, 'Guernsey', 'Guernsey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GH', 'en-ZA', 82, 'Ghana', 'Ghana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GI', 'en-ZA', 83, 'Gibraltar', 'Gibraltar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GL', 'en-ZA', 84, 'Greenland', 'Greenland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GM', 'en-ZA', 85, 'Gambia', 'Gambia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GN', 'en-ZA', 86, 'Guinea', 'Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GP', 'en-ZA', 87, 'Guadeloupe', 'Guadeloupe');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GQ', 'en-ZA', 88, 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GR', 'en-ZA', 89, 'Greece', 'Greece');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GS', 'en-ZA', 90, 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GT', 'en-ZA', 91, 'Guatemala', 'Guatemala');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GU', 'en-ZA', 92, 'Guam', 'Guam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GW', 'en-ZA', 93, 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('GY', 'en-ZA', 94, 'Guyana', 'Guyana');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HK', 'en-ZA', 95, 'Hong Kong', 'Hong Kong');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HM', 'en-ZA', 96, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HN', 'en-ZA', 97, 'Honduras', 'Honduras');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HR', 'en-ZA', 98, 'Croatia', 'Croatia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HT', 'en-ZA', 99, 'Haiti', 'Haiti');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('HU', 'en-ZA', 100, 'Hungary', 'Hungary');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ID', 'en-ZA', 101, 'Indonesia', 'Indonesia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IE', 'en-ZA', 102, 'Ireland', 'Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IL', 'en-ZA', 103, 'Israel', 'Israel');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IM', 'en-ZA', 104, 'Isle of Man', 'Isle of Man');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IN', 'en-ZA', 105, 'India', 'India');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IO', 'en-ZA', 106, 'British Indian Ocean Territory', 'British Indian Ocean Territory');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IQ', 'en-ZA', 107, 'Iraq', 'Iraq');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IR', 'en-ZA', 108, 'Iran, Islamic Republic of', 'Iran, Islamic Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IS', 'en-ZA', 109, 'Iceland', 'Iceland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('IT', 'en-ZA', 110, 'Italy', 'Italy');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JE', 'en-ZA', 111, 'Jersey', 'Jersey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JM', 'en-ZA', 112, 'Jamaica', 'Jamaica');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JO', 'en-ZA', 113, 'Jordan', 'Jordan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('JP', 'en-ZA', 114, 'Japan', 'Japan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KE', 'en-ZA', 115, 'Kenya', 'Kenya');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KG', 'en-ZA', 116, 'Kyrgyzstan', 'Kyrgyzstan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KH', 'en-ZA', 117, 'Cambodia', 'Cambodia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KI', 'en-ZA', 118, 'Kiribati', 'Kiribati');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KM', 'en-ZA', 119, 'Comoros', 'Comoros');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KN', 'en-ZA', 120, 'Saint Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KP', 'en-ZA', 121, 'Korea, Democratic People''s Republic of', 'Korea, Democratic People''s Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KR', 'en-ZA', 122, 'Korea, Republic of', 'Korea, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KW', 'en-ZA', 123, 'Kuwait', 'Kuwait');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KY', 'en-ZA', 124, 'Cayman Islands', 'Cayman Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('KZ', 'en-ZA', 125, 'Kazakhstan', 'Kazakhstan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LA', 'en-ZA', 126, 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LB', 'en-ZA', 127, 'Lebanon', 'Lebanon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LC', 'en-ZA', 128, 'Saint Lucia', 'Saint Lucia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LI', 'en-ZA', 129, 'Liechtenstein', 'Liechtenstein');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LK', 'en-ZA', 130, 'Sri Lanka', 'Sri Lanka');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LR', 'en-ZA', 131, 'Liberia', 'Liberia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LS', 'en-ZA', 132, 'Lesotho', 'Lesotho');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LT', 'en-ZA', 133, 'Lithuania', 'Lithuania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LU', 'en-ZA', 134, 'Luxembourg', 'Luxembourg');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LV', 'en-ZA', 135, 'Latvia', 'Latvia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('LY', 'en-ZA', 136, 'Libya', 'Libya');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MA', 'en-ZA', 137, 'Morocco', 'Morocco');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MC', 'en-ZA', 138, 'Monaco', 'Monaco');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MD', 'en-ZA', 139, 'Moldova, Republic of', 'Moldova, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ME', 'en-ZA', 140, 'Montenegro', 'Montenegro');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MF', 'en-ZA', 141, 'Saint Martin (French part)', 'Saint Martin (French part)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MG', 'en-ZA', 142, 'Madagascar', 'Madagascar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MH', 'en-ZA', 143, 'Marshall Islands', 'Marshall Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MK', 'en-ZA', 144, 'Macedonia, the former Yugoslav Republic of', 'Macedonia, the former Yugoslav Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ML', 'en-ZA', 145, 'Mali', 'Mali');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MM', 'en-ZA', 146, 'Myanmar', 'Myanmar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MN', 'en-ZA', 147, 'Mongolia', 'Mongolia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MO', 'en-ZA', 148, 'Macao', 'Macao');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MP', 'en-ZA', 149, 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MQ', 'en-ZA', 150, 'Martinique', 'Martinique');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MR', 'en-ZA', 151, 'Mauritania', 'Mauritania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MS', 'en-ZA', 152, 'Montserrat', 'Montserrat');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MT', 'en-ZA', 153, 'Malta', 'Malta');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MU', 'en-ZA', 154, 'Mauritius', 'Mauritius');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MV', 'en-ZA', 155, 'Maldives', 'Maldives');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MW', 'en-ZA', 156, 'Malawi', 'Malawi');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MX', 'en-ZA', 157, 'Mexico', 'Mexico');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MY', 'en-ZA', 158, 'Malaysia', 'Malaysia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('MZ', 'en-ZA', 159, 'Mozambique', 'Mozambique');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NA', 'en-ZA', 160, 'Namibia', 'Namibia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NC', 'en-ZA', 161, 'New Caledonia', 'New Caledonia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NE', 'en-ZA', 162, 'Niger', 'Niger');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NF', 'en-ZA', 163, 'Norfolk Island', 'Norfolk Island');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NG', 'en-ZA', 164, 'Nigeria', 'Nigeria');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NI', 'en-ZA', 165, 'Nicaragua', 'Nicaragua');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NL', 'en-ZA', 166, 'Netherlands', 'Netherlands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NO', 'en-ZA', 167, 'Norway', 'Norway');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NP', 'en-ZA', 168, 'Nepal', 'Nepal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NR', 'en-ZA', 169, 'Nauru', 'Nauru');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NU', 'en-ZA', 170, 'Niue', 'Niue');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('NZ', 'en-ZA', 171, 'New Zealand', 'New Zealand');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('OM', 'en-ZA', 172, 'Oman', 'Oman');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PA', 'en-ZA', 173, 'Panama', 'Panama');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PE', 'en-ZA', 174, 'Peru', 'Peru');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PF', 'en-ZA', 175, 'French Polynesia', 'French Polynesia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PG', 'en-ZA', 176, 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PH', 'en-ZA', 177, 'Philippines', 'Philippines');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PK', 'en-ZA', 178, 'Pakistan', 'Pakistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PL', 'en-ZA', 179, 'Poland', 'Poland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PM', 'en-ZA', 180, 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PN', 'en-ZA', 181, 'Pitcairn', 'Pitcairn');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PR', 'en-ZA', 182, 'Puerto Rico', 'Puerto Rico');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PS', 'en-ZA', 183, 'Palestine, State of', 'Palestine, State of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PT', 'en-ZA', 184, 'Portugal', 'Portugal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PW', 'en-ZA', 185, 'Palau', 'Palau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('PY', 'en-ZA', 186, 'Paraguay', 'Paraguay');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('QA', 'en-ZA', 187, 'Qatar', 'Qatar');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RE', 'en-ZA', 188, 'Réunion', 'Réunion');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RO', 'en-ZA', 189, 'Romania', 'Romania');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RS', 'en-ZA', 190, 'Serbia', 'Serbia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RU', 'en-ZA', 191, 'Russian Federation', 'Russian Federation');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('RW', 'en-ZA', 192, 'Rwanda', 'Rwanda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SA', 'en-ZA', 193, 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SB', 'en-ZA', 194, 'Solomon Islands', 'Solomon Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SC', 'en-ZA', 195, 'Seychelles', 'Seychelles');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SD', 'en-ZA', 196, 'Sudan', 'Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SE', 'en-ZA', 197, 'Sweden', 'Sweden');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SG', 'en-ZA', 198, 'Singapore', 'Singapore');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SH', 'en-ZA', 199, 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SI', 'en-ZA', 200, 'Slovenia', 'Slovenia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SJ', 'en-ZA', 201, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SK', 'en-ZA', 202, 'Slovakia', 'Slovakia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SL', 'en-ZA', 203, 'Sierra Leone', 'Sierra Leone');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SM', 'en-ZA', 204, 'San Marino', 'San Marino');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SN', 'en-ZA', 205, 'Senegal', 'Senegal');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SO', 'en-ZA', 206, 'Somalia', 'Somalia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SR', 'en-ZA', 207, 'Suriname', 'Suriname');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SS', 'en-ZA', 208, 'South Sudan', 'South Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ST', 'en-ZA', 209, 'Sao Tome and Principe', 'Sao Tome and Principe');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SV', 'en-ZA', 210, 'El Salvador', 'El Salvador');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SX', 'en-ZA', 211, 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SY', 'en-ZA', 212, 'Syrian Arab Republic', 'Syrian Arab Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('SZ', 'en-ZA', 213, 'Swaziland', 'Swaziland');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TC', 'en-ZA', 214, 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TD', 'en-ZA', 215, 'Chad', 'Chad');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TF', 'en-ZA', 216, 'French Southern Territories', 'French Southern Territories');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TG', 'en-ZA', 217, 'Togo', 'Togo');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TH', 'en-ZA', 218, 'Thailand', 'Thailand');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TJ', 'en-ZA', 219, 'Tajikistan', 'Tajikistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TK', 'en-ZA', 220, 'Tokelau', 'Tokelau');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TL', 'en-ZA', 221, 'Timor-Leste', 'Timor-Leste');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TM', 'en-ZA', 222, 'Turkmenistan', 'Turkmenistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TN', 'en-ZA', 223, 'Tunisia', 'Tunisia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TO', 'en-ZA', 224, 'Tonga', 'Tonga');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TR', 'en-ZA', 225, 'Turkey', 'Turkey');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TT', 'en-ZA', 226, 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TV', 'en-ZA', 227, 'Tuvalu', 'Tuvalu');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TW', 'en-ZA', 228, 'Taiwan, Province of China', 'Taiwan, Province of China');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('TZ', 'en-ZA', 229, 'Tanzania, United Republic of', 'Tanzania, United Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UA', 'en-ZA', 230, 'Ukraine', 'Ukraine');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UG', 'en-ZA', 231, 'Uganda', 'Uganda');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UM', 'en-ZA', 232, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('US', 'en-ZA', 233, 'United States of America', 'United States of America');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UY', 'en-ZA', 234, 'Uruguay', 'Uruguay');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('UZ', 'en-ZA', 235, 'Uzbekistan', 'Uzbekistan');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VA', 'en-ZA', 236, 'Holy See', 'Holy See');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VC', 'en-ZA', 237, 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VE', 'en-ZA', 238, 'Venezuela', 'Venezuela, Bolivarian Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VG', 'en-ZA', 239, 'Virgin Islands, British', 'Virgin Islands, British');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VI', 'en-ZA', 240, 'Virgin Islands, U.S.', 'Virgin Islands, U.S.');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VN', 'en-ZA', 241, 'Viet Nam', 'Viet Nam');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('VU', 'en-ZA', 242, 'Vanuatu', 'Vanuatu');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('WF', 'en-ZA', 243, 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('WS', 'en-ZA', 244, 'Samoa', 'Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('YE', 'en-ZA', 245, 'Yemen', 'Yemen');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('YT', 'en-ZA', 246, 'Mayotte', 'Mayotte');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZA', 'en-ZA', 247, 'South Africa', 'South Africa');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZM', 'en-ZA', 248, 'Zambia', 'Zambia');
INSERT INTO reference.countries (code, locale, sort_index, name, description)
   VALUES ('ZW', 'en-ZA', 249, 'Zimbabwe', 'Zimbabwe');

INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 3, 'Not known', 'Not known');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Employed', 'Employed');

INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 3, 'Not known', 'Not known');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Employed', 'Employed');
  
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('0', '0', 'en-US', 13, 'Not known', 'Not known');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '1', 'en-US', 1, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '2', 'en-US', 2, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '3', 'en-US', 3, 'Housewife/Home Executive', 'Housewife/Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '4', 'en-US', 4, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '5', 'en-US', 5, 'Dismissed', 'Dismissed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '6', 'en-US', 6, 'Suspended', 'Suspended');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '7', 'en-US', 7, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '8', 'en-US', 8, 'Unspecified', 'Unspecified');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '1', 'en-US', 9, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '2', 'en-US', 10, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '3', 'en-US', 11, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '4', 'en-US', 12, 'Self-employed', 'Self-employed');  

INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('0', '0', 'en-ZA', 13, 'Not known', 'Not known');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '1', 'en-ZA', 1, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '2', 'en-ZA', 2, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '3', 'en-ZA', 3, 'Housewife/Home Executive', 'Housewife/Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '4', 'en-ZA', 4, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '5', 'en-ZA', 5, 'Dismissed', 'Dismissed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '6', 'en-ZA', 6, 'Suspended', 'Suspended');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '7', 'en-ZA', 7, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '8', 'en-ZA', 8, 'Unspecified', 'Unspecified');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '1', 'en-ZA', 9, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '2', 'en-ZA', 10, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '3', 'en-ZA', 11, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '4', 'en-ZA', 12, 'Self-employed', 'Self-employed');

INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 6, 'Not known', 'Not known');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('3', 'en-US', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('4', 'en-US', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('9', 'en-US', 5, 'Not applicable', 'Not applicable');

INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 6, 'Not known', 'Not known');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('3', 'en-ZA', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('4', 'en-ZA', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('9', 'en-ZA', 5, 'Not applicable', 'Not applicable');

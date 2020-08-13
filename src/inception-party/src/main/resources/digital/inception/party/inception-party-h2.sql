-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA party;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE party.party (
  id   UUID         NOT NULL,
  type INTEGER      NOT NULL,
  name VARCHAR(100) NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN party.party.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party';

COMMENT ON COLUMN party.party.type IS 'The code identifying the type of party';

COMMENT ON COLUMN party.party.type IS 'The name of the party';


CREATE TABLE party.person (
  id            UUID        NOT NULL,
  date_of_birth DATE        NOT NULL,
  gender        VARCHAR(20) NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN party.person.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person';

COMMENT ON COLUMN party.person.date_of_birth IS 'The date of the birth for the person';

COMMENT ON COLUMN party.person.gender IS 'The code identifying the gender for the person';


CREATE TABLE party.organization (
  id   UUID         NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN party.organization.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

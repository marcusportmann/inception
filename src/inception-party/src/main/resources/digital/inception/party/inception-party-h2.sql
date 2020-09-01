-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA party;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE party.parties (
  id      UUID         NOT NULL,
  type    INTEGER      NOT NULL,
  name    VARCHAR(100) NOT NULL,
  created TIMESTAMP    NOT NULL,
  updated TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN party.parties.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the independent party';

COMMENT ON COLUMN party.parties.type IS 'The code identifying the type of independent party';

COMMENT ON COLUMN party.parties.type IS 'The name of the independent party';

COMMENT ON COLUMN party.parties.created IS 'The date and time the party was created';

COMMENT ON COLUMN party.parties.updated IS 'The date and time the party was last updated';


CREATE TABLE party.persons (
  id               UUID          NOT NULL,
  preferred_name   VARCHAR(100),
  given_name       VARCHAR(100),
  surname          VARCHAR(100),
  gender           VARCHAR(20),
  date_of_birth    DATE,
  country_of_birth VARCHAR(10),
  created          TIMESTAMP     NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT persons_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX persons_date_of_birth_ix ON party.persons(date_of_birth);

COMMENT ON COLUMN party.persons.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person';

COMMENT ON COLUMN party.persons.preferred_name IS 'The optional preferred name for the person';

COMMENT ON COLUMN party.persons.given_name IS 'The optional given name for the person';

COMMENT ON COLUMN party.persons.surname IS 'The optional surname for the person';

COMMENT ON COLUMN party.persons.gender IS 'The optional code identifying the gender for the person';

COMMENT ON COLUMN party.persons.date_of_birth IS 'The optional date of the birth for the person';

COMMENT ON COLUMN party.persons.country_of_birth IS 'The optional code identifying the country of birth for the person';

COMMENT ON COLUMN party.persons.created IS 'The date and time the person was created';

COMMENT ON COLUMN party.persons.updated IS 'The date and time the person was last updated';


CREATE TABLE party.identity_documents (
  id            UUID        NOT NULL,
  person_id     UUID        NOT NULL,
  type          VARCHAR(10) NOT NULL,
  date_of_issue DATE        NOT NULL,
  created       TIMESTAMP   NOT NULL,
  updated       TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT identity_documents_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX identity_documents_person_id_ix ON party.identity_documents(person_id);

COMMENT ON COLUMN party.identity_documents.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the identity document';

COMMENT ON COLUMN party.identity_documents.person_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person the identity document is associated with';

COMMENT ON COLUMN party.identity_documents.type IS 'The code identifying the type of identity document';

COMMENT ON COLUMN party.identity_documents.date_of_issue IS 'The date of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.created IS 'The date and time the identity document was created';

COMMENT ON COLUMN party.identity_documents.updated IS 'The date and time the identity document was last updated';


CREATE TABLE party.organizations (
  id      UUID      NOT NULL,
  created TIMESTAMP NOT NULL,
  updated TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT organizations_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.organizations.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';

COMMENT ON COLUMN party.organizations.created IS 'The date and time the organization was created';

COMMENT ON COLUMN party.organizations.updated IS 'The date and time the organization was last updated';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

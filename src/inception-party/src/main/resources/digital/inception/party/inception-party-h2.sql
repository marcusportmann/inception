-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA party;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE party.parties (
  id   UUID         NOT NULL,
  type INTEGER      NOT NULL,
  name VARCHAR(100) NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN party.parties.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the independent party';

COMMENT ON COLUMN party.parties.type IS 'The code identifying the type of independent party';

COMMENT ON COLUMN party.parties.type IS 'The name of the independent party';


CREATE TABLE party.persons (
  id            UUID        NOT NULL,
  date_of_birth DATE        NOT NULL,
  gender        VARCHAR(20) NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT persons_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.persons.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person';

COMMENT ON COLUMN party.persons.date_of_birth IS 'The date of the birth for the person';

COMMENT ON COLUMN party.persons.gender IS 'The code identifying the gender for the person';


CREATE TABLE party.identity_documents (
  id            UUID        NOT NULL,
  person_id     UUID        NOT NULL,
  type          VARCHAR(10) NOT NULL,
  date_of_issue DATE        NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT identity_documents_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX identity_documents_person_id_ix ON party.identity_documents(person_id);

COMMENT ON COLUMN party.identity_documents.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the identity document';

COMMENT ON COLUMN party.identity_documents.person_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person the identity document is associated with';

COMMENT ON COLUMN party.identity_documents.type IS 'The code identifying the type of identity document';

COMMENT ON COLUMN party.identity_documents.date_of_issue IS 'The date of issue for the identity document';




CREATE TABLE party.organizations (
  id   UUID         NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT organizations_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.organizations.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

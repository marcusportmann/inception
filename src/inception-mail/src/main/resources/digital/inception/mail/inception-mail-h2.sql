-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA mail;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE mail.mail_templates (
  id           VARCHAR(100) NOT NULL,
  name         VARCHAR(100) NOT NULL,
  content_type INTEGER      NOT NULL,
  template     BLOB         NOT NULL,
  updated      TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN mail.mail_templates.id IS 'The ID uniquely identifying the mail template';

COMMENT ON COLUMN mail.mail_templates.name IS 'The name of the mail template';

COMMENT ON COLUMN mail.mail_templates.content_type IS 'The content type for the mail template';

COMMENT ON COLUMN mail.mail_templates.template IS 'The Apache FreeMarker template for the mail template';

COMMENT ON COLUMN mail.mail_templates.updated IS 'The date and time the mail template was last updated';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

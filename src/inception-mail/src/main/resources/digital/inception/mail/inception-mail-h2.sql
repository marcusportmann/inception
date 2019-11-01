-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA mail;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE mail.mail_templates (
  id           UUID         NOT NULL,
  name         VARCHAR(100) NOT NULL,
  content_type INTEGER      NOT NULL,
  template     BLOB         NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN mail.mail_templates.id IS 'The ID used to uniquely identify the mail template';

COMMENT ON COLUMN mail.mail_templates.name IS 'The name of the mail template';

COMMENT ON COLUMN mail.mail_templates.content_type IS 'The content type for the mail template';

COMMENT ON COLUMN mail.mail_templates.template IS 'The Apache FreeMarker template for the mail template';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------

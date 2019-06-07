-- -------------------------------------------------------------------------------------------------
--  Execute the following command to start the database server if it is not running:
--
--    OS X: pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/postgres.log start
--    OS X (Homebrew): brew services start postgresql
--    CentOS (as root): service postgresql-9.6 start
--
--  Execute the following command to create the database:
--
--    OS X: createdb  --template=template0 --encoding=UTF8 sample
--    CentOS (as root): sudo su postgres -c 'createdb --template=template1 --encoding=UTF8 sample'
--
--  Execute the following command to initialise the database:
--
--    OS X: psql -d sample -f sample-postgres.sql
--    CentOS (as root): su postgres -c 'psql -d sample -f sample-postgres.sql'
--
--  Execute the following command to delete the database:
--
--    OS X: dropdb sample
--    CentOS (as root): su postgres -c 'dropdb sample'
--
--  Execute the following command to clean-up unreferenced large objects on the database:
--
--    OS X: vacuumlo sample
--    CentOS (as root): su postgres -c 'vacuumlo sample'
--
-- -------------------------------------------------------------------------------------------------
SET client_min_messages = 'warning';

-- -------------------------------------------------------------------------------------------------
-- DROP TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS sample.data CASCADE;
DROP TABLE IF EXISTS error.error_reports CASCADE;
DROP TABLE IF EXISTS sms.sms CASCADE;
DROP TABLE IF EXISTS reporting.report_definitions CASCADE;
DROP TABLE IF EXISTS messaging.archived_messages CASCADE;
DROP TABLE IF EXISTS messaging.message_parts CASCADE;
DROP TABLE IF EXISTS messaging.messages CASCADE;
DROP TABLE IF EXISTS messaging.message_statuses CASCADE;
DROP TABLE IF EXISTS messaging.message_types CASCADE;
DROP TABLE IF EXISTS codes.codes CASCADE;
DROP TABLE IF EXISTS codes.code_categories CASCADE;
DROP TABLE IF EXISTS scheduler.job_parameters CASCADE;
DROP TABLE IF EXISTS scheduler.jobs CASCADE;
DROP TABLE IF EXISTS security.role_to_group_map CASCADE;
DROP TABLE IF EXISTS security.function_to_role_map CASCADE;
DROP TABLE IF EXISTS security.roles CASCADE;
DROP TABLE IF EXISTS security.functions CASCADE;
DROP TABLE IF EXISTS security.groups CASCADE;
DROP TABLE IF EXISTS security.internal_user_to_internal_group_map CASCADE;
DROP TABLE IF EXISTS security.internal_groups CASCADE;
DROP TABLE IF EXISTS security.internal_users_password_history CASCADE;
DROP TABLE IF EXISTS security.internal_users CASCADE;
DROP TABLE IF EXISTS security.user_directory_to_organization_map CASCADE;
DROP TABLE IF EXISTS security.user_directories CASCADE;
DROP TABLE IF EXISTS security.user_directory_types CASCADE;
DROP TABLE IF EXISTS security.organizations CASCADE;
DROP TABLE IF EXISTS service_registry.service_registry CASCADE;
DROP TABLE IF EXISTS configuration.configuration CASCADE;
DROP TABLE IF EXISTS idgenerator.idgenerator CASCADE;
DROP TABLE IF EXISTS test.test_data CASCADE;

-- -------------------------------------------------------------------------------------------------
-- DROP SCHEMAS
-- -------------------------------------------------------------------------------------------------
DROP SCHEMA IF EXISTS sample CASCADE;
DROP SCHEMA IF EXISTS test CASCADE;
DROP SCHEMA IF EXISTS error CASCADE;
DROP SCHEMA IF EXISTS sms CASCADE;
DROP SCHEMA IF EXISTS service_registry CASCADE;
DROP SCHEMA IF EXISTS security CASCADE;
DROP SCHEMA IF EXISTS scheduler CASCADE;
DROP SCHEMA IF EXISTS reporting CASCADE;
DROP SCHEMA IF EXISTS messaging CASCADE;
DROP SCHEMA IF EXISTS idgenerator CASCADE;
DROP SCHEMA IF EXISTS configuration CASCADE;
DROP SCHEMA IF EXISTS codes CASCADE;

-- -------------------------------------------------------------------------------------------------
-- DROP ROLES
-- -------------------------------------------------------------------------------------------------
DROP
OWNED BY sample CASCADE;
DROP ROLE IF EXISTS sample;

-- -------------------------------------------------------------------------------------------------
-- CREATE ROLES
-- -------------------------------------------------------------------------------------------------
CREATE ROLE sample WITH LOGIN
  PASSWORD 'Password1';
ALTER
ROLE sample
WITH login;

-- -------------------------------------------------------------------------------------------------
-- CREATE PROCEDURES
-- -------------------------------------------------------------------------------------------------
--
-- CREATE OR REPLACE FUNCTION bytea_import(p_path text, p_result out bytea)
--                    language plpgsql as $$
-- declare
--   l_oid oid;
--   r record;
-- begin
--   p_result := '';
--   select lo_import(p_path) into l_oid;
--   for r in ( select data
--              from pg_largeobject
--              where loid = l_oid
--              order by pageno ) loop
--     p_result = p_result || r.data;
--   end loop;
--   perform lo_unlink(l_oid);
-- end;$$;


-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA codes;
CREATE SCHEMA configuration;
CREATE SCHEMA idgenerator;
CREATE SCHEMA messaging;
CREATE SCHEMA reporting;
CREATE SCHEMA scheduler;
CREATE SCHEMA security;
CREATE SCHEMA service_registry;
CREATE SCHEMA sms;
CREATE SCHEMA error;
CREATE SCHEMA test;
CREATE SCHEMA sample;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE codes.code_categories (
  id      TEXT NOT NULL,
  name    TEXT NOT NULL,
  data    TEXT,
  updated TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN codes.code_categories.id IS 'The ID used to uniquely identify the code category';

COMMENT ON COLUMN codes.code_categories.name IS 'The name of the code category';

COMMENT ON COLUMN codes.code_categories.data IS 'The code data for the code category';

COMMENT ON COLUMN codes.code_categories.updated IS 'The date and time the code category was updated';


CREATE TABLE codes.codes (
  id               TEXT NOT NULL,
  code_category_id TEXT NOT NULL,
  name             TEXT NOT NULL,
  value            TEXT NOT NULL,

  PRIMARY KEY (id, code_category_id),
  CONSTRAINT codes_code_category_fk FOREIGN KEY (code_category_id) REFERENCES codes.code_categories(id) ON DELETE CASCADE
);

CREATE INDEX codes_code_category_id_ix ON codes.codes(code_category_id);

COMMENT ON COLUMN codes.codes.id IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN codes.codes.code_category_id IS 'The ID used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN codes.codes.name IS 'The name of the code';

COMMENT ON COLUMN codes.codes.value IS 'The value for the code';


CREATE TABLE configuration.configuration (
  key         TEXT NOT NULL,
  value       TEXT NOT NULL,
  description TEXT NOT NULL,

  PRIMARY KEY (key)
);

COMMENT ON COLUMN configuration.configuration.key IS 'The key used to uniquely identify the configuration value';

COMMENT ON COLUMN configuration.configuration.value IS 'The value for the configuration value';

COMMENT ON COLUMN configuration.configuration.description IS 'The description for the configuration value';


CREATE TABLE idgenerator.idgenerator (
  name    TEXT NOT NULL,
  current BIGINT DEFAULT 0,

  PRIMARY KEY (name)
);

COMMENT ON COLUMN idgenerator.idgenerator.name IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN idgenerator.idgenerator.current IS 'The current ID for the type';


CREATE TABLE messaging.message_types (
  id   UUID NOT NULL,
  name TEXT NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN messaging.message_types.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message type';

COMMENT ON COLUMN messaging.message_types.name IS 'The name of the message type';


CREATE TABLE messaging.message_statuses (
  code INTEGER NOT NULL,
  name TEXT    NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN messaging.message_statuses.code IS 'The code identifying the message status';

COMMENT ON COLUMN messaging.message_statuses.name IS 'The name of the message status';


CREATE TABLE messaging.messages (
  id                UUID      NOT NULL,
  username          TEXT      NOT NULL,
  device_id         UUID      NOT NULL,
  type_id           UUID      NOT NULL,
  correlation_id    UUID      NOT NULL,
  priority          INTEGER   NOT NULL,
  status            INTEGER   NOT NULL,
  created           TIMESTAMP NOT NULL,
  persisted         TIMESTAMP NOT NULL,
  updated           TIMESTAMP,
  send_attempts     INTEGER   NOT NULL,
  process_attempts  INTEGER   NOT NULL,
  download_attempts INTEGER   NOT NULL,
  lock_name         TEXT,
  last_processed    TIMESTAMP,
  data              BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT messages_message_type_fk FOREIGN KEY (type_id) REFERENCES messaging.message_types(id),
  CONSTRAINT messages_message_status_fk FOREIGN KEY (status) REFERENCES messaging.message_statuses(code)
);

CREATE INDEX messages_username_ix ON messaging.messages(username);

CREATE INDEX messages_device_id_ix ON messaging.messages(device_id);

CREATE INDEX messages_type_id_ix ON messaging.messages(type_id);

CREATE INDEX messages_priority_ix ON messaging.messages(priority);

CREATE INDEX messages_status_ix ON messaging.messages(status);

CREATE INDEX messages_lock_name_ix ON messaging.messages(lock_name);

COMMENT ON COLUMN messaging.messages.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN messaging.messages.username IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN messaging.messages.device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN messaging.messages.type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN messaging.messages.correlation_id IS 'The Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.messages.priority IS 'The message priority';

COMMENT ON COLUMN messaging.messages.status IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.messages.persisted IS 'The date and time the message was persisted';

COMMENT ON COLUMN messaging.messages.updated IS 'The date and time the message was last updated';

COMMENT ON COLUMN messaging.messages.send_attempts IS 'The number of times that the sending of the message was attempted';

COMMENT ON COLUMN messaging.messages.process_attempts IS 'The number of times that the processing of the message was attempted';

COMMENT ON COLUMN messaging.messages.download_attempts IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN messaging.messages.lock_name IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN messaging.messages.last_processed IS 'The date and time the last attempt was made to process the message';

COMMENT ON COLUMN messaging.messages.data IS 'The data for the message';


CREATE TABLE messaging.message_parts (
  id                 UUID      NOT NULL,
  part_no            INTEGER   NOT NULL,
  total_parts        INTEGER   NOT NULL,
  send_attempts      INTEGER   NOT NULL,
  download_attempts  INTEGER   NOT NULL,
  status             INTEGER   NOT NULL,
  persisted          TIMESTAMP NOT NULL,
  updated            TIMESTAMP,
  msg_id             UUID      NOT NULL,
  msg_username       TEXT      NOT NULL,
  msg_device_id      UUID      NOT NULL,
  msg_type_id        UUID      NOT NULL,
  msg_correlation_id UUID      NOT NULL,
  msg_priority       INTEGER   NOT NULL,
  msg_created        TIMESTAMP NOT NULL,
  msg_data_hash      TEXT,
  msg_encryption_iv  TEXT      NOT NULL,
  msg_checksum       TEXT      NOT NULL,
  lock_name          TEXT,
  data               BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT message_parts_message_type_fk FOREIGN KEY (msg_type_id) REFERENCES messaging.message_types(id)
);

CREATE INDEX message_parts_status_ix ON messaging.message_parts(status);

CREATE INDEX message_parts_msg_id_ix ON messaging.message_parts(msg_id);

CREATE INDEX message_parts_msg_device_id_ix ON messaging.message_parts(msg_device_id);

CREATE INDEX message_parts_msg_type_id_ix ON messaging.message_parts(msg_type_id);

CREATE INDEX message_parts_lock_name_ix ON messaging.message_parts(lock_name);

COMMENT ON COLUMN messaging.message_parts.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message part';

COMMENT ON COLUMN messaging.message_parts.part_no IS 'The number of the message part in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.total_parts IS 'The total number of parts in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.send_attempts IS 'The number of times that the sending of the message part was attempted';

COMMENT ON COLUMN messaging.message_parts.download_attempts IS 'The number of times that an attempt was made to download the message part';

COMMENT ON COLUMN messaging.message_parts.status IS 'The message part status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.message_parts.persisted IS 'The date and time the message part was persisted';

COMMENT ON COLUMN messaging.message_parts.updated IS 'The date and time the message part was last updated';

COMMENT ON COLUMN messaging.message_parts.msg_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the original message';

COMMENT ON COLUMN messaging.message_parts.msg_username IS 'The username identifying the user associated with the original message';

COMMENT ON COLUMN messaging.message_parts.msg_device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the original message originated from';

COMMENT ON COLUMN messaging.message_parts.msg_type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of the original message';

COMMENT ON COLUMN messaging.message_parts.msg_correlation_id IS 'The Universally Unique Identifier (UUID) used to correlate the original message';

COMMENT ON COLUMN messaging.message_parts.msg_priority IS 'The priority for the original message';

COMMENT ON COLUMN messaging.message_parts.msg_created IS 'The date and time the original message was created';

COMMENT ON COLUMN messaging.message_parts.msg_data_hash IS 'The hash of the unencrypted data for the original message';

COMMENT ON COLUMN messaging.message_parts.msg_encryption_iv IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';

COMMENT ON COLUMN messaging.message_parts.msg_checksum IS 'The checksum for the original message';

COMMENT ON COLUMN messaging.message_parts.lock_name IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN messaging.message_parts.data IS 'The data for the message part';


CREATE TABLE messaging.archived_messages (
  id             UUID      NOT NULL,
  username       TEXT      NOT NULL,
  device_id      UUID      NOT NULL,
  type_id        UUID      NOT NULL,
  correlation_id UUID      NOT NULL,
  created        TIMESTAMP NOT NULL,
  archived       TIMESTAMP NOT NULL,
  data           BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT archived_messages_message_type_fk FOREIGN KEY (type_id) REFERENCES messaging.message_types(id)
);

CREATE INDEX archived_messages_username_ix ON messaging.archived_messages(username);

CREATE INDEX archived_messages_device_id_ix ON messaging.archived_messages(device_id);

CREATE INDEX archived_messages_type_id_ix ON messaging.archived_messages(type_id);

COMMENT ON COLUMN messaging.archived_messages.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN messaging.archived_messages.username IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN messaging.archived_messages.device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN messaging.archived_messages.type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN messaging.archived_messages.correlation_id IS 'The Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.archived_messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.archived_messages.archived IS 'The date and time the message was archived';

COMMENT ON COLUMN messaging.archived_messages.data IS 'The data for the message';



CREATE TABLE reporting.report_definitions (
  id       UUID  NOT NULL,
  name     TEXT  NOT NULL,
  template BYTEA NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN reporting.report_definitions.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN reporting.report_definitions.name IS 'The name of the report definition';

COMMENT ON COLUMN reporting.report_definitions.template IS 'The JasperReports template for the report definition';


CREATE TABLE scheduler.jobs (
  id                 UUID    NOT NULL,
  name               TEXT    NOT NULL,
  scheduling_pattern TEXT    NOT NULL,
  job_class          TEXT    NOT NULL,
  is_enabled         BOOLEAN NOT NULL,
  status             INTEGER NOT NULL DEFAULT 1,
  execution_attempts INTEGER NOT NULL DEFAULT 0,
  lock_name          TEXT,
  last_executed      TIMESTAMP,
  next_execution     TIMESTAMP,
  updated            TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN scheduler.jobs.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN scheduler.jobs.name IS 'The name of the job';

COMMENT ON COLUMN scheduler.jobs.scheduling_pattern IS 'The cron-style scheduling pattern for the job';

COMMENT ON COLUMN scheduler.jobs.job_class IS 'The fully qualified name of the Java class that implements the job';

COMMENT ON COLUMN scheduler.jobs.is_enabled IS 'Is the job enabled for execution';

COMMENT ON COLUMN scheduler.jobs.status IS 'The status of the job';

COMMENT ON COLUMN scheduler.jobs.execution_attempts IS 'The number of times the current execution of the job has been attempted';

COMMENT ON COLUMN scheduler.jobs.lock_name IS 'The name of the entity that has locked the job for execution';

COMMENT ON COLUMN scheduler.jobs.last_executed IS 'The date and time the job was last executed';

COMMENT ON COLUMN scheduler.jobs.next_execution IS 'The date and time created the job will next be executed';

COMMENT ON COLUMN scheduler.jobs.updated IS 'The date and time the job was updated';


CREATE TABLE scheduler.job_parameters (
  id     UUID NOT NULL,
  job_id UUID NOT NULL,
  name   TEXT NOT NULL,
  value  TEXT NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT job_parameters_job_fk FOREIGN KEY (job_id) REFERENCES scheduler.jobs(id) ON DELETE CASCADE
);

CREATE INDEX job_parameters_job_id_ix ON scheduler.job_parameters(job_id);

CREATE INDEX job_parameters_name_ix ON scheduler.job_parameters(name);

COMMENT ON COLUMN scheduler.job_parameters.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job parameter';

COMMENT ON COLUMN scheduler.job_parameters.job_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN scheduler.job_parameters.name IS 'The name of the job parameter';

COMMENT ON COLUMN scheduler.job_parameters.value IS 'The value of the job parameter';


CREATE TABLE security.organizations (
  id     UUID    NOT NULL,
  name   TEXT    NOT NULL,
  status INTEGER NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX organizations_name_ix ON security.organizations(name);

COMMENT ON COLUMN security.organizations.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organization';

COMMENT ON COLUMN security.organizations.name IS 'The name of the organization';

COMMENT ON COLUMN security.organizations.status IS 'The status for the organization';


CREATE TABLE security.user_directory_types (
  id                   UUID NOT NULL,
  name                 TEXT NOT NULL,
  user_directory_class TEXT NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN security.user_directory_types.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directory_types.name IS 'The name of the user directory type';

COMMENT ON COLUMN security.user_directory_types.user_directory_class IS 'The fully qualified name of the Java class that implements the user directory type';


CREATE TABLE security.user_directories (
  id            UUID NOT NULL,
  type_id       UUID NOT NULL,
  name          TEXT NOT NULL,
  configuration TEXT NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT user_directories_user_directory_type_fk FOREIGN KEY (type_id) REFERENCES security.user_directory_types(id) ON DELETE CASCADE
);

CREATE INDEX user_directories_name_ix ON security.user_directories(name);

COMMENT ON COLUMN security.user_directories.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directories.type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directories.name IS 'The name of the user directory';

COMMENT ON COLUMN security.user_directories.configuration IS 'The XML configuration data for the user directory';


CREATE TABLE security.user_directory_to_organization_map (
  user_directory_id UUID NOT NULL,
  organization_id   UUID NOT NULL,

  PRIMARY KEY (user_directory_id, organization_id),
  CONSTRAINT user_directory_to_organization_map_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE,
  CONSTRAINT user_directory_to_organization_map_organization_fk FOREIGN KEY (organization_id) REFERENCES security.organizations(id) ON DELETE CASCADE
);

CREATE INDEX user_directory_to_organization_map_user_directory_id_ix ON security.user_directory_to_organization_map(user_directory_id);

CREATE INDEX user_directory_to_organization_map_organization_id_ix ON security.user_directory_to_organization_map(organization_id);

COMMENT ON COLUMN security.user_directory_to_organization_map.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directory_to_organization_map.organization_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organization';


CREATE TABLE security.internal_users (
  id                UUID    NOT NULL,
  user_directory_id UUID    NOT NULL,
  username          TEXT    NOT NULL,
  status            INTEGER NOT NULL,
  first_name        TEXT    NOT NULL DEFAULT '',
  last_name         TEXT    NOT NULL DEFAULT '',
  phone             TEXT    NOT NULL DEFAULT '',
  mobile            TEXT    NOT NULL DEFAULT '',
  email             TEXT    NOT NULL DEFAULT '',
  password          TEXT    NOT NULL DEFAULT '',
  password_attempts INTEGER NOT NULL DEFAULT 0,
  password_expiry   TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT internal_users_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX internal_users_user_directory_id_ix ON security.internal_users(user_directory_id);

CREATE UNIQUE INDEX internal_users_username_ix ON security.internal_users(username);

COMMENT ON COLUMN security.internal_users.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_users.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal user is associated with';

COMMENT ON COLUMN security.internal_users.username IS 'The username for the internal user';

COMMENT ON COLUMN security.internal_users.status IS 'The status for the internal user';

COMMENT ON COLUMN security.internal_users.first_name IS 'The first name for the internal user';

COMMENT ON COLUMN security.internal_users.last_name IS 'The last name for the internal user';

COMMENT ON COLUMN security.internal_users.phone IS 'The phone number for the internal user';

COMMENT ON COLUMN security.internal_users.mobile IS 'The mobile number for the internal user';

COMMENT ON COLUMN security.internal_users.email IS 'The e-mail address for the internal user';

COMMENT ON COLUMN security.internal_users.password IS 'The password for the internal user';

COMMENT ON COLUMN security.internal_users.password_attempts IS 'The number of failed attempts to authenticate the internal user';

COMMENT ON COLUMN security.internal_users.password_expiry IS 'The date and time that the internal user''s password expires';


CREATE TABLE security.internal_users_password_history (
  id               UUID      NOT NULL,
  internal_user_id UUID      NOT NULL,
  changed          TIMESTAMP NOT NULL,
  password         TEXT,

  PRIMARY KEY (id),
  CONSTRAINT internal_users_password_history_internal_user_id_fk FOREIGN KEY (internal_user_id) REFERENCES security.internal_users(id) ON DELETE CASCADE
);

CREATE INDEX internal_users_password_history_internal_user_id_ix ON security.internal_users_password_history(internal_user_id);

CREATE INDEX internal_users_password_history_changed_ix ON security.internal_users_password_history(changed);

COMMENT ON COLUMN security.internal_users_password_history.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the password history entry';

COMMENT ON COLUMN security.internal_users_password_history.internal_user_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_users_password_history.changed IS 'When the password change took place for the internal user';

COMMENT ON COLUMN security.internal_users_password_history.password IS 'The password for the internal user';


CREATE TABLE security.internal_groups (
  id                UUID NOT NULL,
  user_directory_id UUID NOT NULL,
  groupname         TEXT NOT NULL,
  description       TEXT,

  PRIMARY KEY (id),
  CONSTRAINT internal_groups_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX internal_groups_user_directory_id_ix ON security.internal_groups(user_directory_id);

CREATE INDEX internal_groups_groupname_ix ON security.internal_groups(groupname);

COMMENT ON COLUMN security.internal_groups.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';

COMMENT ON COLUMN security.internal_groups.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with';

COMMENT ON COLUMN security.internal_groups.groupname IS 'The group name for the internal group';

COMMENT ON COLUMN security.internal_groups.description IS 'A description for the internal group';


CREATE TABLE security.internal_user_to_internal_group_map (
  internal_user_id  UUID NOT NULL,
  internal_group_id UUID NOT NULL,

  PRIMARY KEY (internal_user_id, internal_group_id),
  CONSTRAINT internal_user_to_internal_group_map_internal_user_fk FOREIGN KEY (internal_user_id) REFERENCES security.internal_users(id) ON DELETE CASCADE,
  CONSTRAINT internal_user_to_internal_group_map_internal_group_fk FOREIGN KEY (internal_group_id) REFERENCES security.internal_groups(id) ON DELETE CASCADE
);

CREATE INDEX internal_user_to_internal_group_map_internal_user_id_ix ON security.internal_user_to_internal_group_map(internal_user_id);

CREATE INDEX internal_user_to_internal_group_map_internal_group_id_ix ON security.internal_user_to_internal_group_map(internal_group_id);

COMMENT ON COLUMN security.internal_user_to_internal_group_map.internal_user_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_user_to_internal_group_map.internal_group_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';


CREATE TABLE security.groups (
  id                UUID NOT NULL,
  user_directory_id UUID NOT NULL,
  groupname         TEXT NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX groups_user_directory_id_ix ON security.groups(user_directory_id);

CREATE INDEX groups_groupname_ix ON security.groups(groupname);

COMMENT ON COLUMN security.groups.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';

COMMENT ON COLUMN security.groups.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the group is associated with';

COMMENT ON COLUMN security.groups.groupname IS 'The group name for the group';


CREATE TABLE security.functions (
  id          UUID NOT NULL,
  code        TEXT NOT NULL,
  name        TEXT NOT NULL,
  description TEXT,

  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX functions_code_ix ON security.functions(code);

COMMENT ON COLUMN security.functions.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN security.functions.code IS 'The unique code used to identify the function';

COMMENT ON COLUMN security.functions.name IS 'The name of the function';

COMMENT ON COLUMN security.functions.description IS 'A description for the function';


CREATE TABLE security.roles (
  id          UUID NOT NULL,
  name        TEXT NOT NULL,
  description TEXT,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN security.roles.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN security.roles.name IS 'The name of the role';

COMMENT ON COLUMN security.roles.description IS 'A description for the role';


CREATE TABLE security.function_to_role_map (
  function_id UUID NOT NULL,
  role_id     UUID NOT NULL,

  PRIMARY KEY (function_id, role_id),
  CONSTRAINT function_to_role_map_function_fk FOREIGN KEY (function_id) REFERENCES security.functions(id) ON DELETE CASCADE,
  CONSTRAINT function_to_role_map_role_fk FOREIGN KEY (role_id) REFERENCES security.roles(id) ON DELETE CASCADE
);

CREATE INDEX function_to_role_map_function_id_ix ON security.function_to_role_map(function_id);

CREATE INDEX function_to_role_map_role_id_ix ON security.function_to_role_map(role_id);

COMMENT ON COLUMN security.function_to_role_map.function_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN security.function_to_role_map.role_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';


CREATE TABLE security.role_to_group_map (
  role_id  UUID NOT NULL,
  group_id UUID NOT NULL,

  PRIMARY KEY (role_id, group_id),
  CONSTRAINT role_to_group_map_role_fk FOREIGN KEY (role_id) REFERENCES security.roles(id) ON DELETE CASCADE,
  CONSTRAINT role_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups(id) ON DELETE CASCADE
);

CREATE INDEX role_to_group_map_role_id_ix ON security.role_to_group_map(role_id);

CREATE INDEX role_to_group_map_group_id_ix ON security.role_to_group_map(group_id);

COMMENT ON COLUMN security.role_to_group_map.role_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN security.role_to_group_map.group_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';


CREATE TABLE service_registry.service_registry (
  name                 TEXT    NOT NULL,
  security_type        INTEGER NOT NULL,
  supports_compression CHAR(1) NOT NULL,
  endpoint             TEXT    NOT NULL,
  service_class        TEXT    NOT NULL,
  wsdl_location        TEXT    NOT NULL,
  username             TEXT,
  password             TEXT,

  PRIMARY KEY (name)
);

COMMENT ON COLUMN service_registry.service_registry.name IS 'The name used to uniquely identify the web service';

COMMENT ON COLUMN service_registry.service_registry.security_type IS 'The type of security model implemented by the web service i.e. 0 = None, 1 = Mutual SSL, etc';

COMMENT ON COLUMN service_registry.service_registry.supports_compression IS 'Does the web service support compression';

COMMENT ON COLUMN service_registry.service_registry.endpoint IS 'The endpoint for the web service';

COMMENT ON COLUMN service_registry.service_registry.service_class IS 'The fully qualified name of the Java service class';

COMMENT ON COLUMN service_registry.service_registry.wsdl_location IS 'The location of the WSDL defining the web service on the classpath';

COMMENT ON COLUMN service_registry.service_registry.username IS 'The username to use created accessing a web service with username-password security enabled';

COMMENT ON COLUMN service_registry.service_registry.password IS 'The password to use created accessing a web service with username-password security enabled';


CREATE TABLE sms.sms (
  id             BIGINT  NOT NULL,
  mobile_number  TEXT    NOT NULL,
  message        TEXT    NOT NULL,
  status         INTEGER NOT NULL,
  send_attempts  INTEGER NOT NULL,
  lock_name      TEXT,
  last_processed TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE INDEX sms_mobile_number_ix ON sms.sms(mobile_number);

COMMENT ON COLUMN sms.sms.id IS 'The ID used to uniquely identify the SMS';

COMMENT ON COLUMN sms.sms.mobile_number IS 'The mobile number to send the SMS to';

COMMENT ON COLUMN sms.sms.message IS 'The message to send';

COMMENT ON COLUMN sms.sms.status IS 'The status of the SMS';

COMMENT ON COLUMN sms.sms.send_attempts IS 'The number of times that the sending of the SMS was attempted';

COMMENT ON COLUMN sms.sms.lock_name IS 'The name of the entity that has locked the SMS for sending';

COMMENT ON COLUMN sms.sms.last_processed IS 'The date and time the last attempt was made to send the SMS';


CREATE TABLE error.error_reports (
  id                  UUID      NOT NULL,
  application_id      TEXT      NOT NULL,
  application_version TEXT      NOT NULL,
  description         TEXT      NOT NULL,
  detail              TEXT      NOT NULL,
  created             TIMESTAMP NOT NULL,
  who                 TEXT,
  device_id           TEXT,
  feedback            TEXT,
  data                BYTEA,

  PRIMARY KEY (id)
);

CREATE INDEX error_reports_application_id_ix ON error.error_reports(application_id);

CREATE INDEX error_reports_created_ix ON error.error_reports(created);

CREATE INDEX error_reports_who_ix ON error.error_reports(who);

COMMENT ON COLUMN error.error_reports.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN error.error_reports.application_id IS 'The ID used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN error.error_reports.application_version IS 'The version of the application that generated the error report';

COMMENT ON COLUMN error.error_reports.description IS 'The description of the error';

COMMENT ON COLUMN error.error_reports.detail IS 'The error detail e.g. a stack trace';

COMMENT ON COLUMN error.error_reports.created IS 'The date and time the error report was created';

COMMENT ON COLUMN error.error_reports.who IS 'The optional username identifying the user associated with the error report';

COMMENT ON COLUMN error.error_reports.device_id IS 'The optional ID used to uniquely identify the device the error report originated from';

COMMENT ON COLUMN error.error_reports.feedback IS 'The optional feedback provided by the user for the error';

COMMENT ON COLUMN error.error_reports.data IS 'The optional data associated with the error report';


CREATE TABLE test.test_data (
  id    TEXT NOT NULL,
  name  TEXT NOT NULL,
  value TEXT NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN test.test_data.id IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN test.test_data.name IS 'The name for the test data';

COMMENT ON COLUMN test.test_data.value IS 'The value for the test data';


CREATE TABLE sample.data (
  id              BIGINT        NOT NULL,
  name            TEXT NOT NULL,
  integer_value   INTEGER,
  string_value    TEXT,
  date_value      DATE,
  timestamp_value TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN sample.data.id IS 'The ID used to uniquely identify the data';

COMMENT ON COLUMN sample.data.name IS 'The name for the data';

COMMENT ON COLUMN sample.data.integer_value IS 'The integer value for the data';

COMMENT ON COLUMN sample.data.string_value IS 'The string value for the data';

COMMENT ON COLUMN sample.data.date_value IS 'The date value for the data';

COMMENT ON COLUMN sample.data.timestamp_value IS 'The timestamp value for the data';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO security.organizations (id, name, status)
  VALUES ('c1685b92-9fe5-453a-995b-89d8c0f29cb5', 'MMP', 1);

INSERT INTO security.user_directory_types (id, name, user_directory_class)
  VALUES ('b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', 'digital.inception.security.InternalUserDirectory');
INSERT INTO security.user_directory_types (id, name, user_directory_class)
  VALUES ('e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'LDAP User Directory', 'digital.inception.security.LDAPUserDirectory');

INSERT INTO security.user_directories (id, type_id, name, configuration)
  VALUES ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
  VALUES ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'c1685b92-9fe5-453a-995b-89d8c0f29cb5');

INSERT INTO security.internal_users (id, user_directory_id, username, status, first_name, last_name, phone, mobile, email, password, password_attempts)
  VALUES ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrator', 1, '', '', '', '', '', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', 0);

INSERT INTO security.internal_groups (id, user_directory_id, groupname, description)
  VALUES ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators', 'Administrators');
INSERT INTO security.internal_groups (id, user_directory_id, groupname, description)
  VALUES ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organization Administrators', 'Organization Administrators');

INSERT INTO security.internal_user_to_internal_group_map (internal_user_id, internal_group_id)
  VALUES ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');

INSERT INTO security.groups (id, user_directory_id, groupname)
  VALUES ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators');
INSERT INTO security.groups (id, user_directory_id, groupname)
  VALUES ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organization Administrators');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', 'Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', 'Application.SecureHome', 'Secure Home', 'Secure Home');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', 'Codes.CodeAdministration', 'Code Administration', 'Code Administration');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', 'Configuration.ConfigurationAdministration', 'Configuration Administration', 'Configuration Administration');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('97f0f870-a871-48de-a3e0-a32a95770f12', 'Error.ErrorReportAdministration', 'Error Report Administration', 'Error Report Administration');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('180c84f9-9816-48d0-9762-dc753b2228b1', 'Process.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('d2854c65-9a59-40b8-9dc7-a882c64b2610', 'Process.ViewProcess', 'View Process', 'View Process');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', 'Reporting.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', 'Reporting.ViewReport', 'View Report', 'View Report');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a', 'Scheduler.SchedulerAdministration', 'Scheduler Administration', 'Scheduler Administration');

INSERT INTO security.functions (id, code, name, description)
  VALUES ('ef03f384-24f7-43eb-a29c-f5c5b838698d', 'Security.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('2d52b029-920f-4b15-b646-5b9955c188e3', 'Security.OrganizationAdministration', 'Organization Administration', 'Organization Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', 'Security.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('9105fb6d-1629-4014-bf4c-1990a92db276', 'Security.SecurityAdministration', 'Security Administration', 'Security Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', 'Security.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO security.functions (id, code, name, description)
  VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', 'Security.UserGroups', 'User Groups', 'User Groups');

INSERT INTO security.roles (id, name, description)
  VALUES ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'Administrator', 'Administrator');
INSERT INTO security.roles (id, name, description)
  VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', 'Organization Administrator', 'Organization Administrator');

INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Application.Dashboard function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Application.SecureHome function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Codes.CodeAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Configuration.ConfigurationAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('97f0f870-a871-48de-a3e0-a32a95770f12', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Error.ErrorReportAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('180c84f9-9816-48d0-9762-dc753b2228b1', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Process.ProcessDefinitionAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('d2854c65-9a59-40b8-9dc7-a882c64b2610', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Process.ViewProcess function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Reporting.ReportDefinitionAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Reporting.ViewReport function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Scheduler.SchedulerAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('ef03f384-24f7-43eb-a29c-f5c5b838698d', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.GroupAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('2d52b029-920f-4b15-b646-5b9955c188e3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.OrganizationAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.ResetUserPassword function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('9105fb6d-1629-4014-bf4c-1990a92db276', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.SecurityAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.UserAdministration function to the Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Assign the Security.UserGroups function to the Administrator role

INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Application.SecureHome function to the Organization Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Application.Dashboard function to the Organization Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Reporting.ViewReport function to the Organization Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Security.ResetUserPassword function to the Organization Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Security.UserAdministration function to the Organization Administrator role
INSERT INTO security.function_to_role_map (function_id, role_id)
  VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Assign the Security.UserGroups function to the Organization Administrator role

INSERT INTO security.role_to_group_map (role_id, group_id)
  VALUES ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');
INSERT INTO security.role_to_group_map (role_id, group_id)
  VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '758c0a2a-f3a3-4561-bebc-90569291976e');

INSERT INTO messaging.message_types (id, name)
  VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', 'AuthenticateRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');

INSERT INTO messaging.message_statuses (code, name)
  VALUES (0, 'Initialised');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (1, 'QueuedForSending');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (2, 'QueuedForProcessing');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (3, 'Aborted');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (4, 'Failed');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (5, 'Processing');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (6, 'Sending');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (7, 'QueuedForDownload');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (8, 'Downloading');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (10, 'Processed');

INSERT INTO test.test_data (id, name, value)
  VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO test.test_data (id, name, value)
  VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO test.test_data (id, name, value)
  VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO test.test_data (id, name, value)
  VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO test.test_data (id, name, value)
  VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO test.test_data (id, name, value)
  VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO test.test_data (id, name, value)
  VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO test.test_data (id, name, value)
  VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO test.test_data (id, name, value)
  VALUES (9, 'Sample Name 9', 'Sample Value 9');
  
  
  
  
  
  
  
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b64d', 'Sample', 1);

INSERT INTO security.user_directories (id, type_id, name, configuration)
  VALUES ('34ccdbc9-4a01-46f5-a284-ba13e095675c', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Sample Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

--INSERT INTO MMP.USER_DIRECTORIES (ID, TYPE_ID, NAME, CONFIGURATION) VALUES
--  ('595d13ac-22d6-4ce2-b898-3add4658a748', 'e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'Sample LDAP User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>Host</name><value>sds.inception.digital</value></parameter><parameter><name>Port</name><value>389</value></parameter><parameter><name>UseSSL</name><value>false</value></parameter><parameter><name>BindDN</name><value>uid=system,ou=users,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>BindPassword</name><value>Password1</value></parameter><parameter><name>BaseDN</name><value>ou=test,ou=applications,o=MMP</value></parameter><parameter><name>UserBaseDN</name><value>ou=users,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>GroupBaseDN</name><value>ou=groups,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>SharedBaseDN</name><value>ou=staff,o=MMP</value></parameter><parameter><name>UserObjectClass</name><value>inetOrgPerson</value></parameter><parameter><name>UserUsernameAttribute</name><value>uid</value></parameter><parameter><name>UserPasswordExpiryAttribute</name><value>passwordexpiry</value></parameter><parameter><name>UserPasswordAttemptsAttribute</name><value>passwordattempts</value></parameter><parameter><name>UserPasswordHistoryAttribute</name><value>passwordhistory</value></parameter><parameter><name>UserFirstNameAttribute</name><value>givenName</value></parameter><parameter><name>UserLastNameAttribute</name><value>sn</value></parameter><parameter><name>UserPhoneNumberAttribute</name><value>telephoneNumber</value></parameter><parameter><name>UserFaxNumberAttribute</name><value>facsimileTelephoneNumber</value></parameter><parameter><name>UserMobileNumberAttribute</name><value>mobile</value></parameter><parameter><name>UserEmailAttribute</name><value>mail</value></parameter><parameter><name>UserDescriptionAttribute</name><value>cn</value></parameter><parameter><name>GroupObjectClass</name><value>groupOfNames</value></parameter><parameter><name>GroupNameAttribute</name><value>cn</value></parameter><parameter><name>GroupMemberAttribute</name><value>member</value></parameter><parameter><name>GroupDescriptionAttribute</name><value>description</value></parameter><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>SupportPasswordHistory</name><value>true</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>PasswordHistoryMaxLength</name><value>128</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter><parameter><name>MaxFilteredGroups</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
  VALUES ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', '204e5b8f-48e7-4354-bd15-753e6543b64d');
INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
  VALUES ('34ccdbc9-4a01-46f5-a284-ba13e095675c', '204e5b8f-48e7-4354-bd15-753e6543b64d');
--INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANIZATION_MAP (USER_DIRECTORY_ID, ORGANIZATION_ID) VALUES
--  ('595d13ac-22d6-4ce2-b898-3add4658a748', '204e5b8f-48e7-4354-bd15-753e6543b64d');

INSERT INTO security.internal_users (id, user_directory_id, username, status, first_name, last_name, phone, mobile, email, password, password_attempts, password_expiry)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'sample', 1, '', '', '', '', '', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', 0, NULL);

INSERT INTO security.internal_groups (id, user_directory_id, groupname, description)
  VALUES ('956c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Organization Administrators', 'Organization Administrators');

INSERT INTO security.internal_user_to_internal_group_map (internal_user_id, internal_group_id)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '956c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO security.groups (id, user_directory_id, groupname)
  VALUES ('956c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Organization Administrators');

INSERT INTO security.role_to_group_map (role_id, group_id)
  VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '956c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO sample.data (id, name, string_value)
  VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO sample.data (id, name, string_value)
  VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO sample.data (id, name, string_value)
  VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO sample.data (id, name, string_value)
  VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO sample.data (id, name, string_value)
  VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO sample.data (id, name, string_value)
  VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO sample.data (id, name, string_value)
  VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO sample.data (id, name, string_value)
  VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO sample.data (id, name, string_value)
  VALUES (9, 'Sample Name 9', 'Sample Value 9');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory01', 'Test Code Category 01', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode011', 'TestCodeCategory01', 'Test Code Name 1.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode012', 'TestCodeCategory01', 'Test Code Name 1.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode013', 'TestCodeCategory01', 'Test Code Name 1.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory02', 'Test Code Category 02', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode021', 'TestCodeCategory02', 'Test Code Name 2.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode022', 'TestCodeCategory02', 'Test Code Name 2.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode023', 'TestCodeCategory02', 'Test Code Name 2.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory03', 'Test Code Category 03', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode031', 'TestCodeCategory03', 'Test Code Name 3.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode032', 'TestCodeCategory03', 'Test Code Name 3.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode033', 'TestCodeCategory03', 'Test Code Name 3.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory04', 'Test Code Category 04', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode041', 'TestCodeCategory04', 'Test Code Name 4.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode042', 'TestCodeCategory04', 'Test Code Name 4.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode043', 'TestCodeCategory04', 'Test Code Name 4.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory05', 'Test Code Category 05', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode051', 'TestCodeCategory05', 'Test Code Name 5.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode052', 'TestCodeCategory05', 'Test Code Name 5.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode053', 'TestCodeCategory05', 'Test Code Name 5.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory06', 'Test Code Category 06', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode061', 'TestCodeCategory06', 'Test Code Name 6.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode062', 'TestCodeCategory06', 'Test Code Name 6.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode063', 'TestCodeCategory06', 'Test Code Name 6.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory07', 'Test Code Category 07', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode071', 'TestCodeCategory07', 'Test Code Name 7.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode072', 'TestCodeCategory07', 'Test Code Name 7.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode073', 'TestCodeCategory07', 'Test Code Name 7.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory08', 'Test Code Category 08', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode081', 'TestCodeCategory08', 'Test Code Name 8.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode082', 'TestCodeCategory08', 'Test Code Name 8.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode083', 'TestCodeCategory08', 'Test Code Name 8.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory09', 'Test Code Category 09', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode091', 'TestCodeCategory09', 'Test Code Name 9.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode092', 'TestCodeCategory09', 'Test Code Name 9.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode093', 'TestCodeCategory09', 'Test Code Name 9.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory10', 'Test Code Category 10', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode101', 'TestCodeCategory10', 'Test Code Name 10.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode102', 'TestCodeCategory10', 'Test Code Name 10.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode103', 'TestCodeCategory10', 'Test Code Name 10.3', 'Test Code Value 3');

INSERT INTO codes.code_categories (id, name, updated, data)
  VALUES ('TestCodeCategory11', 'Test Code Category 11', NOW(), '<codes><code name="name1" value="value1"/></codes>');

INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode111', 'TestCodeCategory11', 'Test Code Name 11.1', 'Test Code Value 1');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode112', 'TestCodeCategory11', 'Test Code Name 11.2', 'Test Code Value 2');
INSERT INTO codes.codes (id, code_category_id, name, value)
  VALUES ('TestCode113', 'TestCodeCategory11', 'Test Code Name 11.3', 'Test Code Value 3');


--INSERT INTO configuration.configuration (key, value, description) VALUES ('TestKey', 'TestValue', 'TestDescription');


INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b601', 'Test 1', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b602', 'Test 2', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b603', 'Test 3', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b604', 'Test 4', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b605', 'Test 5', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b606', 'Test 6', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b607', 'Test 7', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b608', 'Test 8', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b609', 'Test 9', 1);
INSERT INTO security.organizations (id, name, status)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b610', 'Test 10', 1);  
  
  
  
  
  
  

-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON SCHEMA codes TO sample;
GRANT ALL ON SCHEMA configuration TO sample;
GRANT ALL ON SCHEMA idgenerator TO sample;
GRANT ALL ON SCHEMA messaging TO sample;
GRANT ALL ON SCHEMA reporting TO sample;
GRANT ALL ON SCHEMA scheduler TO sample;
GRANT ALL ON SCHEMA security TO sample;
GRANT ALL ON SCHEMA service_registry TO sample;
GRANT ALL ON SCHEMA sms TO sample;
GRANT ALL ON SCHEMA error TO sample;
GRANT ALL ON SCHEMA test TO sample;
GRANT ALL ON SCHEMA sample TO sample;

GRANT ALL ON table codes.code_categories TO sample;
GRANT ALL ON table codes.codes TO sample;
GRANT ALL ON table configuration.configuration TO sample;
GRANT ALL ON table idgenerator.idgenerator TO sample;
GRANT ALL ON table messaging.message_types TO sample;
GRANT ALL ON table messaging.message_statuses TO sample;
GRANT ALL ON table messaging.messages TO sample;
GRANT ALL ON table messaging.message_parts TO sample;
GRANT ALL ON table messaging.archived_messages TO sample;
GRANT ALL ON table reporting.report_definitions TO sample;
GRANT ALL ON table scheduler.jobs TO sample;
GRANT ALL ON table scheduler.job_parameters TO sample;
GRANT ALL ON table security.organizations TO sample;
GRANT ALL ON table security.user_directory_types TO sample;
GRANT ALL ON table security.user_directories TO sample;
GRANT ALL ON table security.user_directory_to_organization_map TO sample;
GRANT ALL ON table security.internal_users TO sample;
GRANT ALL ON table security.internal_users_password_history TO sample;
GRANT ALL ON table security.internal_groups TO sample;
GRANT ALL ON table security.internal_user_to_internal_group_map TO sample;
GRANT ALL ON table security.groups TO sample;
GRANT ALL ON table security.functions TO sample;
GRANT ALL ON table security.roles TO sample;
GRANT ALL ON table security.function_to_role_map TO sample;
GRANT ALL ON table security.role_to_group_map TO sample;
GRANT ALL ON table service_registry.service_registry TO sample;
GRANT ALL ON table sms.sms TO sample;
GRANT ALL ON table error.error_reports TO sample;
GRANT ALL ON table test.test_data TO sample;
GRANT ALL ON table sample.data TO sample;

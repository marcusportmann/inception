-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA security;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE security.organisations (
  id     UUID          NOT NULL,
  name   VARCHAR(4000) NOT NULL,
  status INTEGER       NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX organisations_name_ix
  ON security.organisations
  (name);

COMMENT ON COLUMN security.organisations.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';

COMMENT ON COLUMN security.organisations.name
IS 'The name of the organisation';

COMMENT ON COLUMN security.organisations.status
IS 'The status for the organisation';


CREATE TABLE security.user_directory_types (
  id                   UUID          NOT NULL,
  name                 VARCHAR(4000) NOT NULL,
  user_directory_class VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN security.user_directory_types.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directory_types.name
IS 'The name of the user directory type';

COMMENT ON COLUMN security.user_directory_types.user_directory_class
IS 'The fully qualified name of the Java class that implements the user directory type';


CREATE TABLE security.user_directories (
  id            UUID          NOT NULL,
  type_id       UUID          NOT NULL,
  name          VARCHAR(4000) NOT NULL,
  configuration VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT user_directories_user_directory_type_fk FOREIGN KEY (type_id) REFERENCES security.user_directory_types (id) ON DELETE CASCADE
);

CREATE INDEX user_directories_name_ix
  ON security.user_directories
  (name);

COMMENT ON COLUMN security.user_directories.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directories.type_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directories.name
IS 'The name of the user directory';

COMMENT ON COLUMN security.user_directories.configuration
IS 'The XML configuration data for the user directory';


CREATE TABLE security.user_directory_to_organisation_map (
  user_directory_id UUID NOT NULL,
  organisation_id   UUID NOT NULL,

  PRIMARY KEY (user_directory_id, organisation_id),
  CONSTRAINT user_directory_to_organisation_map_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories (id) ON DELETE CASCADE,
  CONSTRAINT user_directory_to_organisation_map_organisation_fk FOREIGN KEY (organisation_id) REFERENCES security.organisations (id) ON DELETE CASCADE
);

CREATE INDEX user_directory_to_organisation_map_user_directory_id_ix
  ON security.user_directory_to_organisation_map
  (user_directory_id);

CREATE INDEX user_directory_to_organisation_map_organisation_id_ix
  ON security.user_directory_to_organisation_map
  (organisation_id);

COMMENT ON COLUMN security.user_directory_to_organisation_map.user_directory_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directory_to_organisation_map.organisation_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';


CREATE TABLE security.internal_users (
  id                UUID          NOT NULL,
  user_directory_id UUID          NOT NULL,
  username          VARCHAR(4000) NOT NULL,
  password          VARCHAR(4000),
  first_name        VARCHAR(4000),
  last_name         VARCHAR(4000),
  phone             VARCHAR(4000),
  mobile            VARCHAR(4000),
  email             VARCHAR(4000),
  password_attempts INTEGER,
  password_expiry   DATETIME,

  PRIMARY KEY (id),
  CONSTRAINT internal_users_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories (id) ON DELETE CASCADE
);

CREATE INDEX internal_users_user_directory_id_ix
  ON security.internal_users
  (user_directory_id);

CREATE UNIQUE INDEX internal_users_username_ix
  ON security.internal_users
  (username);

COMMENT ON COLUMN security.internal_users.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_users.user_directory_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal user is associated with';

COMMENT ON COLUMN security.internal_users.username
IS 'The username for the internal user';

COMMENT ON COLUMN security.internal_users.password
IS 'The password for the internal user';

COMMENT ON COLUMN security.internal_users.first_name
IS 'The first name for the internal user';

COMMENT ON COLUMN security.internal_users.last_name
IS 'The last name for the internal user';

COMMENT ON COLUMN security.internal_users.phone
IS 'The phone number for the internal user';

COMMENT ON COLUMN security.internal_users.mobile
IS 'The mobile number for the internal user';

COMMENT ON COLUMN security.internal_users.email
IS 'The e-mail address for the internal user';

COMMENT ON COLUMN security.internal_users.password_attempts
IS 'The number of failed attempts to authenticate the internal user';

COMMENT ON COLUMN security.internal_users.password_expiry
IS 'The date and time that the internal user''s password expires';


CREATE TABLE security.internal_users_password_history (
  id               UUID     NOT NULL,
  internal_user_id UUID     NOT NULL,
  changed          DATETIME NOT NULL,
  password         VARCHAR(4000),

  PRIMARY KEY (id),
  CONSTRAINT internal_users_password_history_internal_user_id_fk FOREIGN KEY (internal_user_id) REFERENCES security.internal_users (id) ON DELETE CASCADE
);

CREATE INDEX internal_users_password_history_internal_user_id_ix
  ON security.internal_users_password_history
  (internal_user_id);

CREATE INDEX internal_users_password_history_changed_ix
  ON security.internal_users_password_history
  (changed);

COMMENT ON COLUMN security.internal_users_password_history.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the password history entry';

COMMENT ON COLUMN security.internal_users_password_history.internal_user_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_users_password_history.changed
IS 'When the password change took place for the internal user';

COMMENT ON COLUMN security.internal_users_password_history.password
IS 'The password for the internal user';


CREATE TABLE security.internal_groups (
  id                UUID          NOT NULL,
  user_directory_id UUID          NOT NULL,
  groupname         VARCHAR(4000) NOT NULL,
  description       VARCHAR(4000),

  PRIMARY KEY (id),
  CONSTRAINT internal_groups_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories (id) ON DELETE CASCADE
);

CREATE INDEX internal_groups_user_directory_id_ix
  ON security.internal_groups
  (user_directory_id);

CREATE INDEX internal_groups_groupname_ix
  ON security.internal_groups
  (groupname);

COMMENT ON COLUMN security.internal_groups.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';

COMMENT ON COLUMN security.internal_groups.user_directory_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with';

COMMENT ON COLUMN security.internal_groups.groupname
IS 'The group name for the internal group';

COMMENT ON COLUMN security.internal_groups.description
IS 'A description for the internal group';


CREATE TABLE security.internal_user_to_internal_group_map (
  internal_user_id  UUID NOT NULL,
  internal_group_id UUID NOT NULL,

  PRIMARY KEY (internal_user_id, internal_group_id),
  CONSTRAINT internal_user_to_internal_group_map_internal_user_fk FOREIGN KEY (internal_user_id) REFERENCES security.internal_users (id) ON DELETE CASCADE,
  CONSTRAINT internal_user_to_internal_group_map_internal_group_fk FOREIGN KEY (internal_group_id) REFERENCES security.internal_groups (id) ON DELETE CASCADE
);

CREATE INDEX internal_user_to_internal_group_map_internal_user_id_ix
  ON security.internal_user_to_internal_group_map
  (internal_user_id);

CREATE INDEX internal_user_to_internal_group_map_internal_group_id_ix
  ON security.internal_user_to_internal_group_map
  (internal_group_id);

COMMENT ON COLUMN security.internal_user_to_internal_group_map.internal_user_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN security.internal_user_to_internal_group_map.internal_group_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';


CREATE TABLE security.groups (
  id                UUID          NOT NULL,
  user_directory_id UUID          NOT NULL,
  groupname         VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX groups_user_directory_id_ix
  ON security.groups
  (user_directory_id);

CREATE INDEX groups_groupname_ix
  ON security.groups
  (groupname);

COMMENT ON COLUMN security.groups.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';

COMMENT ON COLUMN security.groups.user_directory_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the group is associated with';

COMMENT ON COLUMN security.groups.groupname
IS 'The group name for the group';


CREATE TABLE security.functions (
  id          UUID          NOT NULL,
  code        VARCHAR(4000) NOT NULL,
  name        VARCHAR(4000) NOT NULL,
  description VARCHAR(4000),

  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX functions_code_ix
  ON security.functions
  (code);

COMMENT ON COLUMN security.functions.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN security.functions.code
IS 'The unique code used to identify the function';

COMMENT ON COLUMN security.functions.name
IS 'The name of the function';

COMMENT ON COLUMN security.functions.description
IS 'A description for the function';


CREATE TABLE security.roles (
  id          UUID          NOT NULL,
  name        VARCHAR(4000) NOT NULL,
  description VARCHAR(4000),

  PRIMARY KEY (id)
);

COMMENT ON COLUMN security.roles.id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN security.roles.name
IS 'The name of the role';

COMMENT ON COLUMN security.roles.description
IS 'A description for the role';


CREATE TABLE security.function_to_role_map (
  function_id UUID NOT NULL,
  role_id     UUID NOT NULL,

  PRIMARY KEY (function_id, role_id),
  CONSTRAINT function_to_role_map_function_fk FOREIGN KEY (function_id) REFERENCES security.functions (id) ON DELETE CASCADE,
  CONSTRAINT function_to_role_map_role_fk FOREIGN KEY (role_id) REFERENCES security.roles (id) ON DELETE CASCADE
);

CREATE INDEX function_to_role_map_function_id_ix
  ON security.function_to_role_map
  (function_id);

CREATE INDEX function_to_role_map_role_id_ix
  ON security.function_to_role_map
  (role_id);

COMMENT ON COLUMN security.function_to_role_map.function_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN security.function_to_role_map.role_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';


CREATE TABLE security.role_to_group_map (
  role_id  UUID NOT NULL,
  group_id UUID NOT NULL,

  PRIMARY KEY (role_id, group_id),
  CONSTRAINT role_to_group_map_role_fk FOREIGN KEY (role_id) REFERENCES security.roles (id) ON DELETE CASCADE,
  CONSTRAINT role_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups (id) ON DELETE CASCADE
);

CREATE INDEX role_to_group_map_role_id_ix
  ON security.role_to_group_map
  (role_id);

CREATE INDEX role_to_group_map_group_id_ix
  ON security.role_to_group_map
  (group_id);

COMMENT ON COLUMN security.role_to_group_map.role_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN security.role_to_group_map.group_id
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO security.organisations (id, name, status) VALUES
  ('c1685b92-9fe5-453a-995b-89d8c0f29cb5', 'MMP', 1);

INSERT INTO security.user_directory_types (id, name, user_directory_class) VALUES
  ('b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory',
   'digital.inception.security.InternalUserDirectory');
INSERT INTO security.user_directory_types (id, name, user_directory_class) VALUES
  ('e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'LDAP User Directory',
   'digital.inception.security.LDAPUserDirectory');

INSERT INTO security.user_directories (id, type_id, name, configuration) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f',
   'Internal User Directory',
   '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_organisation_map (user_directory_id, organisation_id) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'c1685b92-9fe5-453a-995b-89d8c0f29cb5');

INSERT INTO security.internal_users (id, user_directory_id, username, password, first_name, last_name, phone, mobile, email, password_attempts, password_expiry)
VALUES
  ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrator',
                                           'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=',
                                           'Administrator', '', '', '', '', NULL, NULL);

INSERT INTO security.internal_groups (id, user_directory_id, groupname, description) VALUES
  ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators',
   'Administrators');
INSERT INTO security.internal_groups (id, user_directory_id, groupname, description) VALUES
  ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4',
   'Organisation Administrators', 'Organisation Administrators');

INSERT INTO security.internal_user_to_internal_group_map (internal_user_id, internal_group_id)
VALUES
  ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');

INSERT INTO security.groups (id, user_directory_id, groupname) VALUES
  ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4',
   'Administrators');
INSERT INTO security.groups (id, user_directory_id, groupname) VALUES
  ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4',
   'Organisation Administrators');

INSERT INTO security.functions (id, code, name, description) VALUES
  ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', 'Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', 'Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('2d52b029-920f-4b15-b646-5b9955c188e3', 'Application.OrganisationAdministration',
   'Organisation Administration', 'Organisation Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('567d7e55-f3d0-4191-bc4c-12d357900fa3', 'Application.UserAdministration', 'User Administration',
   'User Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('ef03f384-24f7-43eb-a29c-f5c5b838698d', 'Application.GroupAdministration',
   'Group Administration', 'Group Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('7a54a71e-3680-4d49-b87d-29604a247413', 'Application.UserGroups', 'User Groups', 'User Groups');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', 'Application.CodeAdministration', 'Code Administration',
   'Code Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('029b9a06-0241-4a44-a234-5c489f2017ba', 'Application.ResetUserPassword', 'Reset User Password',
   'Reset User Password');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('9105fb6d-1629-4014-bf4c-1990a92db276', 'Application.SecurityAdministration',
   'Security Administration', 'Security Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', 'Application.ConfigurationAdministration',
   'Configuration Administration', 'Configuration Administration');

INSERT INTO security.functions (id, code, name, description) VALUES
  ('97f0f870-a871-48de-a3e0-a32a95770f12', 'Application.ErrorReports', 'ApplicationError Reports',
   'ApplicationError Reports');

INSERT INTO security.functions (id, code, name, description) VALUES
  ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', 'Application.ReportDefinitionAdministration',
   'Report Definition Administration', 'Report Definition Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('539fceb8-da82-4170-ab1a-ae6b04001c03', 'Application.ViewReport', 'View Report', 'View Report');

INSERT INTO security.functions (id, code, name, description) VALUES
  ('180c84f9-9816-48d0-9762-dc753b2228b1', 'Application.ProcessDefinitionAdministration',
   'Process Definition Administration', 'Process Definition Administration');
INSERT INTO security.functions (id, code, name, description) VALUES
  ('d2854c65-9a59-40b8-9dc7-a882c64b2610', 'Application.ViewProcess', 'View Process',
   'View Process');

INSERT INTO security.functions (id, code, name, description) VALUES
  ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a', 'Application.SchedulerAdministration',
   'Scheduler Administration', 'Scheduler Administration');

INSERT INTO security.roles (id, name, description) VALUES
  ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'Administrator', 'Administrator');
INSERT INTO security.roles (id, name, description) VALUES
  ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', 'Organisation Administrator',
   'Organisation Administrator');

INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecureHome
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.Dashboard
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('2d52b029-920f-4b15-b646-5b9955c188e3',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.OrganisationAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('567d7e55-f3d0-4191-bc4c-12d357900fa3',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('ef03f384-24f7-43eb-a29c-f5c5b838698d',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.GroupAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('7a54a71e-3680-4d49-b87d-29604a247413',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserGroups
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.CodeAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('029b9a06-0241-4a44-a234-5c489f2017ba',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ResetUserPassword
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('9105fb6d-1629-4014-bf4c-1990a92db276',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecurityAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('b233ed4a-b30f-4356-a5d3-1c660aa69f00',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ConfigurationAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('97f0f870-a871-48de-a3e0-a32a95770f12',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ErrorReports
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('3a17959c-5dfc-43a2-9587-48a1eb95a22a',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ReportDefinitionAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('539fceb8-da82-4170-ab1a-ae6b04001c03',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewReport
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('180c84f9-9816-48d0-9762-dc753b2228b1',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ProcessDefinitionAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('d2854c65-9a59-40b8-9dc7-a882c64b2610',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewProcess
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a',
   '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SchedulerAdministration

INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.SecureHome
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.Dashboard
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('567d7e55-f3d0-4191-bc4c-12d357900fa3',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserAdministration
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('7a54a71e-3680-4d49-b87d-29604a247413',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserGroups
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('029b9a06-0241-4a44-a234-5c489f2017ba',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ResetUserPassword
INSERT INTO security.function_to_role_map (function_id, role_id) VALUES
  ('539fceb8-da82-4170-ab1a-ae6b04001c03',
   '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ViewReport

INSERT INTO security.role_to_group_map (role_id, group_id)
VALUES ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');
INSERT INTO security.role_to_group_map (role_id, group_id)
VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '758c0a2a-f3a3-4561-bebc-90569291976e');

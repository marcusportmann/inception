-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA security;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE security.organizations (
  id     UUID         NOT NULL,
  name   VARCHAR(100) NOT NULL,
  status INTEGER      NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX organizations_name_ix ON security.organizations(name);

COMMENT ON COLUMN security.organizations.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';

COMMENT ON COLUMN security.organizations.name IS 'The name of the organization';

COMMENT ON COLUMN security.organizations.status IS 'The status for the organization';


CREATE TABLE security.user_directory_types (
  code                 VARCHAR(100)  NOT NULL,
  name                 VARCHAR(100)  NOT NULL,
  user_directory_class VARCHAR(1000) NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.user_directory_types.code IS 'The code uniquely identifying the user directory type';

COMMENT ON COLUMN security.user_directory_types.name IS 'The name of the user directory type';

COMMENT ON COLUMN security.user_directory_types.user_directory_class IS 'The fully qualified name of the Java class that implements the user directory type';


CREATE TABLE security.user_directories (
  id            UUID          NOT NULL,
  type          VARCHAR(100)  NOT NULL,
  name          VARCHAR(100)  NOT NULL,
  configuration VARCHAR(4000) NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT user_directories_user_directory_type_fk FOREIGN KEY (type) REFERENCES security.user_directory_types(code) ON DELETE CASCADE
);

CREATE INDEX user_directories_name_ix ON security.user_directories(name);

COMMENT ON COLUMN security.user_directories.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user directory';

COMMENT ON COLUMN security.user_directories.type IS 'The code uniquely identifying the user directory type';

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

COMMENT ON COLUMN security.user_directory_to_organization_map.user_directory_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user directory';

COMMENT ON COLUMN security.user_directory_to_organization_map.organization_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';


CREATE TABLE security.users (
  id                UUID         NOT NULL,
  user_directory_id UUID         NOT NULL,
  username          VARCHAR(100) NOT NULL,
  status            INTEGER      NOT NULL,
  name              VARCHAR(100) NOT NULL DEFAULT '',
  preferred_name    VARCHAR(100) NOT NULL DEFAULT '',
  phone_number      VARCHAR(100) NOT NULL DEFAULT '',
  mobile_number     VARCHAR(100) NOT NULL DEFAULT '',
  email             VARCHAR(100) NOT NULL DEFAULT '',
  password          VARCHAR(100) NOT NULL DEFAULT '',
  password_attempts INTEGER      NOT NULL DEFAULT 0,
  password_expiry   TIMESTAMP    NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT users_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX users_user_directory_id_ix ON security.users(user_directory_id);

CREATE UNIQUE INDEX users_username_ix ON security.users(username);

CREATE UNIQUE INDEX users_name_ix ON security.users(name);

COMMENT ON COLUMN security.users.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user';

COMMENT ON COLUMN security.users.user_directory_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user directory the user is associated with';

COMMENT ON COLUMN security.users.username IS 'The username for the user';

COMMENT ON COLUMN security.users.status IS 'The status for the user';

COMMENT ON COLUMN security.users.name IS 'The name of the user';

COMMENT ON COLUMN security.users.preferred_name IS 'The preferred name for the user';

COMMENT ON COLUMN security.users.phone_number IS 'The phone number for the user';

COMMENT ON COLUMN security.users.mobile_number IS 'The mobile number for the user';

COMMENT ON COLUMN security.users.email IS 'The e-mail address for the user';

COMMENT ON COLUMN security.users.password IS 'The password for the user';

COMMENT ON COLUMN security.users.password_attempts IS 'The number of failed attempts to authenticate the user';

COMMENT ON COLUMN security.users.password_expiry IS 'The date and time that the user''s password expires';


CREATE TABLE security.users_password_history (
  user_id  UUID      NOT NULL,
  changed  TIMESTAMP NOT NULL,
  password VARCHAR(100),

  PRIMARY KEY (user_id, changed),
  CONSTRAINT users_password_history_user_id_fk FOREIGN KEY (user_id) REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE INDEX users_password_history_user_id_ix ON security.users_password_history(user_id);

CREATE INDEX users_password_history_changed_ix ON security.users_password_history(changed);

COMMENT ON COLUMN security.users_password_history.user_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user';

COMMENT ON COLUMN security.users_password_history.changed IS 'When the password change took place for the user';

COMMENT ON COLUMN security.users_password_history.password IS 'The password for the user';


CREATE TABLE security.groups (
  id                UUID         NOT NULL,
  user_directory_id UUID         NOT NULL,
  name              VARCHAR(100) NOT NULL,
  description       VARCHAR(100),

  PRIMARY KEY (id),
  CONSTRAINT groups_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX groups_user_directory_id_ix ON security.groups(user_directory_id);

CREATE UNIQUE INDEX groups_user_directory_id_name_ix ON security.groups(user_directory_id, name);

COMMENT ON COLUMN security.groups.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the group';

COMMENT ON COLUMN security.groups.user_directory_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user directory the group is associated with';

COMMENT ON COLUMN security.groups.name IS 'The name for the group';

COMMENT ON COLUMN security.groups.description IS 'A description for the group';


CREATE TABLE security.user_to_group_map (
  user_id  UUID NOT NULL,
  group_id UUID NOT NULL,

  PRIMARY KEY (user_id, group_id),
  CONSTRAINT user_to_group_map_user_fk FOREIGN KEY (user_id) REFERENCES security.users(id) ON DELETE CASCADE,
  CONSTRAINT user_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups(id) ON DELETE CASCADE
);

CREATE INDEX user_to_group_map_user_id_ix ON security.user_to_group_map(user_id);

CREATE INDEX user_to_group_map_group_id_ix ON security.user_to_group_map(group_id);

COMMENT ON COLUMN security.user_to_group_map.user_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the user';

COMMENT ON COLUMN security.user_to_group_map.group_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the group';


CREATE TABLE security.functions (
  code        VARCHAR(100) NOT NULL,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(100),

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.functions.code IS 'The code uniquely identifying the function';

COMMENT ON COLUMN security.functions.name IS 'The name of the function';

COMMENT ON COLUMN security.functions.description IS 'A description for the function';


CREATE TABLE security.roles (
  code        VARCHAR(100) NOT NULL,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(100),

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.roles.code IS 'The code uniquely identifying the role';

COMMENT ON COLUMN security.roles.name IS 'The name of the role';

COMMENT ON COLUMN security.roles.description IS 'The description for the role';


CREATE TABLE security.function_to_role_map (
  function_code VARCHAR(100) NOT NULL,
  role_code     VARCHAR(100) NOT NULL,

  PRIMARY KEY (function_code, role_code),
  CONSTRAINT function_to_role_map_function_fk FOREIGN KEY (function_code) REFERENCES security.functions(code) ON DELETE CASCADE,
  CONSTRAINT function_to_role_map_role_fk FOREIGN KEY (role_code) REFERENCES security.roles(code) ON DELETE CASCADE
);

CREATE INDEX function_to_role_map_function_code_ix ON security.function_to_role_map(function_code);

CREATE INDEX function_to_role_map_role_code_ix ON security.function_to_role_map(role_code);

COMMENT ON COLUMN security.function_to_role_map.function_code IS 'The code uniquely identifying the function';

COMMENT ON COLUMN security.function_to_role_map.role_code IS 'The code uniquely identifying the role';


CREATE TABLE security.role_to_group_map (
  role_code VARCHAR(100) NOT NULL,
  group_id  UUID         NOT NULL,

  PRIMARY KEY (role_code, group_id),
  CONSTRAINT role_to_group_map_role_fk FOREIGN KEY (role_code) REFERENCES security.roles(code) ON DELETE CASCADE,
  CONSTRAINT role_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups(id) ON DELETE CASCADE
);

CREATE INDEX role_to_group_map_role_code_ix ON security.role_to_group_map(role_code);

CREATE INDEX role_to_group_map_group_id_ix ON security.role_to_group_map(group_id);

COMMENT ON COLUMN security.role_to_group_map.role_code IS 'The code uniquely identifying the role';

COMMENT ON COLUMN security.role_to_group_map.group_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the group';


CREATE TABLE security.password_resets (
  username           VARCHAR(100) NOT NULL,
  requested          TIMESTAMP    NOT NULL,
  status             INTEGER      NOT NULL,
  security_code_hash VARCHAR(100) NOT NULL,
  completed          TIMESTAMP,
  expired            TIMESTAMP,

  PRIMARY KEY (username, requested)
);

CREATE INDEX password_resets_username_ix ON security.password_resets(username);

COMMENT ON COLUMN security.password_resets.username IS 'The username for the user associated with the password reset';

COMMENT ON COLUMN security.password_resets.requested IS 'The date and time the password reset was requested';

COMMENT ON COLUMN security.password_resets.status IS 'The status of the password reset';

COMMENT ON COLUMN security.password_resets.security_code_hash IS 'The security code hash';

COMMENT ON COLUMN security.password_resets.completed IS 'The date and time the password reset was completed';

COMMENT ON COLUMN security.password_resets.expired IS 'The date and time the password reset expired';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO security.organizations (id, name, status)
  VALUES ('00000000-0000-0000-0000-000000000000', 'Administration', 1);

INSERT INTO security.user_directory_types (code, name, user_directory_class)
  VALUES ('InternalUserDirectory', 'Internal User Directory', 'digital.inception.security.InternalUserDirectory');
INSERT INTO security.user_directory_types (code, name, user_directory_class)
  VALUES ('LDAPUserDirectory', 'LDAP User Directory', 'digital.inception.security.LDAPUserDirectory');

INSERT INTO security.user_directories (id, type, name, configuration)
  VALUES ('00000000-0000-0000-0000-000000000000', 'InternalUserDirectory', 'Administration Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter><parameter><name>MaxFilteredGroups</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
  VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000');

INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry)
  VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'administrator', 1, 'Administrator', '', '', '', '', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'));

INSERT INTO security.groups (id, user_directory_id, name, description)
  VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'Administrators', 'Administrators');

INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000');

INSERT INTO security.functions (code, name, description)
  VALUES ('Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO security.functions (code, name, description)
  VALUES ('Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO security.functions (code, name, description)
  VALUES ('Codes.CodeAdministration', 'Code Administration', 'Code Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Configuration.ConfigurationAdministration', 'Configuration Administration', 'Configuration Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Error.ErrorReportAdministration', 'Error Report Administration', 'Error Report Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Mail.MailTemplateAdministration', 'Mail Template Administration', 'Mail Template Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Process.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Process.ViewProcess', 'View Process', 'View Process');
INSERT INTO security.functions (code, name, description)
  VALUES ('Reporting.ReportingAdministration', 'Reporting Administration', 'Reporting Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Reporting.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Reporting.ViewReport', 'View Report', 'View Report');
INSERT INTO security.functions (code, name, description)
  VALUES ('Scheduler.SchedulerAdministration', 'Scheduler Administration', 'Scheduler Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Scheduler.JobAdministration', 'Job Administration', 'Job Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.OrganizationAdministration', 'Organization Administration', 'Organization Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.SecurityAdministration', 'Security Administration', 'Security Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserDirectoryAdministration', 'User Directory Administration', 'User Directory Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserGroups', 'User Groups', 'User Groups');

INSERT INTO security.roles (code, name, description)
  VALUES ('Administrator', 'Administrator', 'Administrator');
INSERT INTO security.roles (code, name, description)
  VALUES ('OrganizationAdministrator', 'Organization Administrator', 'Organization Administrator');
INSERT INTO security.roles (code, name, description)
  VALUES ('PasswordResetter', 'Password Resetter', 'Password Resetter');

INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.SecureHome', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.Dashboard', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Reporting.ViewReport', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.ResetUserPassword', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.UserAdministration', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.UserGroups', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.GroupAdministration', 'OrganizationAdministrator');

INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.SecureHome', 'PasswordResetter');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.Dashboard', 'PasswordResetter');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.ResetUserPassword', 'PasswordResetter');

INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('Administrator', '00000000-0000-0000-0000-000000000000'); -- Assign the Administrator role to the Administrators group






-- INSERT INTO security.organizations (id, name, status)
--   VALUES ('11111111-1111-1111-1111-111111111111', 'Sample LDAP Organization', 1);
--
-- INSERT INTO security.user_directories (id, type, name, configuration)
--   VALUES ('11111111-1111-1111-1111-111111111111', 'LDAPUserDirectory', 'Sample LDAP User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>Host</name><value>localhost</value></parameter><parameter><name>Port</name><value>389</value></parameter><parameter><name>UseSSL</name><value>false</value></parameter><parameter><name>BindDN</name><value>cn=root,o=sample</value></parameter><parameter><name>BindPassword</name><value>Password1</value></parameter><parameter><name>BaseDN</name><value>ou=sample,ou=applications,o=sample</value></parameter><parameter><name>UserBaseDN</name><value>ou=users,ou=sample,ou=applications,o=sample</value></parameter><parameter><name>GroupBaseDN</name><value>ou=groups,ou=sample,ou=applications,o=sample</value></parameter><parameter><name>UserObjectClass</name><value>inetOrgPerson</value></parameter><parameter><name>UserUsernameAttribute</name><value>uid</value></parameter><parameter><name>UserNameAttribute</name><value>cn</value></parameter><parameter><name>UserPreferredNameAttribute</name><value>nickName</value></parameter><parameter><name>UserPhoneNumberAttribute</name><value>telephoneNumber</value></parameter><parameter><name>UserFaxNumberAttribute</name><value>facsimileTelephoneNumber</value></parameter><parameter><name>UserMobileNumberAttribute</name><value>mobile</value></parameter><parameter><name>UserEmailAttribute</name><value>mail</value></parameter><parameter><name>UserDescriptionAttribute</name><value>cn</value></parameter><parameter><name>GroupObjectClass</name><value>groupOfNames</value></parameter><parameter><name>GroupNameAttribute</name><value>cn</value></parameter><parameter><name>GroupMemberAttribute</name><value>member</value></parameter><parameter><name>GroupDescriptionAttribute</name><value>description</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter><parameter><name>MaxFilteredGroups</name><value>100</value></parameter></userDirectory>');
--
-- INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
--   VALUES ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111');

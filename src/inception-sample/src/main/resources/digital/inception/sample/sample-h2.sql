-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA sample;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE sample.data (
  id              BIGINT        NOT NULL,
  name            VARCHAR(4000) NOT NULL,
  integer_value   INTEGER,
  string_value    VARCHAR(4000),
  date_value      DATE,
  timestamp_value DATETIME,

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
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'sample', 1, '', '', '', '', '', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', NULL, NULL);

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

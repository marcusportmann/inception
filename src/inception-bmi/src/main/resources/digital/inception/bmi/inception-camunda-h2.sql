--
-- Complete the following steps to update the Camunda schema in this file:
--
-- 1. Clone the camunda-bpm-platform repository (https://github.com/camunda/camunda-bpm-platform.git)
--
-- 2. Apply the changes in the appropriate file in under the following folder:
--
--    camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/upgrade
--
--    e.g. h2_engine_7.12_to_7.13.sql
--
create schema CAMUNDA;


--              _   _       _ _   _   _     ____                       _                         _                        _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___   ___ _ __   __ _(_)_ __   ___   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ / _ \ '_ \ / _` | | '_ \ / _ \ / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/|  __/ | | | (_| | | | | |  __/_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)___|_| |_|\__, |_|_| |_|\___(_)___/\__, |_|
--                                                                                         |___/                       |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.engine.sql

create table CAMUNDA.ACT_GE_PROPERTY (
    NAME_ varchar(64),
    VALUE_ varchar(300),
    REV_ integer,
    primary key (NAME_)
);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('schema.version', 'fox', 1);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('schema.history', 'create(fox)', 1);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('next.dbid', '1', 1);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('deployment.lock', '0', 1);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('history.cleanup.job.lock', '0', 1);

insert into CAMUNDA.ACT_GE_PROPERTY
values ('startup.lock', '0', 1);

create table CAMUNDA.ACT_GE_BYTEARRAY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    DEPLOYMENT_ID_ varchar(64),
    BYTES_ longvarbinary,
    GENERATED_ bit,
    TENANT_ID_ varchar(64),
    TYPE_ integer,
    CREATE_TIME_ timestamp,
    ROOT_PROC_INST_ID_ varchar(64),
    REMOVAL_TIME_ timestamp,
    primary key (ID_)
);

create table CAMUNDA.ACT_GE_SCHEMA_LOG (
    ID_ varchar(64),
    TIMESTAMP_ timestamp,
    VERSION_ varchar(255),
    primary key (ID_)
);

insert into CAMUNDA.ACT_GE_SCHEMA_LOG
values ('0', CURRENT_TIMESTAMP, '7.13.0');
insert into CAMUNDA.ACT_GE_SCHEMA_LOG
values ('300', CURRENT_TIMESTAMP, '7.14.0');

create table CAMUNDA.ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    DEPLOY_TIME_ timestamp,
    SOURCE_ varchar(255),
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_EXECUTION (
    ID_ varchar(64),
    REV_ integer,
    ROOT_PROC_INST_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    SUPER_EXEC_ varchar(64),
    SUPER_CASE_EXEC_ varchar(64),
    CASE_INST_ID_ varchar(64),
    ACT_INST_ID_ varchar(64),
    ACT_ID_ varchar(255),
    IS_ACTIVE_ bit,
    IS_CONCURRENT_ bit,
    IS_SCOPE_ bit,
    IS_EVENT_SCOPE_ bit,
    SUSPENSION_STATE_ integer,
    CACHED_ENT_STATE_ integer,
    SEQUENCE_COUNTER_ integer,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_JOB (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    TYPE_ varchar(255) NOT NULL,
    LOCK_EXP_TIME_ timestamp,
    LOCK_OWNER_ varchar(255),
    EXCLUSIVE_ boolean,
    EXECUTION_ID_ varchar(64),
    PROCESS_INSTANCE_ID_ varchar(64),
    PROCESS_DEF_ID_ varchar(64),
    PROCESS_DEF_KEY_ varchar(255),
    RETRIES_ integer,
    EXCEPTION_STACK_ID_ varchar(64),
    EXCEPTION_MSG_ varchar(4000),
    FAILED_ACT_ID_ varchar(255),
    DUEDATE_ timestamp,
    REPEAT_ varchar(255),
    REPEAT_OFFSET_ bigint DEFAULT 0,
    HANDLER_TYPE_ varchar(255),
    HANDLER_CFG_ varchar(4000),
    DEPLOYMENT_ID_ varchar(64),
    SUSPENSION_STATE_ integer NOT NULL DEFAULT 1,
    JOB_DEF_ID_ varchar(64),
    PRIORITY_ bigint NOT NULL DEFAULT 0,
    SEQUENCE_COUNTER_ integer,
    TENANT_ID_ varchar(64),
    CREATE_TIME_ timestamp,
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_JOBDEF (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    PROC_DEF_ID_ varchar(64),
    PROC_DEF_KEY_ varchar(255),
    ACT_ID_ varchar(255),
    JOB_TYPE_ varchar(255) NOT NULL,
    JOB_CONFIGURATION_ varchar(255),
    SUSPENSION_STATE_ integer,
    JOB_PRIORITY_ bigint,
    TENANT_ID_ varchar(64),
    DEPLOYMENT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RE_PROCDEF (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255) NOT NULL,
    VERSION_ integer NOT NULL,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    HAS_START_FORM_KEY_ bit,
    SUSPENSION_STATE_ integer,
    TENANT_ID_ varchar(64),
    VERSION_TAG_ varchar(64),
    HISTORY_TTL_ integer,
    STARTABLE_ boolean NOT NULL default TRUE,
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_TASK (
    ID_ varchar(64),
    REV_ integer,
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    CASE_EXECUTION_ID_ varchar(64),
    CASE_INST_ID_ varchar(64),
    CASE_DEF_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    TASK_DEF_KEY_ varchar(255),
    OWNER_ varchar(255),
    ASSIGNEE_ varchar(255),
    DELEGATION_ varchar(64),
    PRIORITY_ integer,
    CREATE_TIME_ timestamp,
    DUE_DATE_ timestamp,
    FOLLOW_UP_DATE_ timestamp,
    SUSPENSION_STATE_ integer,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_IDENTITYLINK (
    ID_ varchar(64),
    REV_ integer,
    GROUP_ID_ varchar(255),
    TYPE_ varchar(255),
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_VARIABLE (
    ID_ varchar(64) not null,
    REV_ integer,
    TYPE_ varchar(255) not null,
    NAME_ varchar(255) not null,
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    CASE_EXECUTION_ID_ varchar(64),
    CASE_INST_ID_ varchar(64),
    TASK_ID_ varchar(64),
    BATCH_ID_ varchar(64),
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    VAR_SCOPE_ varchar(64) not null,
    SEQUENCE_COUNTER_ integer,
    IS_CONCURRENT_LOCAL_ bit,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_EVENT_SUBSCR (
    ID_ varchar(64) not null,
    REV_ integer,
    EVENT_TYPE_ varchar(255) not null,
    EVENT_NAME_ varchar(255),
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTIVITY_ID_ varchar(255),
    CONFIGURATION_ varchar(255),
    CREATED_ timestamp not null,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

create table CAMUNDA.ACT_RU_INCIDENT (
  ID_ varchar(64) not null,
  REV_ integer not null,
  INCIDENT_TIMESTAMP_ timestamp not null,
  INCIDENT_MSG_ varchar(4000),
  INCIDENT_TYPE_ varchar(255) not null,
  EXECUTION_ID_ varchar(64),
  ACTIVITY_ID_ varchar(255),
  FAILED_ACTIVITY_ID_ varchar(255),
  PROC_INST_ID_ varchar(64),
  PROC_DEF_ID_ varchar(64),
  CAUSE_INCIDENT_ID_ varchar(64),
  ROOT_CAUSE_INCIDENT_ID_ varchar(64),
  CONFIGURATION_ varchar(255),
  TENANT_ID_ varchar(64),
  JOB_DEF_ID_ varchar(64),
  primary key (ID_)
);

create table CAMUNDA.ACT_RU_AUTHORIZATION (
  ID_ varchar(64) not null,
  REV_ integer not null,
  TYPE_ integer not null,
  GROUP_ID_ varchar(255),
  USER_ID_ varchar(255),
  RESOURCE_TYPE_ integer not null,
  RESOURCE_ID_ varchar(255),
  PERMS_ integer,
  REMOVAL_TIME_ timestamp,
  ROOT_PROC_INST_ID_ varchar(64),
  primary key (ID_)
);

create table CAMUNDA.ACT_RU_FILTER (
  ID_ varchar(64) not null,
  REV_ integer not null,
  RESOURCE_TYPE_ varchar(255) not null,
  NAME_ varchar(255) not null,
  OWNER_ varchar(255),
  QUERY_ CLOB not null,
  PROPERTIES_ CLOB,
  primary key (ID_)
);

create table CAMUNDA.ACT_RU_METER_LOG (
  ID_ varchar(64) not null,
  NAME_ varchar(64) not null,
  REPORTER_ varchar(255),
  VALUE_ long,
  TIMESTAMP_ timestamp,
  MILLISECONDS_ bigint DEFAULT 0,
  primary key (ID_)
);

create table CAMUNDA.ACT_RU_EXT_TASK (
  ID_ varchar(64) not null,
  REV_ integer not null,
  WORKER_ID_ varchar(255),
  TOPIC_NAME_ varchar(255),
  RETRIES_ integer,
  ERROR_MSG_ varchar(4000),
  ERROR_DETAILS_ID_ varchar(64),
  LOCK_EXP_TIME_ timestamp,
  SUSPENSION_STATE_ integer,
  EXECUTION_ID_ varchar(64),
  PROC_INST_ID_ varchar(64),
  PROC_DEF_ID_ varchar(64),
  PROC_DEF_KEY_ varchar(255),
  ACT_ID_ varchar(255),
  ACT_INST_ID_ varchar(64),
  TENANT_ID_ varchar(64),
  PRIORITY_ bigint NOT NULL DEFAULT 0,
  primary key (ID_)
);

create table CAMUNDA.ACT_RU_BATCH (
  ID_ varchar(64) not null,
  REV_ integer not null,
  TYPE_ varchar(255),
  TOTAL_JOBS_ integer,
  JOBS_CREATED_ integer,
  JOBS_PER_SEED_ integer,
  INVOCATIONS_PER_JOB_ integer,
  SEED_JOB_DEF_ID_ varchar(64),
  BATCH_JOB_DEF_ID_ varchar(64),
  MONITOR_JOB_DEF_ID_ varchar(64),
  SUSPENSION_STATE_ integer,
  CONFIGURATION_ varchar(255),
  TENANT_ID_ varchar(64),
  CREATE_USER_ID_ varchar(255),
  primary key (ID_)
);

create index ACT_IDX_EXEC_ROOT_PI on CAMUNDA.ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);
create index ACT_IDX_EXEC_BUSKEY on CAMUNDA.ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_EXEC_TENANT_ID on CAMUNDA.ACT_RU_EXECUTION(TENANT_ID_);
create index ACT_IDX_TASK_CREATE on CAMUNDA.ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_TASK_ASSIGNEE on CAMUNDA.ACT_RU_TASK(ASSIGNEE_);
create index ACT_IDX_TASK_TENANT_ID on CAMUNDA.ACT_RU_TASK(TENANT_ID_);
create index ACT_IDX_IDENT_LNK_USER on CAMUNDA.ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on CAMUNDA.ACT_RU_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on CAMUNDA.ACT_RU_EVENT_SUBSCR(CONFIGURATION_);
create index ACT_IDX_EVENT_SUBSCR_TENANT_ID on CAMUNDA.ACT_RU_EVENT_SUBSCR(TENANT_ID_);
create index ACT_IDX_VARIABLE_TASK_ID on CAMUNDA.ACT_RU_VARIABLE(TASK_ID_);
create index ACT_IDX_VARIABLE_TENANT_ID on CAMUNDA.ACT_RU_VARIABLE(TENANT_ID_);
create index ACT_IDX_VARIABLE_TASK_NAME_TYPE on CAMUNDA.ACT_RU_VARIABLE(TASK_ID_, NAME_, TYPE_);

create index ACT_IDX_ATHRZ_PROCEDEF on CAMUNDA.ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
create index ACT_IDX_INC_CONFIGURATION on CAMUNDA.ACT_RU_INCIDENT(CONFIGURATION_);
create index ACT_IDX_INC_TENANT_ID on CAMUNDA.ACT_RU_INCIDENT(TENANT_ID_);
-- CAM-5914
create index ACT_IDX_JOB_EXECUTION_ID on CAMUNDA.ACT_RU_JOB(EXECUTION_ID_);
create index ACT_IDX_JOB_HANDLER on CAMUNDA.ACT_RU_JOB(HANDLER_TYPE_,HANDLER_CFG_);
create index ACT_IDX_JOB_PROCINST on CAMUNDA.ACT_RU_JOB(PROCESS_INSTANCE_ID_);
create index ACT_IDX_JOB_TENANT_ID on CAMUNDA.ACT_RU_JOB(TENANT_ID_);
create index ACT_IDX_JOBDEF_TENANT_ID on CAMUNDA.ACT_RU_JOBDEF(TENANT_ID_);

-- new metric milliseconds column
CREATE INDEX ACT_IDX_METER_LOG_MS ON CAMUNDA.ACT_RU_METER_LOG(MILLISECONDS_);
CREATE INDEX ACT_IDX_METER_LOG_NAME_MS ON CAMUNDA.ACT_RU_METER_LOG(NAME_, MILLISECONDS_);
CREATE INDEX ACT_IDX_METER_LOG_REPORT ON CAMUNDA.ACT_RU_METER_LOG(NAME_, REPORTER_, MILLISECONDS_);

-- old metric timestamp column
CREATE INDEX ACT_IDX_METER_LOG_TIME ON CAMUNDA.ACT_RU_METER_LOG(TIMESTAMP_);
CREATE INDEX ACT_IDX_METER_LOG ON CAMUNDA.ACT_RU_METER_LOG(NAME_, TIMESTAMP_);

create index ACT_IDX_EXT_TASK_TOPIC ON CAMUNDA.ACT_RU_EXT_TASK(TOPIC_NAME_);
create index ACT_IDX_EXT_TASK_TENANT_ID ON CAMUNDA.ACT_RU_EXT_TASK(TENANT_ID_);
create index ACT_IDX_EXT_TASK_PRIORITY ON CAMUNDA.ACT_RU_EXT_TASK(PRIORITY_);
create index ACT_IDX_EXT_TASK_ERR_DETAILS ON CAMUNDA.ACT_RU_EXT_TASK(ERROR_DETAILS_ID_);
create index ACT_IDX_AUTH_GROUP_ID ON CAMUNDA.ACT_RU_AUTHORIZATION(GROUP_ID_);
create index ACT_IDX_JOB_JOB_DEF_ID on CAMUNDA.ACT_RU_JOB(JOB_DEF_ID_);

-- indexes for deadlock problems - https://app.camunda.com/jira/browse/CAM-2567 --
create index ACT_IDX_INC_CAUSEINCID on CAMUNDA.ACT_RU_INCIDENT(CAUSE_INCIDENT_ID_);
create index ACT_IDX_INC_EXID on CAMUNDA.ACT_RU_INCIDENT(EXECUTION_ID_);
create index ACT_IDX_INC_PROCDEFID on CAMUNDA.ACT_RU_INCIDENT(PROC_DEF_ID_);
create index ACT_IDX_INC_PROCINSTID on CAMUNDA.ACT_RU_INCIDENT(PROC_INST_ID_);
create index ACT_IDX_INC_ROOTCAUSEINCID on CAMUNDA.ACT_RU_INCIDENT(ROOT_CAUSE_INCIDENT_ID_);
-- index for deadlock problem - https://app.camunda.com/jira/browse/CAM-4440 --
create index ACT_IDX_AUTH_RESOURCE_ID on CAMUNDA.ACT_RU_AUTHORIZATION(RESOURCE_ID_);
-- index to prevent deadlock on fk constraint - https://app.camunda.com/jira/browse/CAM-5440 --
create index ACT_IDX_EXT_TASK_EXEC on CAMUNDA.ACT_RU_EXT_TASK(EXECUTION_ID_);

-- indexes to improve deployment
create index ACT_IDX_BYTEARRAY_ROOT_PI on CAMUNDA.ACT_GE_BYTEARRAY(ROOT_PROC_INST_ID_);
create index ACT_IDX_BYTEARRAY_RM_TIME on CAMUNDA.ACT_GE_BYTEARRAY(REMOVAL_TIME_);
create index ACT_IDX_BYTEARRAY_NAME on CAMUNDA.ACT_GE_BYTEARRAY(NAME_);
create index ACT_IDX_DEPLOYMENT_NAME on CAMUNDA.ACT_RE_DEPLOYMENT(NAME_);
create index ACT_IDX_DEPLOYMENT_TENANT_ID on CAMUNDA.ACT_RE_DEPLOYMENT(TENANT_ID_);
create index ACT_IDX_JOBDEF_PROC_DEF_ID ON CAMUNDA.ACT_RU_JOBDEF(PROC_DEF_ID_);
create index ACT_IDX_JOB_HANDLER_TYPE ON CAMUNDA.ACT_RU_JOB(HANDLER_TYPE_);
create index ACT_IDX_EVENT_SUBSCR_EVT_NAME ON CAMUNDA.ACT_RU_EVENT_SUBSCR(EVENT_NAME_);
create index ACT_IDX_PROCDEF_DEPLOYMENT_ID ON CAMUNDA.ACT_RE_PROCDEF(DEPLOYMENT_ID_);
create index ACT_IDX_PROCDEF_TENANT_ID ON CAMUNDA.ACT_RE_PROCDEF(TENANT_ID_);
create index ACT_IDX_PROCDEF_VER_TAG ON CAMUNDA.ACT_RE_PROCDEF(VERSION_TAG_);

alter table CAMUNDA.ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
    foreign key (DEPLOYMENT_ID_)
    references CAMUNDA.ACT_RE_DEPLOYMENT;

alter table CAMUNDA.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
    foreign key (PROC_INST_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
    foreign key (PARENT_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
    foreign key (SUPER_EXEC_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
    foreign key (PROC_DEF_ID_)
    references CAMUNDA.ACT_RE_PROCDEF (ID_);

alter table CAMUNDA.ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
    foreign key (TASK_ID_)
    references CAMUNDA.ACT_RU_TASK;

alter table CAMUNDA.ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
    foreign key (PROC_DEF_ID_)
    references CAMUNDA.ACT_RE_PROCDEF;

alter table CAMUNDA.ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
    foreign key (EXECUTION_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
    foreign key (PROC_INST_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_TASK
  add constraint ACT_FK_TASK_PROCDEF
  foreign key (PROC_DEF_ID_)
  references CAMUNDA.ACT_RE_PROCDEF;

alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
    foreign key (EXECUTION_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
    foreign key (PROC_INST_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
    foreign key (BYTEARRAY_ID_)
    references CAMUNDA.ACT_GE_BYTEARRAY;

alter table CAMUNDA.ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
    foreign key (EXCEPTION_STACK_ID_)
    references CAMUNDA.ACT_GE_BYTEARRAY;

alter table CAMUNDA.ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC
    foreign key (EXECUTION_ID_)
    references CAMUNDA.ACT_RU_EXECUTION;

alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_EXE
    foreign key (EXECUTION_ID_)
    references CAMUNDA.ACT_RU_EXECUTION (ID_);

alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_PROCINST
    foreign key (PROC_INST_ID_)
    references CAMUNDA.ACT_RU_EXECUTION (ID_);

alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_PROCDEF
    foreign key (PROC_DEF_ID_)
    references CAMUNDA.ACT_RE_PROCDEF (ID_);

alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_CAUSE
    foreign key (CAUSE_INCIDENT_ID_)
    references CAMUNDA.ACT_RU_INCIDENT (ID_);

alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_RCAUSE
    foreign key (ROOT_CAUSE_INCIDENT_ID_)
    references CAMUNDA.ACT_RU_INCIDENT (ID_);

alter table CAMUNDA.ACT_RU_EXT_TASK
    add constraint ACT_FK_EXT_TASK_ERROR_DETAILS
    foreign key (ERROR_DETAILS_ID_)
    references CAMUNDA.ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_INC_JOB_DEF on CAMUNDA.ACT_RU_INCIDENT(JOB_DEF_ID_);
alter table CAMUNDA.ACT_RU_INCIDENT
    add constraint ACT_FK_INC_JOB_DEF
    foreign key (JOB_DEF_ID_)
    references CAMUNDA.ACT_RU_JOBDEF (ID_);

alter table CAMUNDA.ACT_RU_AUTHORIZATION
    add constraint ACT_UNIQ_AUTH_USER
    unique (TYPE_, USER_ID_,RESOURCE_TYPE_,RESOURCE_ID_);

alter table CAMUNDA.ACT_RU_AUTHORIZATION
    add constraint ACT_UNIQ_AUTH_GROUP
    unique (TYPE_, GROUP_ID_,RESOURCE_TYPE_,RESOURCE_ID_);

alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_UNIQ_VARIABLE
    unique (VAR_SCOPE_, NAME_);

alter table CAMUNDA.ACT_RU_EXT_TASK
    add constraint ACT_FK_EXT_TASK_EXE
    foreign key (EXECUTION_ID_)
    references CAMUNDA.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_BATCH_SEED_JOB_DEF ON CAMUNDA.ACT_RU_BATCH(SEED_JOB_DEF_ID_);
alter table CAMUNDA.ACT_RU_BATCH
    add constraint ACT_FK_BATCH_SEED_JOB_DEF
    foreign key (SEED_JOB_DEF_ID_)
    references CAMUNDA.ACT_RU_JOBDEF (ID_);

create index ACT_IDX_BATCH_MONITOR_JOB_DEF ON CAMUNDA.ACT_RU_BATCH(MONITOR_JOB_DEF_ID_);
alter table CAMUNDA.ACT_RU_BATCH
    add constraint ACT_FK_BATCH_MONITOR_JOB_DEF
    foreign key (MONITOR_JOB_DEF_ID_)
    references CAMUNDA.ACT_RU_JOBDEF (ID_);

create index ACT_IDX_BATCH_JOB_DEF ON CAMUNDA.ACT_RU_BATCH(BATCH_JOB_DEF_ID_);
alter table CAMUNDA.ACT_RU_BATCH
    add constraint ACT_FK_BATCH_JOB_DEF
    foreign key (BATCH_JOB_DEF_ID_)
    references CAMUNDA.ACT_RU_JOBDEF (ID_);

create index ACT_IDX_BATCH_ID ON CAMUNDA.ACT_RU_VARIABLE(BATCH_ID_);
alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BATCH
    foreign key (BATCH_ID_)
    references CAMUNDA.ACT_RU_BATCH (ID_);

-- indices for history cleanup: https://jira.camunda.com/browse/CAM-11616
create index ACT_IDX_AUTH_ROOT_PI on CAMUNDA.ACT_RU_AUTHORIZATION(ROOT_PROC_INST_ID_);
create index ACT_IDX_AUTH_RM_TIME on CAMUNDA.ACT_RU_AUTHORIZATION(REMOVAL_TIME_);







--              _   _       _ _   _   _     ____                       _         _     _     _                               _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___  | |__ (_)___| |_ ___  _ __ _   _   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ | '_ \| / __| __/ _ \| '__| | | | / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/_| | | | \__ \ || (_) | |  | |_| |_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)_| |_|_|___/\__\___/|_|   \__, (_)___/\__, |_|
--                                                                                                         |___/          |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.history.sql






--              _   _       _ _   _   _     ____                       _                                             _                        _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___   ___ __ _ ___  ___   ___ _ __   __ _(_)_ __   ___   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ / __/ _` / __|/ _ \ / _ \ '_ \ / _` | | '_ \ / _ \ / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/| (_| (_| \__ \  __/|  __/ | | | (_| | | | | |  __/_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)___\__,_|___/\___(_)___|_| |_|\__, |_|_| |_|\___(_)___/\__, |_|
--                                                                                                             |___/                       |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.case.engine.sql

-- create case definition table --

create table CAMUNDA.ACT_RE_CASE_DEF (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255) NOT NULL,
    VERSION_ integer NOT NULL,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    TENANT_ID_ varchar(64),
    HISTORY_TTL_ integer,
    primary key (ID_)
);

-- create case execution table --

create table CAMUNDA.ACT_RU_CASE_EXECUTION (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CASE_INST_ID_ varchar(64),
    SUPER_CASE_EXEC_ varchar(64),
    SUPER_EXEC_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    CASE_DEF_ID_ varchar(64),
    ACT_ID_ varchar(255),
    PREV_STATE_ integer,
    CURRENT_STATE_ integer,
    REQUIRED_ bit,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

-- create case sentry part table --

create table CAMUNDA.ACT_RU_CASE_SENTRY_PART (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CASE_INST_ID_ varchar(64),
    CASE_EXEC_ID_ varchar(64),
    SENTRY_ID_ varchar(255),
    TYPE_ varchar(255),
    SOURCE_CASE_EXEC_ID_ varchar(64),
    STANDARD_EVENT_ varchar(255),
    SOURCE_ varchar(255),
    VARIABLE_EVENT_ varchar(255),
    VARIABLE_NAME_ varchar(255),
    SATISFIED_ bit,
    TENANT_ID_ varchar(64),
    primary key (ID_)
);

-- create index on business key --
create index ACT_IDX_CASE_EXEC_BUSKEY on CAMUNDA.ACT_RU_CASE_EXECUTION(BUSINESS_KEY_);

-- create foreign key constraints on ACT_RU_CASE_EXECUTION --
alter table CAMUNDA.ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_CASE_INST
    foreign key (CASE_INST_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

alter table CAMUNDA.ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_PARENT
    foreign key (PARENT_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

alter table CAMUNDA.ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_CASE_DEF
    foreign key (CASE_DEF_ID_)
    references CAMUNDA.ACT_RE_CASE_DEF;

-- create foreign key constraints on ACT_RU_VARIABLE --
alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_CASE_EXE
    foreign key (CASE_EXECUTION_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

alter table CAMUNDA.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_CASE_INST
    foreign key (CASE_INST_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

-- create foreign key constraints on ACT_RU_TASK --
alter table CAMUNDA.ACT_RU_TASK
    add constraint ACT_FK_TASK_CASE_EXE
    foreign key (CASE_EXECUTION_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

alter table CAMUNDA.ACT_RU_TASK
  add constraint ACT_FK_TASK_CASE_DEF
  foreign key (CASE_DEF_ID_)
  references CAMUNDA.ACT_RE_CASE_DEF;

-- create foreign key constraints on ACT_RU_CASE_SENTRY_PART --
alter table CAMUNDA.ACT_RU_CASE_SENTRY_PART
    add constraint ACT_FK_CASE_SENTRY_CASE_INST
    foreign key (CASE_INST_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

alter table CAMUNDA.ACT_RU_CASE_SENTRY_PART
    add constraint ACT_FK_CASE_SENTRY_CASE_EXEC
    foreign key (CASE_EXEC_ID_)
    references CAMUNDA.ACT_RU_CASE_EXECUTION;

create index ACT_IDX_CASE_DEF_TENANT_ID on CAMUNDA.ACT_RE_CASE_DEF(TENANT_ID_);
create index ACT_IDX_CASE_EXEC_TENANT_ID on CAMUNDA.ACT_RU_CASE_EXECUTION(TENANT_ID_);





--              _   _       _ _   _   _     ____                       _                             _     _     _                               _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___   ___ __ _ ___  ___  | |__ (_)___| |_ ___  _ __ _   _   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ / __/ _` / __|/ _ \ | '_ \| / __| __/ _ \| '__| | | | / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/| (_| (_| \__ \  __/_| | | | \__ \ || (_) | |  | |_| |_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)___\__,_|___/\___(_)_| |_|_|___/\__\___/|_|   \__, (_)___/\__, |_|
--                                                                                                                             |___/          |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.case.history.sql




--        _   _       _ _   _   _     ____                       _            _           _     _                               _                        _
--    ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___   __| | ___  ___(_)___(_) ___  _ __    ___ _ __   __ _(_)_ __   ___   ___  __ _| |
--   / __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ / _` |/ _ \/ __| / __| |/ _ \| '_ \  / _ \ '_ \ / _` | | '_ \ / _ \ / __|/ _` | |
--  | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/| (_| |  __/ (__| \__ \ | (_) | | | ||  __/ | | | (_| | | | | |  __/_\__ \ (_| | |
--   \___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)__,_|\___|\___|_|___/_|\___/|_| |_(_)___|_| |_|\__, |_|_| |_|\___(_)___/\__, |_|
--                                                                                                                        |___/                       |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.decision.engine.sql







--              _   _       _ _   _   _     ____                       _            _           _     _               _     _     _                               _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___   __| | ___  ___(_)___(_) ___  _ __   | |__ (_)___| |_ ___  _ __ _   _   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ / _` |/ _ \/ __| / __| |/ _ \| '_ \  | '_ \| / __| __/ _ \| '__| | | | / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/| (_| |  __/ (__| \__ \ | (_) | | | |_| | | | \__ \ || (_) | |  | |_| |_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)__,_|\___|\___|_|___/_|\___/|_| |_(_)_| |_|_|___/\__\___/|_|   \__, (_)___/\__, |_|
--                                                                                                                                              |___/          |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.decision.history.sql





--              _   _       _ _   _   _     ____                       _         _     _            _   _ _                     _
--    __ _  ___| |_(_)_   _(_) |_(_) | |__ |___ \   ___ _ __ ___  __ _| |_ ___  (_) __| | ___ _ __ | |_(_) |_ _   _   ___  __ _| |
--   / _` |/ __| __| \ \ / / | __| | | '_ \  __) | / __| '__/ _ \/ _` | __/ _ \ | |/ _` |/ _ \ '_ \| __| | __| | | | / __|/ _` | |
--  | (_| | (__| |_| |\ V /| | |_| |_| | | |/ __/ | (__| | |  __/ (_| | ||  __/_| | (_| |  __/ | | | |_| | |_| |_| |_\__ \ (_| | |
--   \__,_|\___|\__|_| \_/ |_|\__|_(_)_| |_|_____(_)___|_|  \___|\__,_|\__\___(_)_|\__,_|\___|_| |_|\__|_|\__|\__, (_)___/\__, |_|
--                                                                                                            |___/          |_|
-- camunda-bpm-platform/engine/src/main/resources/org/camunda/bpm/engine/db/create/activiti.h2.create.identity.sql
































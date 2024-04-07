#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine rollback_demo_postgresql_database.sh


liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo-party-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver clear-checksums
liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"

liquibase --changeLogFile=../inception-sms/src/main/resources/db/inception-sms.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-security/src/main/resources/db/inception-security.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-scheduler/src/main/resources/db/inception-scheduler.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-reporting/src/main/resources/db/inception-reporting.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-skills-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-industry-classification-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-mail/src/main/resources/db/inception-mail.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-error/src/main/resources/db/inception-error.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-config/src/main/resources/db/inception-config.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-codes/src/main/resources/db/inception-codes.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
#liquibase --changeLogFile=../inception-audit/src/main/resources/db/inception-audit.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"

#liquibase --changeLogFile=../inception-application/src/main/resources/db/inception-application.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"
liquibase --changeLogFile=../inception-core/src/main/resources/db/inception-core.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver rollback-to-date --date "2020-01-01"




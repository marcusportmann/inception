#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine initialize_demo_postgresql_database.sh

liquibase --changeLogFile=../inception-core/src/main/resources/db/inception-core.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update

liquibase --changeLogFile=../inception-codes/src/main/resources/db/inception-codes.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-config/src/main/resources/db/inception-config.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-error/src/main/resources/db/inception-error.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-mail/src/main/resources/db/inception-mail.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-reporting/src/main/resources/db/inception-reporting.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-scheduler/src/main/resources/db/inception-scheduler.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-security/src/main/resources/db/inception-security.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update

liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-industry-classification-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-skills-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update


liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo-data.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=drivers/postgresql.jar --driver=org.postgresql.Driver update


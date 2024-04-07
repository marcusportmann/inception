#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine initialize_demo_oracle_database.sh

programname=$0

function usage {
    echo "Usage: $programname -h host -p port -s service_name -u username -w password"
    echo "Options:"
    echo "  -h HOST         The Oracle database hostname."
    echo "  -p PORT         The Oracle database port."
    echo "  -s SERVICE_NAME The Oracle database service name."
    echo "  -u USERNAME     The Oracle database username."
    echo "  -w PASSWORD     The Oracle database password."
}

function update_liquibase_changelogs {
  local db_host=$1
  local db_port=$2
  local db_service=$3
  local db_username=$4
  local db_password=$5
  
  echo "Applying Liquibase changelogs to the database $db_service on the host $db_host with port $db_port using the username $db_username and password $db_password."
  
  liquibase --changeLogFile=../inception-core/src/main/resources/db/inception-core.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  #liquibase --changeLogFile=../inception-application/src/main/resources/db/inception-application.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update

  #liquibase --changeLogFile=../inception-audit/src/main/resources/db/inception-audit.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-codes/src/main/resources/db/inception-codes.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-config/src/main/resources/db/inception-config.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-error/src/main/resources/db/inception-error.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-mail/src/main/resources/db/inception-mail.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-industry-classification-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-party/src/main/resources/db/inception-party-skills-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-reporting/src/main/resources/db/inception-reporting.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-scheduler/src/main/resources/db/inception-scheduler.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-sms/src/main/resources/db/inception-sms.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-security/src/main/resources/db/inception-security.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update

  liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
  liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-party-demo-data.changelog.xml --username=$db_username --password=$db_password --url=jdbc:oracle:thin:@$db_host:$db_port:$db_service --classpath=drivers/ojdbc11-23.2.0.0.jar --driver=oracle.jdbc.driver.OracleDriver update
}

if [ "$#" -ne 10 ]; then
    usage
    exit 1
fi

while getopts h:p:s:u:w: option
do
case "${option}" in
    h)
        db_host=${OPTARG}
        ;;
    p)
        db_port=${OPTARG}
        ;;
    s)
        db_service=${OPTARG}
        ;;
    u)
        db_username=${OPTARG}
        ;;
    w)
        db_password=${OPTARG}
        ;;
    \? )
        echo "Invalid Option: -$OPTARG" 1>&2
        exit 1
        ;;
esac
done

update_liquibase_changelogs $db_host $db_port $db_service $db_username $db_password


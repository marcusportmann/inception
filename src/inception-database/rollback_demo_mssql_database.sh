#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine rollback_demo_mssql_database.sh

programname=$0

function usage {
    echo "Usage: $programname -h host -p port -n database_name -u username -w password -d date"
    echo "Options:"
    echo "  -h HOST          The MS SQL database hostname."
    echo "  -p PORT          The MS SQL database port."
    echo "  -n DATABASE_NAME The MS SQL database name."
    echo "  -u USERNAME      The MS SQL database username."
    echo "  -w PASSWORD      The MS SQL database password."
    echo "  -d DATE          The date to rollback to."
}

function rollback_liquibase_changelogs {
  local db_host=$1
  local db_port=$2
  local db_name=$3
  local db_username=$4
  local db_password=$5
  local rollback_date=$6
  
  echo "Rolling Liquibase changelogs for the database $db_name on the host $db_host with port $db_port using the username $db_username and password $db_password to date $rollback_date."
  
  liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo-data.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-demo/src/main/resources/db/inception-demo.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  
  liquibase --changeLogFile=../inception-security/src/main/resources/db/inception-security.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-scheduler/src/main/resources/db/inception-scheduler.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-reporting/src/main/resources/db/inception-reporting.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference-data.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-reference/src/main/resources/db/inception-reference.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-mail/src/main/resources/db/inception-mail.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-error/src/main/resources/db/inception-error.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-config/src/main/resources/db/inception-config.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
  liquibase --changeLogFile=../inception-codes/src/main/resources/db/inception-codes.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
    
  liquibase --changeLogFile=../inception-core/src/main/resources/db/inception-core.changelog.xml --username=$db_username --password=$db_password --url="jdbc:sqlserver://$db_host:$db_port;databaseName=$db_name;trustServerCertificate=true" --classpath=drivers/mssql-jdbc.jar --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver rollback-to-date --date "$rollback_date"  
}

if [ "$#" -ne 12 ]; then
    usage
    exit 1
fi

while getopts h:p:n:u:w:d: option
do
case "${option}" in
    h)
        db_host=${OPTARG}
        ;;
    p)
        db_port=${OPTARG}
        ;;
    n)
        db_name=${OPTARG}
        ;;
    u)
        db_username=${OPTARG}
        ;;
    w)
        db_password=${OPTARG}
        ;;
    d)
        rollback_date=${OPTARG}
        ;;
    \? )
        echo "Invalid Option: -$OPTARG" 1>&2
        exit 1
        ;;
esac
done

rollback_liquibase_changelogs $db_host $db_port $db_name $db_username $db_password $rollback_date



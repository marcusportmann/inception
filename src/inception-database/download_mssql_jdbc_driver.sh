#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine download_mssql_jdbc_driver.sh

curl -o drivers/mssql-jdbc.jar https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/11.2.3.jre17/mssql-jdbc-11.2.3.jre17.jar

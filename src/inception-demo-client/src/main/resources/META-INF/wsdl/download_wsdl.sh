#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   chmod a+x download_wsdl.sh && xattr -d com.apple.quarantine download_wsdl.sh

curl "http://localhost:8080/service/CodesService?wsdl" -o CodesService.wsdl
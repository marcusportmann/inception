#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   chmod a+x download_swagger.sh && xattr -d com.apple.quarantine download_swagger.sh

curl "http://localhost:8080/v3/api-docs/reference" -o ReferenceAPI.json
curl "http://localhost:8080/v3/api-docs/demo" -o DemoAPI.json

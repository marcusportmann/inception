#!/bin/bash

# Define variables
TOKEN_URL="http://localhost:8080/oauth/token"
USERNAME="Administrator"
PASSWORD="Password1"
CLIENT_ID="client_id"
CLIENT_SECRET="client_secret"
GRANT_TYPE="password"

# Loop to request tokens
for i in {1..600}; do
  # Invoke the OAuth2 token endpoint
  RESPONSE=$(curl -s -w "%{http_code}" -o response.json -X POST "$TOKEN_URL" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -u "$CLIENT_ID:$CLIENT_SECRET" \
    -d "grant_type=$GRANT_TYPE" \
    -d "username=$USERNAME" \
    -d "password=$PASSWORD")

  HTTP_CODE=${RESPONSE: -3} # Extract the last three characters for the HTTP status code

  if [ "$HTTP_CODE" -eq 200 ]; then
    echo "Request $i: Success"
    # cat response.json # Print the access token response
    # echo ""
  else
    echo "Request $i: Failed with HTTP $HTTP_CODE."
    cat response.json # Print the error response
    exit 1 # Exit immediately on non-200 HTTP code
  fi
done


#!/bin/sh

# Loop to request tokens
for i in {1..800}; do
  RESPONSE=$(curl -s -w "%{http_code}" -X 'GET' \
    -H 'accept: application/json' \
    'http://localhost:8080/api/config/configs/AnotherTestId')

  HTTP_CODE=${RESPONSE: -3} # Extract the last three characters for the HTTP status code

  if [ "$HTTP_CODE" -eq 200 ]; then
    echo "Request $i: Success"
  else
    echo "Request $i: Failed with HTTP $HTTP_CODE."
    cat response.json # Print the error response
    exit 1 # Exit immediately on non-200 HTTP code
  fi
done


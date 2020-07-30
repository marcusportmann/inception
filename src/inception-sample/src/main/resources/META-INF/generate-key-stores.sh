#!/bin/sh
rm -f sample.p12
rm -f sample.crt
rm -f ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.p12
rm -f ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.crt

# Create the keystores for the sample and sample-client applications
keytool -genkeypair -keystore sample.p12 -storetype pkcs12 -storepass sample -alias sample -keyalg RSA -keysize 2048 -keypass sample -validity 7300 -dname "CN=sample" -ext san=dns:sample.local,dns:localhost,ip:127.0.0.1
keytool -genkeypair -keystore ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.p12 -storetype pkcs12 -storepass sample-client -alias sample-client -keyalg RSA -keysize 2048 -keypass sample-client -validity 7300 -dname "CN=sample-client"

# Extract the certificates from the sample and sample-client keystores
keytool -list -rfc -keystore sample.p12 -storetype pkcs12 -storepass sample | awk '/-----BEGIN CERTIFICATE-----/,/-----END CERTIFICATE-----/' > sample.crt
keytool -list -rfc -keystore ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.p12 -storetype pkcs12 -storepass sample-client | awk '/-----BEGIN CERTIFICATE-----/,/-----END CERTIFICATE-----/' > ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.crt

# Import the certificates into the sample and sample-client keystores
keytool -importcert -trustcacerts -noprompt -keystore sample.p12 -storetype pkcs12 -storepass sample -alias sample-client -file ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.crt
keytool -importcert -trustcacerts -noprompt -keystore ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.p12 -storetype pkcs12 -storepass sample-client -alias sample -file sample.crt

# Dump the sample and sample-client keystores
keytool -list -keystore sample.p12 -storetype pkcs12 -storepass sample
keytool -list -keystore ../../../../../inception-sample-client/src/main/resources/META-INF/sample-client.p12 -storetype pkcs12 -storepass sample-client

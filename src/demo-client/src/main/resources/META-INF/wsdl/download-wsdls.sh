#!/bin/sh
curl "http://localhost:8080/service/CodesService?wsdl" -o CodesService.wsdl
curl "http://localhost:8080/service/ConfigurationService?wsdl" -o ConfigurationService.wsdl
curl "http://localhost:8080/service/DemoService?wsdl" -o DemoService.wsdl
curl "http://localhost:8080/service/ErrorService?wsdl" -o ErrorService.wsdl
curl "http://localhost:8080/service/MailService?wsdl" -o MailService.wsdl
#curl "http://localhost:8080/service/MessagingService?wsdl" -o MessagingService.wsdl
curl "http://localhost:8080/service/ReportingService?wsdl" -o ReportingService.wsdl
curl "http://localhost:8080/service/SchedulerService?wsdl" -o SchedulerService.wsdl
curl "http://localhost:8080/service/SecurityService?wsdl" -o SecurityService.wsdl
curl "http://localhost:8080/service/SMSService?wsdl" -o SMSService.wsdl
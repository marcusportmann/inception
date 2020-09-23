# Inception Framework

## Overview
The Inception Framework enables the rapid development of Java back-end and Angular
front-end applications.

## Quickstart for MacOS

### Setup a development environment on MacOS

Complete the following steps to setup a development environment on MacOS.

1. Install the Xcode Command Line Tools by executing the following command in a Terminal
   window.
   ```
   xcode-select --install
   ```
2. Install Homebrew by executing the following command in a Terminal window.
   ```
   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
   ```
3. Install OpenJDK 11 by executing the following commands in a Terminal window.
   ```
   brew tap AdoptOpenJDK/openjdk
   brew cask install adoptopenjdk11
   ```
4. Install Apache Maven by executing the following command in a Terminal window.
   ```
   brew install maven
   ```
5. Install jenv by executing the following commands in a Terminal window.
   ```
   brew install jenv
   ```
6. Add the following lines to your .zshrc or .bash_profile file to enable jenv and restart
   your Terminal.
   ```
   export PATH="$HOME/.jenv/bin:$PATH"
   eval "$(jenv init -)"
   ```
7. Set OpenJDK 11 as the default java verison by executing the following commands in a
   Terminal window.
   ```
   jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
   jenv global 11.0
   ```
8. Install the maven plugin for jenv by executing the following command in a Terminal
   window.
   ```
   jenv enable-plugin maven
   ```
9. Install Postgres by executing the following command in a Terminal window.
   ```
   brew install postgres
   ```
10. Install liquibase by executing the following commands in a Terminal window.
    ```
    brew install liquibase
    ```
11. Install npm by executing the following commands in a Terminal window.
    ```
    brew install npm
    ```
12. Install the Angular CLI 10 globally by executing the following command in a Terminal
    window.
    ```
    npm install -g @angular/cli@10
    ```
13. Execute the following command to change to the more conservative tilde (~) patch
    update approach for dependencies for npm.
    ```
    npm config set save-prefix="~"
    ```

### Setup MailSlurper on MacOS

MailSlurper is a handy SMTP mail server useful for local and team application development.
Mails sent by an application running locally on a developer's machine can be viewed using
a web interface. This is useful when developing and testing an application locally.

Complete the following steps to setup MailSlurper on MacOS.

1. Download the MailSlurper package from *https://github.com/mailslurper/mailslurper/releases*.
2. Extract the MailSlurper package.
3. Edit the *config.json* file and set the *wwwPort* value to *8090*.
4. Launch the *createcredentials* binary in a Terminal window and enter the username as
   *inception* and password as *inception* when prompted.

   NOTE: You may need to enable the execution of the *mailslurper* binary under
   **System Preferences > Security & Privacy > General**
5. Launch the *mailslurper* binary in a Terminal window.

   NOTE: You may need to enable the execution of the *mailslurper* binary under
   **System Preferences > Security & Privacy > General**


### Checkout and build the Inception Framework on MacOS

Complete the following steps to checkout and build the Inception Framework on MacOS.

1. Checkout the Inception Framework by executing the following command in a Terminal
   window.
   ```
   git clone https://github.com/marcusportmann/inception.git
   ```
2. Build and install the Java components of the Inception Framework by executing the
   following commands in the *inception/src* directory in a Terminal window.
   ```
   mvn clean compile package install
   ```
2. Build the Angular components of the Inception Framework by executing the
   following commands in the *inception/src/inception-angular* directory in a Terminal
   window.
   ```
   npm i
   ng build
   cd dist/ngx-inception
   npm link
   ```
3. To launch the *inception-sample* back-end application, execute the following command
   in the *src/inception-sample/target* directory in a Terminal window, after building
   the Java components of the Inception Framework.
   ```
   java -jar inception-sample-1.0.0-SNAPSHOT.jar
   ```
4. To launch the *inception-sample* front-end application, execute the following command
   in the *inception/src/inception-angular* directory in a Terminal window, after building
   the Angular components of the Inception Framework.
   ```
   ng serve --host 0.0.0.0
   ```

## Quickstart for Windows

### Setup a development environment on Windows

1. Download the Git for Windows package from *https://git-scm.com/download/win* and
   install it.

   Select the following options when installing Git:
   ```
   Use the native Windows Secure Channel library
   Checkout Windows-style, commit Unix-style line endings
   Use MinTTY (the default terminal of MSYS2)
   Default (fast-forward or merge)
   Git Credential Manager
   Enable file system caching
   ```
2. Download the OpenJDK 11 package from *https://adoptopenjdk.net* and install it.
3. Download the Maven package from *https://maven.apache.org* and install it.

   **NOTE:** Ensure that Maven is on the path.

   **NOTE:** Add the proxy settings to the *~/.m2/settings.xml* file if required.
4. Download the Node.js and npm package from *https://www.npmjs.com/get-npm* and install
   it.

   **NOTE:** Set the proxy for NPM if required.
5. Install the Angular CLI 10 globally by executing the following command in a Git Bash
   window.
   ```
   npm install -g @angular/cli@10
   ```
6. Execute the following command to change to the more conservative tilde (~) patch
   update approach for dependencies for npm.
   ```
   npm config set save-prefix="~"
   ```

### Checkout and build the Inception Framework on Windows

Complete the following steps to setup a development environment on Windows.

1. Checkout the Inception Framework by executing the following command in a Terminal
   window.
   ```
   git clone https://github.com/marcusportmann/inception.git
   ```
2. Build and install the Java components of the Inception Framework by executing the
   following commands in the *inception/src* directory in a Terminal window.
   ```
   mvn clean compile package install
   ```
2. Build the Angular components of the Inception Framework by executing the
   following commands in the *inception/src/inception-angular* directory in a Terminal
   window.
   ```
   npm i
   ng build
   cd dist/ngx-inception
   npm link
   ```
3. To launch the *inception-sample* back-end application, execute the following command
   in the *src/inception-sample/target* directory in a Terminal window, after building
   the Java components of the Inception Framework.
   ```
   java -jar inception-sample-1.0.0-SNAPSHOT.jar
   ```
4. To launch the *inception-sample* front-end application, execute the following command
   in the *inception/src/inception-angular* directory in a Terminal window, after building
   the Angular components of the Inception Framework.
   ```
   ng serve --host 0.0.0.0
   ```

## Creating a new application based on the Inception Framework

Complete the following steps to create a new application based on the Inception Framework.

1. Select a name for the new application, e.g. demo, and create the top-level directory
   for the application with the same name. The directory **MUST** be created under the
   same directory that the Inception Framework project was cloned into. This is because
   the Inception Framework will be installed as an npm dependency using a relative path.
2. Execute the following commands under the top-level directory for the new application
   to create the project directory structure.
   ```
   mkdir -p src/main/java
   mkdir -p src/main/java-templates
   mkdir -p src/main/resources/META-INF
   mkdir -p src/test/java
   mkdir -p src/test/resources
   ```
3. Setup the Spring Boot back-end for the application.
   1. Execute the following commands under the top-level directory for the new application
      to generate the RSA keypair used to sign and verify OAuth2 JWTs issued by the
      application.
      ```
      openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -pkeyopt rsa_keygen_pubexp:65537 | openssl pkcs8 -topk8 -nocrypt -outform pem > src/main/resources/META-INF/oauth2-jwt-key
      openssl pkey -pubout -inform pem -outform pem -in src/main/resources/META-INF/oauth2-jwt-key -out src/main/resources/META-INF/oauth2-jwt-key.pub
      ```
   2. Create the *pom.xml* file under the *src* directory with the following contents,
      changing the *groupId*, *artifactId*, *version*, *name* and *description* values
      as appropriate.

      **NOTE:** If you do not require all the capabilities provided by the Inception
      Framework you can remove the appropriate dependencies from the *pom.xml* file, e.g.
      removing the *inception-reporting-rs* dependency will remove the JasperReports-based
      reporting functionality.
      ```
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <groupId>digital.inception</groupId>
        <artifactId>demo</artifactId>
        <version>1.0.0-SNAPSHOT</version>

        <name>Demo</name>
        <description></description>

        <parent>
          <groupId>digital.inception</groupId>
          <artifactId>inception</artifactId>
          <version>1.0.0-SNAPSHOT</version>
          <relativePath/>
        </parent>

        <dependencies>
          <!-- Inception Dependencies -->
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-application</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-codes-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-configuration-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-error-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-mail-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-oauth2-authorization-server </artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-oauth2-resource-server </artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-persistence</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-reporting-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-scheduler-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-security-rs</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-validation</artifactId>
          </dependency>

          <!-- Dependencies -->
          <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
          </dependency>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
          </dependency>

          <!-- Test Dependencies -->
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-test</artifactId>
            <scope>test</scope>
          </dependency>
        </dependencies>

        <build>
          <plugins>
            <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
              <groupId>com.github.eirslett</groupId>
              <artifactId>frontend-maven-plugin</artifactId>
              <version>1.6</version>
              <configuration>
                <workingDirectory>src/main/frontend</workingDirectory>
                <nodeVersion>v14.7.0</nodeVersion>
                <npmVersion>6.14.7</npmVersion>
              </configuration>
              <executions>
                <execution>
                  <id>install node and npm</id>
                  <goals>
                    <goal>install-node-and-npm</goal>
                  </goals>
                </execution>
                <execution>
                  <id>npm install</id>
                  <goals>
                    <goal>npm</goal>
                  </goals>
                </execution>
                <execution>
                  <id>npm run build</id>
                  <goals>
                    <goal>npm</goal>
                  </goals>
                  <configuration>
                    <arguments>run build</arguments>
                  </configuration>
                </execution>
              </executions>
            </plugin>
          </plugins>
        </build>

      </project>
      ```
   3. Create the class and resource packages for the application. The classes and
      resources for the application will be created under these packages, e.g. the Spring
      Boot application class, the in-memory H2 database scripts for the application, etc.
      ```
      mkdir -p src/main/java/digital/inception/demo
      mkdir -p src/main/resources/digital/inception/demo
      ```
   4. Add the Spring Boot Application class,
      e.g. *src/main/java/digital/inception/demo/DemoApplication.java*, that extends the
      Inception Framework application class (digital.inception.application.Application).
      ```
      package digital.inception.demo;

      // ~--- non-JDK imports --------------------------------------------------------

      import digital.inception.application.Application;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      import org.springframework.boot.SpringApplication;
      import org.springframework.boot.autoconfigure.SpringBootApplication;
      import org.springframework.context.ApplicationContext;
      import org.springframework.context.annotation.ComponentScan;
      import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

      // ~--- JDK imports ------------------------------------------------------------

      /**
       * The <code>DemoApplication</code> provides the implementation of the Inception Framework
       * application class for the Demo application.
       */
      @SpringBootApplication
      @ComponentScan(
          basePackages = {"digital.inception.demo"})
      @EnableJpaRepositories(
          entityManagerFactoryRef = "applicationPersistenceUnit",
          basePackages = {"digital.inception.demo"})
      public class DemoApplication extends Application {

        /* Logger */
        private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

        /**
         * Constructs a new <code>DemoApplication</code>.
         *
         * @param applicationContext the Spring application context
         */
        public DemoApplication(
            ApplicationContext applicationContext) {
          super(applicationContext);
        }

        /**
         * The main method.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
          SpringApplication.run(DemoApplication.class, args);
        }
      }
      ```
   5. Add the Spring application configuration file, *src/main/resources/application.yml*,
      changing the *spring.application.name* value to the name of the application.
      ```
      server:
        port: 8080

      inception:
        application:
          data-source:
            class-name: org.h2.jdbcx.JdbcDataSource
            url: jdbc:h2:mem:application;MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
            min-pool-size: 5
            max-pool-size: 10
        oauth2:
          authorization-server:
            jwt:
              private-key-location: classpath:META-INF/oauth2-jwt-key
              public-key-location: classpath:META-INF/oauth2-jwt-key.pub

      spring:
        application:
          name: demo
        security:
          oauth2:
            resourceserver:
              jwt:
                public-key-location: classpath:META-INF/oauth2-jwt-key.pub
      ```
   6. Add the in-memory H2 database script for the application,
      e.g. *src/main/resources/digital/inception/demo/demo-h2.sql*, to the project. This
      file will contain all the Data Definition Language (DDL) and Data Manipulation
      Language (DML) commands use to initialize the in-memory H2 database for the
      application. This database allows developers to run the application locally while
      developing the application and is also used to execute all Junit tests for the
      application as part of the build process.
      ```
      -- -------------------------------------------------------------------------------------------------
      -- CREATE SCHEMAS
      -- -------------------------------------------------------------------------------------------------


      -- -------------------------------------------------------------------------------------------------
      -- CREATE TABLES
      -- -------------------------------------------------------------------------------------------------


      -- -------------------------------------------------------------------------------------------------
      -- POPULATE TABLES
      -- -------------------------------------------------------------------------------------------------
4. Setup the Angular front-end for the application.

   1. Execute the following command under the *src/main* directory to create the new
      Angular frontend.
      ```
      ng new --skip-git --routing --style scss frontend
      ```
   2. Edit the *src/main/frontend/angular.json* file and add the fontawesome path to the
      *styles* array as shown below.
      ```
      {
        ...

        "projects": {
          "frontend": {
            ...
            "architect": {
              "build": {
                ...
                "options": {
                  ...
                  "styles": [
                    "node_modules/@fortawesome/fontawesome-free/css/all.css",
                    "src/styles.scss"
                  ],
                  ...
                },
        ...
      }
      ```
   3. Edit the *src/main/frontend/angular.json* file and change the *outputPath* option to
      *../../../target/classes/static*, add the *preserveSymlinks* option with a value
      of *true*, and set the *aot* option to *true* as shown below. This will result in the Angular font-end being packaged as
      part of the Spring Boot application as a set of static resources.
      ```
      {
        ...
        "projects": {
          "frontend": {
            ...
            "architect": {
              "build": {
                ...
                "options": {
                  "preserveSymlinks": true,
                  "outputPath": "../../../target/classes/static",
                  ...
                  "aot": true,
                  ...
                },
        ...
      }
      ```
   4. Execute the following commands under the *src/main/frontend* directory to install the
      dependencies for the *ngx-inception* library.
      ```
      npm install --save @angular/cdk@10
      npm install --save @angular/localize@10
      npm install --save @angular/material@10
      npm install --save @angular/material-moment-adapter@10
      npm install --save @auth0/angular-jwt@5
      npm install --save @fortawesome/fontawesome-free@5
      npm install --save bootstrap@4
      npm install --save jquery@3
      npm install --save moment@2
      npm install --save ngx-perfect-scrollbar@10
      npm install --save string-template@1
      npm install --save uuid@8

      npm install --save-dev @types/uuid@8
      npm install --save-dev @types/string-template@1
      npm install --save-dev codelyzer
      ```
   5. Execute the following command under the *src/main/frontend* directory to install the
      local *ngx-inception* library dependency using a relative path.
      ```
      npm install ../../../../inception/src/inception-angular/dist/ngx-inception
      ```
   6. Edit the *src/main/frontend/src/polyfills.ts* file and add the line below under the
      *APPLICATION IMPORTS* section.
      ```
      import '@angular/localize/init';
      ```
   7. Add the *BrowserAnimationsModule* and Inception Framework imports to the
      *src/main/frontend/src/app/app.module.ts* file.
      ```
      ...

      import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

      import {
        InceptionAppModule, InceptionConfig, InceptionModule, NavigationBadge, NavigationItem, NavigationTitle
      } from 'ngx-inception';

      ...
      ```
   8. Add the *ngxInceptionConfig* to the *src/main/frontend/src/app/app.module.ts* file.

      **NOTE:** If the application has a logged out landing page, which unauthenticated
      users can access, then the *logoutRedirectUri* should be changed to the URL for this
      page.
      ```
      ...

      const ngxInceptionConfig: InceptionConfig = {
        // Application Information
        applicationId: 'digital.inception.demo',
        applicationVersion: '1.0.0',

        // OAuth Token URL
        oauthTokenUrl: 'http://localhost:8080/oauth/token',

        // Logout redirect URI
        logoutRedirectUri: '/login',

        // Inception API URLs
        codesApiUrlPrefix: 'http://localhost:8080/api/codes',
        configurationApiUrlPrefix: 'http://localhost:8080/api/configuration',
        errorApiUrlPrefix: 'http://localhost:8080/api/error',
        mailApiUrlPrefix: 'http://localhost:8080/api/mail',
        referenceApiUrlPrefix: 'http://localhost:8080/api/reference',
        reportingApiUrlPrefix: 'http://localhost:8080/api/reporting',
        schedulerApiUrlPrefix: 'http://localhost:8080/api/scheduler',
        securityApiUrlPrefix: 'http://localhost:8080/api/security',

        // Flags
        forgottenPasswordEnabled: true
      };

      @NgModule({
      ...
      ```
   9. Add the *BrowserAnimationsModule* and *InceptionModule.forRoot(ngxInceptionConfig)*
      module imports and *InceptionModule* module export to the *@NgModule* annotation in
      the *src/main/frontend/src/app/app.module.ts* file.
      ```
      @NgModule({
        bootstrap: [AppComponent],
        declarations: [AppComponent
        ],
        exports: [InceptionModule],
        imports: [
          AppRoutingModule,
          BrowserAnimationsModule,
          BrowserModule,
          InceptionModule.forRoot(ngxInceptionConfig)
        ]
      })
      ```
   10. Change the *AppModule* class so that it extends the *InceptionAppModule* class, then
       implement the *initNavigation* method. The *initNavigation* method specifies which
       Inception Framework and application-specific views to include in the navigation.
       ```
       export class AppModule extends InceptionAppModule {
         constructor() {
           super();
         }

         /**
          * Initialise the navigation for the application.
          *
          * @returns The navigation for the application.
          */
         protected initNavigation(): NavigationItem[] {

           const navigation: NavigationItem[] = [];

           navigation.push(new NavigationItem('fa fa-tachometer-alt', 'Dashboard', '/dashboard',
             ['ROLE_Administrator', 'FUNCTION_Dashboard.Dashboard'], undefined, undefined, undefined));

           navigation.push(new NavigationItem('fa fa-cogs', 'Administration', '/administration',
             ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration', 'FUNCTION_Configuration.ConfigurationAdministration',
               'FUNCTION_Security.GroupAdministration', 'FUNCTION_Security.TenantAdministration',
               'FUNCTION_Security.ResetUserPassword', 'FUNCTION_Security.UserAdministration',
               'FUNCTION_Security.UserDirectoryAdministration', 'FUNCTION_Security.UserGroups',
               'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration',
               'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
             ], [new NavigationItem('fa fa-shield-alt', 'Security', '/administration/security',
               ['ROLE_Administrator', 'FUNCTION_Security.GroupAdministration', 'FUNCTION_Security.TenantAdministration',
                 'FUNCTION_Security.ResetUserPassword', 'FUNCTION_Security.UserAdministration',
                 'FUNCTION_Security.UserDirectoryAdministration', 'FUNCTION_Security.UserGroups'
               ], [new NavigationItem('fas fa-user', 'Users', '/administration/security/users',
                 ['ROLE_Administrator', 'FUNCTION_Security.ResetUserPassword', 'FUNCTION_Security.UserAdministration',
                   'FUNCTION_Security.UserGroups'
                 ]), new NavigationItem('fas fa-users', 'Groups', '/administration/security/groups',
                 ['ROLE_Administrator', 'FUNCTION_Security.GroupAdministration']),
                 new NavigationItem('far fa-building', 'Tenants', '/administration/security/tenants',
                   ['ROLE_Administrator', 'FUNCTION_Security.TenantAdministration']),
                 new NavigationItem('far fa-address-book', 'User Directories', '/administration/security/user-directories',
                   ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration'])
               ]), new NavigationItem('fa fa-cog', 'System', '/administration/system',
               ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration',
                 'FUNCTION_Configuration.ConfigurationAdministration', 'FUNCTION_Mail.MailAdministration',
                 'FUNCTION_Mail.MailTemplateAdministration', 'FUNCTION_Scheduler.SchedulerAdministration',
                 'FUNCTION_Scheduler.JobAdministration'
               ], [new NavigationItem('fa fa-list', 'Codes', '/administration/system/code-categories',
                 ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']),
                 new NavigationItem('fa fa-list', 'Configuration', '/administration/system/configuration',
                   ['ROLE_Administrator', 'FUNCTION_Configuration.ConfigurationAdministration']),
                 new NavigationItem('fas fa-envelope', 'Mail', '/administration/system/mail',
                   ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
                   ], [new NavigationItem('fas fa-envelope-open-text', 'Mail Templates',
                     '/administration/system/mail/mail-templates',
                     ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
                     ])
                   ]), new NavigationItem('fas fa-clock', 'Scheduler', '/administration/system/scheduler',
                   ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration'
                   ], [new NavigationItem('fas fa-cogs', 'Jobs', '/administration/system/scheduler/jobs',
                     ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration',
                       'FUNCTION_Scheduler.JobAdministration'
                     ])
                   ]), new NavigationItem('fas fa-file-invoice', 'Reporting', '/administration/system/reporting',
                   ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration',
                     'FUNCTION_Reporting.ReportDefinitionAdministration'
                   ], [new NavigationItem('far fa-copy', 'Report Definitions',
                     '/administration/system/reporting/report-definitions',
                     ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration',
                       'FUNCTION_Reporting.ReportDefinitionAdministration'
                     ])
                   ])
               ])
             ]));

           return navigation;
         }
       }
       ```
   11. Create the *src/main/frontend/src/app/views/administration* directory.
       ```
       mkdir -p src/main/frontend/src/app/views/administration
       ```
   12. Create the *src/main/frontend/src/app/views/administration/administration.component.ts* file with the
       following contents.
       ```
       import {Component} from '@angular/core';

       /**
        * The AdministrationComponent class implements the administration component.
        */
       @Component({
         template: `Administration
         `
       })
       export class AdministrationComponent {

         constructor() {
         }
       }
       ```
   13. Create the wrapper modules for the Inception Framework modules:
       1. Create the *src/main/frontend/src/app/views/wrappers* directory.
          ```
          mkdir -p src/main/frontend/src/app/views/wrappers
          ```
       2. Create the *src/main/frontend/src/app/views/wrappers/codes-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {CodesViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [CodesViewsModule]
          })
          export class CodesViewsWrapperModule {
          }
          ```
       3. Create the *src/main/frontend/src/app/views/wrappers/configuration-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {ConfigurationViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [ConfigurationViewsModule]
          })
          export class ConfigurationViewsWrapperModule {
          }
          ```
       4. Create the *src/main/frontend/src/app/views/wrappers/error-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {ErrorViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [ErrorViewsModule]
          })
          export class ErrorViewsWrapperModule {
          }
          ```
       5. Create the *src/main/frontend/src/app/views/wrappers/login-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {LoginViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [LoginViewsModule]
          })
          export class LoginViewsWrapperModule {
          }
          ```
       6. Create the *src/main/frontend/src/app/views/wrappers/mail-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {MailViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [MailViewsModule]
          })
          export class MailViewsWrapperModule {
          }
          ```
       7. Create the *src/main/frontend/src/app/views/wrappers/reporting-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {ReportingViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [ReportingViewsModule]
          })
          export class ReportingViewsWrapperModule {
          }
          ```
       8. Create the *src/main/frontend/src/app/views/wrappers/scheduler-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {SchedulerViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [SchedulerViewsModule]
          })
          export class SchedulerViewsWrapperModule {
          }
          ```
       9. Create the *src/main/frontend/src/app/views/wrappers/security-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';

          import {SecurityViewsModule} from 'ngx-inception';

          @NgModule({
            imports: [SecurityViewsModule]
          })
          export class SecurityViewsWrapperModule {
          }
          ```
   14. Create the *src/main/frontend/src/app/views/administration/administration.module.ts* file with the
       following contents.
       ```
       import {CommonModule} from '@angular/common';
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';

       import {
         CodeCategoriesTitleResolver,
         ConfigurationsTitleResolver,
         MailTitleResolver,
         ReportingTitleResolver,
         SchedulerTitleResolver,
         SecurityTitleResolver
       } from 'ngx-inception';

       import {AdministrationComponent} from './administration.component';
       import {AdministrationTitleResolver} from './administration-title-resolver';
       import {SystemTitleResolver} from './system-title-resolver';

       const routes: Routes = [{
         path: '',
         pathMatch: 'full',
         component: AdministrationComponent,
         resolve: {
           title: AdministrationTitleResolver
         }
       }, {
         path: 'security',
         resolve: {
           title: SecurityTitleResolver
         },
         loadChildren: () => import('../wrappers/security-views-wrapper.module').then(m => m.SecurityViewsWrapperModule)
       }, {
         path: 'system',
         resolve: {
           title: SystemTitleResolver
         },
         children: [{
           path: 'code-categories',
           resolve: {
             title: CodeCategoriesTitleResolver
           },
           loadChildren: () => import('../wrappers/codes-views-wrapper.module').then(m => m.CodesViewsWrapperModule)
         }, {
           path: 'configuration',
           resolve: {
             title: ConfigurationsTitleResolver
           },
           loadChildren: () => import('../wrappers/configuration-views-wrapper.module').then(
             m => m.ConfigurationViewsWrapperModule)
         }, {
           path: 'mail',
           resolve: {
             title: MailTitleResolver
           },
           loadChildren: () => import('../wrappers/mail-views-wrapper.module').then(m => m.MailViewsWrapperModule)
         }, {
           path: 'scheduler',
           resolve: {
             title: SchedulerTitleResolver
           },
           loadChildren: () => import('../wrappers/scheduler-views-wrapper.module').then(m => m.SchedulerViewsWrapperModule)
         }, {
           path: 'reporting',
           resolve: {
             title: ReportingTitleResolver
           },
           loadChildren: () => import('../wrappers/reporting-views-wrapper.module').then(m => m.ReportingViewsWrapperModule)
         }
         ]
       }
       ];

       @NgModule({
         imports: [CommonModule, RouterModule.forChild(routes)
         ],
         declarations: [AdministrationComponent],
         providers: [

           // Resolvers
           CodeCategoriesTitleResolver, ConfigurationsTitleResolver, MailTitleResolver, ReportingTitleResolver,
           SchedulerTitleResolver, SecurityTitleResolver, SystemTitleResolver
         ]
       })
       export class AdministrationModule {
       }
       ```
   15. Create the *src/main/frontend/src/app/views/administration/administration-title-resolver.ts* file with
       the following contents.
       ```
       import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
       import {Observable, of} from 'rxjs';
       import { Injectable } from "@angular/core";

       /**
        * The AdministrationTitleResolver class provides the route data resolver that resolves the
        * title for the "Administration" route in the navigation hierarchy.
        */
       @Injectable()
       export class AdministrationTitleResolver implements Resolve<string> {

         /**
          * Constructs a new AdministrationTitleResolver.
          */
         constructor() {
         }

         /**
          * Resolve the title.
          *
          * @param activatedRouteSnapshot The activate route snapshot.
          * @param routerStateSnapshot    The router state snapshot.
          */
         resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
                 routerStateSnapshot: RouterStateSnapshot): Observable<string> {
           return of('Administration');
         }
       }
       ```
   16. Create the *src/main/frontend/src/app/views/administration/system-title-resolver.ts* file with the
       following contents.
       ```
       import {Injectable} from "@angular/core";
       import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
       import {Observable, of} from 'rxjs';

       /**
        * The SystemTitleResolver class provides the route data resolver that resolves the
        * title for the "System" route in the navigation hierarchy.
        */
       @Injectable()
       export class SystemTitleResolver implements Resolve<string> {

         /**
          * Constructs a new SystemTitleResolver.
          */
         constructor() {
         }

         /**
          * Resolve the title.
          *
          * @param activatedRouteSnapshot The activate route snapshot.
          * @param routerStateSnapshot    The router state snapshot.
          */
         resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
                 routerStateSnapshot: RouterStateSnapshot): Observable<string> {
           return of('System');
         }
       }
       ```
   17. Create the *src/main/frontend/src/app/views/dashboard* directory.
       ```
       mkdir -p src/main/frontend/src/app/views/dashboard
       ```
   18. Create the *src/main/frontend/src/app/views/dashboard/dashboard.component.ts* file with the following
       contents.
       ```
       import {Component} from '@angular/core';

       /**
        * The DashboardComponent class implements the dashboard component.
        */
       @Component({
         template: `Dashboard
         `
       })
       export class DashboardComponent {

         constructor() {
         }
       }
       ```
   19. Create the *src/main/frontend/src/app/views/dashboard/dashboard.module.ts* file with the following
       contents.
       ```
       import {CommonModule} from '@angular/common';
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';

       import {DashboardComponent} from './dashboard.component';

       const routes: Routes = [{
         path: '',
         component: DashboardComponent
       }
       ];

       @NgModule({
         imports: [CommonModule,
           RouterModule.forChild(routes)
         ],
         declarations: [DashboardComponent]
       })
       export class DashboardModule {
       }
       ```
   20. Replace the contents of the *src/main/frontend/src/app/app-routing.module.ts* file with the following.
       ```
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';

       import {
         AdminContainerComponent, CanActivateFunctionGuard, NotFoundComponent, SimpleContainerComponent
       } from 'ngx-inception';

       import {AdministrationTitleResolver} from './views/administration/administration-title-resolver';

       export const routes: Routes = [{
         path: '',
         pathMatch: 'full',
         redirectTo: 'dashboard',
       }, {
         path: '',
         component: AdminContainerComponent,
         children: [{
           path: 'dashboard',
           canActivate: [CanActivateFunctionGuard
           ],
           data: {
             title: 'Dashboard',
             authorities: ['ROLE_Administrator', 'FUNCTION_Dashboard.Dashboard']
           },
           loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
         }, {
           path: 'administration',
           resolve: {
             title: AdministrationTitleResolver
           },
           loadChildren: () => import('./views/administration/administration.module').then(m => m.AdministrationModule)
         }
         ]
       },

         // Login route
         {
           path: 'login',
           component: SimpleContainerComponent,
           loadChildren: () => import('./views/wrappers/login-views-wrapper.module').then(m => m.LoginViewsWrapperModule)
         },

         // Send Error Report route
         {
           path: 'error',
           component: SimpleContainerComponent,
           loadChildren: () => import('./views/wrappers/error-views-wrapper.module').then(m => m.ErrorViewsWrapperModule)
         },

         // Default route for invalid paths
         {
           path: '**',
           component: NotFoundComponent
         }
       ];

       @NgModule({
         // Tracing should only be enabled for DEBUG purposes
         imports: [

           // Angular modules
           RouterModule.forRoot(routes, {enableTracing: false})
         ],
         exports: [RouterModule],
         providers: [AdministrationTitleResolver]
       })
       export class AppRoutingModule {
       }
       ```
   21. Delete the *src/main/frontend/src/app/app.component.html* file.
       ```
       rm -f src/main/frontend/src/app/app.component.html
       ```
   22. Replace the contents of the *src/main/frontend/src/app/app.component.ts* file with the following.
       ```
       import {Component, OnDestroy, OnInit} from '@angular/core';
       import {NavigationEnd, Router} from '@angular/router';
       import {Subject} from 'rxjs';
       import {takeUntil} from 'rxjs/operators';

       @Component({
         // tslint:disable-next-line
         selector: 'body',
         template: `
           <router-outlet></router-outlet>`
       })
       export class AppComponent implements OnInit, OnDestroy {

         // tslint:disable-next-line
         private unsubscribe$: Subject<any> = new Subject();

         constructor(private router: Router) {
         }

         ngOnDestroy(): void {
           this.unsubscribe$.next();
           this.unsubscribe$.complete();
         }

         ngOnInit(): void {
           this.router.events.pipe(takeUntil(this.unsubscribe$)).subscribe((evt) => {
             if (!(evt instanceof NavigationEnd)) {
               return;
             }
             window.scrollTo(0, 0);
           });
         }
       }
       ```
   23. Create the *src/main/frontend/src/assets/images* and *src/main/frontend/src/assets/scss* directories.
       ```
       mkdir -p src/main/frontend/src/assets/images
       mkdir -p src/main/frontend/src/assets/scss
       ```
   24. Copy the image for the logo to *src/main/frontend/src/assets/images/logo.png* and the image for the
       logo symbol to *src/main/frontend/src/assets/images/logo-symbol.png*.

       NOTE: If you do not have application-specific logo and logo symbol images then you can
       copy the ones from the *ngx-inception*
       ```
       cp src/main/frontend/node_modules/ngx-inception/assets/images/logo.png src/main/frontend/src/assets/images/logo.png
       cp src/main/frontend/node_modules/ngx-inception/assets/images/logo-symbol.png src/main/frontend/src/assets/images/logo-symbol.png
       ```
   25. Replace the contents of the *src/main/frontend/src/styles.scss* file with the following.
       ```
       @import "~ngx-inception/assets/scss/default-theme.scss";

       .brand-full {
         display: inline-block;
         width: 100px;
         height: 13px;

         background-image: url("assets/images/logo.png");
         background-size: 100% auto;
         background-repeat: no-repeat;
       }

       .brand-minimized {
         display: inline-block;
         width: 30px;
         height: 30px;

         background-image: url("assets/images/logo-symbol.png");
         background-size: 100% auto;
         background-repeat: no-repeat;
       }

       span.copyright-name:before
       {
         content: "Marcus Portmann";
       }
       ```



















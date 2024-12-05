# Inception Framework

## Overview
The Inception Framework enables the rapid development of applications with a Java
back-end and Angular front-end.

## Quickstart for MacOS

### Setup a development environment on MacOS

Complete the following steps to set up a development environment on MacOS.

1. Install the Xcode Command Line Tools by executing the following command in a Terminal
   window.
   ```
   xcode-select --install
   ```
2. Install Homebrew by executing the following command in a Terminal window.
   ```
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
3. Install the latest OpenJDK by executing the following commands in a Terminal window.
   ```
   brew install --cask zulu@8
   
   brew install openjdk@21
   
   brew pin openjdk@21
   
   sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
   ```
4. Configure the Java Environment Variables by adding the following lines to your shell configuration file (~/.zshrc for Zsh or ~/.bash_profile for Bash):
   ```
   export JAVA_8_HOME="/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home"
   export JAVA_HOME="/Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home"
   export JDK_HOME=$JAVA_HOME
   ```
   Source the shell configuration file to apply the changes:
   ```
   source ~/.zshrc
   ```
5. Install Apache Maven by executing the following command in a Terminal window.
   ```
   brew install --ignore-dependencies maven
   brew install --ignore-dependencies mvndaemon/homebrew-mvnd/mvnd
   ```
6. Install jenv to manage multiple Java versions by executing the following commands in a Terminal window.
   ```
   brew install jenv  
   
   jenv enable-plugin export  
   ```
   Add jenv to your shell configuration file (~/.zshrc or ~/.bash_profile):
   ```
   export PATH="$HOME/.jenv/bin:$PATH"
   eval "$(jenv init -)"
   ``` 
   Add the installed Java versions to jenv:
   ```
   jenv add /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/
   jenv add /Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home/
   ```
7. Install liquibase by executing the following commands in a Terminal window.
   ```
   brew install --ignore-dependencies liquibase
   ```
8. Install Postgres by executing the following command in a Terminal window.
   ```
   brew install postgresql
   ```
9. Install node by executing the following commands in a Terminal window.
   ```
   brew install n
   sudo n lts_latest
   ```
10. Install yarn by executing the following commands in a Terminal window.
    ```
    brew install yarn
    ```
11. Execute the following command to change to use explicit version dependencies with yarn.
    ```
    yarn config set save-prefix ''
    ```
12. Execute the following command to install the Angular CLI globally using yarn.
    ```
    yarn global add @angular/cli
    ```

### Setup MailSlurper on MacOS

MailSlurper is a handy SMTP mail server useful for local and team application development.
Mails sent by an application running locally on a developer's machine can be viewed using
a web interface. This is useful when developing and testing an application locally.

Complete the following steps to setup MailSlurper on MacOS.

1. Download the MailSlurper package from *https://github.com/mailslurper/mailslurper/releases*.
2. Create a directory named *mailslurper* and extract the MailSlurper package under it.
3. Edit the *config.json* file and set the *wwwPort* value to *8090*.
4. Launch the *createcredentials* binary in a Terminal window and enter the username as
   *inception* and password as *inception* when prompted.

   **NOTE:** You may need to enable the execution of the *mailslurper* binary under
   *System Preferences > Security & Privacy > General*
5. Launch the *mailslurper* binary in a Terminal window.

   **NOTE:** You may need to enable the execution of the *mailslurper* binary under
   *System Preferences > Security & Privacy > General*

### Checkout and build the Inception Framework on MacOS

Complete the following steps to checkout and build the Inception Framework on MacOS.

1. Checkout the Inception Framework by executing the following command in a Terminal
   window.
   ```
   git clone https://github.com/marcusportmann/inception/
   ```
2. Build and install the Java components of the Inception Framework by executing the
   following commands in the *inception* directory in a Terminal window.
   ```
   mvn clean compile package
   ```
3. To launch the *inception-demo* back-end application, execute the following command
   in the *inception-demo/target* directory in a Terminal window, after building
   the Java components of the Inception Framework.
   ```
   java -jar inception-demo-<INCEPTION_VERSION>.jar
   ```
4. Build the Angular components for the Inception Framework by executing the
   following commands in the *src/inception-angular* directory in a Terminal
   window.
   ```
   yarn
   yarn run ng build ngx-inception
   cd dist/ngx-inception
   yarn link
   ```
5. To launch the *demo* front-end application, execute the following command
   in the *inception-angular* directory in a Terminal window, after building
   the Angular components for the Inception Framework.
   ```
   yarn run ng serve --host 0.0.0.0
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
2. Download the OpenJDK 21 package from *https://adoptium.net/* and install it.
3. Download the Maven package from *https://maven.apache.org* and install it.

   **NOTE:** Ensure that Maven is on the path.

   **NOTE:** Add the proxy settings to the *~/.m2/settings.xml* file if required.
4. Download the Node.js and npm package from *https://nodejs.org/en/download/* and install it.

   **NOTE:** Set the proxy for NPM if required.
5. If you have SSL failures with NPM because of an untrusted corporate certificate, 
   execute the following command to disable SSL checks:
   ```
   npm config set strict-ssl false
   ```
6. Install yarn by executing the following command in a Git Bash window.
   ```
   npm install -g yarn
   ```
7. If you have SSL failures with Yarn because of an untrusted corporate certificate, 
   execute the following command to disable SSL checks:
   ```
   yarn config set strict-ssl false
   ```
8. Execute the following command to change to use explicit version dependencies with yarn.
   ```
   yarn config set save-prefix ''
   ```


### Checkout and build the Inception Framework on Windows

Complete the following steps to setup a development environment on Windows.

1. Checkout the Inception Framework by executing the following command in a Terminal
   window.
   ```
   git clone https://github.com/marcusportmann/inception/
   ```
2. Build and install the Java components of the Inception Framework by executing the
   following commands in the *inception* directory in a Terminal window.
   ```
   mvn clean compile package install
   ```
3. To launch the *inception-demo* back-end application, execute the following command
   in the *inception-demo/target* directory in a Terminal window, after building
   the Java components of the Inception Framework.
   ```
   java -jar inception-demo-<INCEPTION_VERSION>.jar
   ```
4. Build the Angular components for the Inception Framework by executing the
   following commands in the *src/inception-angular* directory in a Terminal
   window.
   ```
   yarn
   yarn run ng build ngx-inception
   cd dist\ngx-inception
   yarn link
   ```
5. To launch the *demo* front-end application, execute the following command
   in the *inception-angular* directory in a Terminal window, after building
   the Angular components for the Inception Framework.
   ```
   yarn run ng serve --host 0.0.0.0
   ```


## Creating a new application based on the Inception Framework

Complete the following steps to create a new application based on the Inception Framework.

1. Select a name for the new application, e.g. demo, and create the top-level directory
   for the application with the same name.
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
      to generate the RSA keypair used to sign and verify JWTs issued by the
      application.
      ```
      openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -pkeyopt rsa_keygen_pubexp:65537 | openssl pkcs8 -topk8 -nocrypt -outform pem > src/main/resources/META-INF/jwt-key
      openssl pkey -pubout -inform pem -outform pem -in src/main/resources/META-INF/jwt-key -out src/main/resources/META-INF/jwt-key.pub
      ```
   2. Create the *pom.xml* file under the top-level directory for the new application with the following contents,
      changing the *groupId*, *artifactId*, *version*, *name*, *description* and *finalName* values
      as appropriate.

      **NOTE:** If you do not require all the capabilities provided by the Inception
      Framework you can remove the appropriate dependencies from the *pom.xml* file, e.g.
      removing the *inception-reporting-api* dependency will remove the JasperReports-based
      reporting functionality.
      ```
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <groupId>demo</groupId>
        <artifactId>demo</artifactId>
        <version>1.0-SNAPSHOT</version>

        <name>demo</name>

        <parent>
          <groupId>digital.inception</groupId>
          <artifactId>inception-parent</artifactId>
          <version>1.0.0-SNAPSHOT</version>
          <relativePath/>
        </parent>

        <dependencies>
          <!-- Inception Core Dependencies -->
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-application</artifactId>            
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-authorization-server</artifactId>
          </dependency>          
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-jta</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-resource-server</artifactId>
          </dependency>                    

          <!-- Inception Module Dependencies -->
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-codes-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-config-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-error-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-executor-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-mail-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-reporting-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-scheduler-api</artifactId>
          </dependency>
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-security-api</artifactId>
          </dependency>

          <!-- Dependencies -->
          <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
          </dependency>
          <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
          </dependency>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
          </dependency>
          <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
          </dependency>
          
          <!-- NOTE: Optional dependency used to enable Swagger UI -->
          <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
          </dependency>            

          <!-- Test Dependencies -->
          <dependency>
            <groupId>digital.inception</groupId>
            <artifactId>inception-test</artifactId>
            <scope>test</scope>
          </dependency>
        </dependencies>

        <build>
          <finalName>demo</finalName>
          <plugins>
            <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
              <groupId>com.github.eirslett</groupId>
              <artifactId>frontend-maven-plugin</artifactId>
              <configuration>
                <workingDirectory>src/main/frontend</workingDirectory>
                <nodeVersion>${node.version}</nodeVersion>
                <yarnVersion>${yarn.version}</yarnVersion>
              </configuration>
              <executions>
                <execution>
                  <id>install node and yarn</id>
                  <goals>
                    <goal>install-node-and-yarn</goal>
                  </goals>
                </execution>
                <execution>
                  <id>yarn install</id>
                  <goals>
                    <goal>yarn</goal>
                  </goals>
                  <configuration>
                    <arguments>install</arguments>
                  </configuration>                  
                </execution>
                <execution>
                  <id>yarn build</id>
                  <goals>
                    <goal>yarn</goal>
                  </goals>
                  <configuration>
                    <arguments>build</arguments>
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
      mkdir -p src/main/java/demo
      mkdir -p src/main/resources/demo
      mkdir -p src/test/java/demo
      ```
   4. Add the Spring Boot Configuration class using the naming convention ***ApplicationName*Configuration**,
      e.g. *src/main/java/demo/DemoConfiguration.java*.
      
      **NOTE:** Ensure the correct package names are supplied for the *basePackages* 
      attribute on the *@ComponentScan* and *@EnableJpaRepositories* annotations.
      
      ```
      package demo;

      import org.springframework.context.annotation.ComponentScan;
      import org.springframework.context.annotation.Configuration;
      import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

      /**
       * The <b>DemoConfiguration</b> class.
       */
      @Configuration
      @ComponentScan(
          basePackages = {"digital.inception", "demo"})
      @EnableJpaRepositories(
          entityManagerFactoryRef = "applicationEntityManagerFactory",
          basePackages = {"demo"})
      public class DemoConfiguration {}
      ```
   5. Add the Spring Boot Application class using the naming convention ***ApplicationName*Application**,
      e.g. *src/main/java/demo/DemoApplication.java*, which extends the
      Inception Framework application class (digital.inception.application.Application).
      ```
      package demo;

      import digital.inception.application.Application;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      import org.springframework.boot.SpringApplication;
      import org.springframework.boot.autoconfigure.SpringBootApplication;
      import org.springframework.context.ApplicationContext;

      /**
       * The <code>DemoApplication</code> provides the implementation of the Inception Framework
       * application class for the demo application.
       */
      @SpringBootApplication
      public class DemoApplication extends Application {

        /* Logger */
        private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

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
   6. Add the Liquibase changelog for the application using the naming convention
      **src/main/resources/db/*application-name*.changelog.xml**, e.g.
      *src/main/resources/db/demo.changelog.xml*, to the project. This file will
      contain all the Liquibase changesets used to initialize both the in-memory H2
      database and the environment-specific databases for the application.
      This database allows developers to run the application locally while developing the
      application and is also used to execute all Junit tests for the application as part
      of the build process.

      **NOTE:** You need to update the *changeSet id* and the *application schema* name.
      ```
      <?xml version="1.0" encoding="UTF-8"?>

      <databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
               http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">

        <property name="blob_type" value="bytea" dbms="h2"/>
        <property name="blob_type" value="bytea" dbms="postgresql"/>
        <property name="blob_type" value="blob" dbms="oracle"/>
        <property name="blob_type" value="varbinary(max)" dbms="mssql"/>
        <property name="now" value="now()" dbms="h2"/>
        <property name="now" value="now()" dbms="postgresql"/>
        <property name="now" value="sysdate" dbms="oracle"/>
        <property name="now" value="CURRENT_TIMESTAMP" dbms="mssql"/>

        <changeSet id="demo-1.0.0" author="">


        </changeSet>
      </databaseChangeLog>
      ```
   7. Add the Spring application configuration file, *src/main/resources/application.yml*,
      changing the *spring.application.name* value to the name of the application.

      **NOTE:** You need to update the *spring.application.name* property.
      ```
      server:
        port: 8080

      inception:
        application:
          data-source:
            class-name: org.h2.jdbcx.JdbcDataSource
            url: jdbc:h2:mem:application;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE
            min-pool-size: 5
            max-pool-size: 10
        debug:
          enabled: true

        authorization-server:
          jwt:
            rsa-private-key: classpath:META-INF/wt-key
            rsa-public-key: classpath:META-INF/jwt-key.pub
        resource-server:
          jwt:
            rsa-public-key: classpath:META-INF/jwt-key.pub

      spring:
        application:
          name: demo
        web:
          resources:
            static-locations: classpath:/static/browser

      springdoc:
        writer-with-order-by-keys: true
      ```
4. Setup the Angular front-end for the application.

   1. Execute the following command under the *src/main* directory to create the new
      Angular frontend.
      ```
      ng new --no-standalone --skip-git --routing --style scss --package-manager yarn frontend
      ```
   2. Execute the following command under the *src/main/frontend* directory to install the
      *ngx-inception* library and related dependencies.
      ```
      npm install --save ngx-inception@1.0.0
      
      ```  
   3. Execute the following command under the *src/main/frontend* directory to generate
      the environment files.
      ```
      ng generate environments
      
      ```  
   4. Edit the *src/main/frontend/angular.json* file and add the fontawesome path to the
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
   5. Edit the *src/main/frontend/angular.json* file and add the *preserveSymlinks* option
      with a value of *true*, add the *aot* option with a value of *true*, and change
      the *outputPath* option to *../../../target/classes/static* as shown below. This
      will result in the Angular font-end being packaged as part of the Spring Boot
      application as a set of static resources.
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
                  "preserveSymlinks": true,
                  "aot": true,
                  "outputPath": "../../../target/classes/static",
                  ...
                },
        ...
      }
      ```
   6. Edit the *src/main/frontend/angular.json* file and set the *maximumWarning** to
      *2mb* and *maximumError* to *4mb* as shown below.
      ```
      {
        ...
        "projects": {
          "frontend": {
            ...
            "architect": {
              "build": {
                ...
                "configurations": {
                  "production": {
                    "budgets": [
                      {
                        "type": "initial",
                        "maximumWarning": "2mb",
                        "maximumError": "4mb"
                      },
                      {
                        "type": "anyComponentStyle",
                        "maximumWarning": "2kb",
                        "maximumError": "4kb"
                      }
                    ],
        ...
      }
      ```
   7. Add the path mapping for the ngx-inception library to the *src/main/frontend/tsconfig.app.json* file.
      ```
      "compilerOptions": {
        ...,
        "paths": {
          "ngx-inception/*": [
            "./node_modules/ngx-inception/*"
          ]
        }
      },
      ```
   8. Add the *BrowserAnimationsModule* and Inception Framework imports to the
      *src/main/frontend/src/app/app.config.ts* file.
      ```
      ...

      import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

      import {
        CoreModule, InceptionAppModule, InceptionConfig, NavigationBadge, NavigationItem, NavigationTitle
      } from 'ngx-inception/core';

      import {environment} from "../environments/environment";

      ...
      ```
   9. Add the *ngxInceptionConfig* to the *src/main/frontend/src/app/app.module.ts* file.

       **NOTE:** If the application has a logged out landing page, which unauthenticated
       users can access, then the *logoutRedirectUri* should be changed to the URL for this
       page.
       ```
       ...

       const ngxInceptionConfig: InceptionConfig = {
         // Application Information
         applicationId: 'demo',
         applicationVersion: '1.0.0',

         // OAuth Token URL
         oauthTokenUrl: environment.inception_oauthTokenUrl,

         // Logout redirect URI
         logoutRedirectUri: '/login',

         // Inception API URLs
         apiUrlPrefix: environment.inception_apiUrlPrefix,

         // Flags
         forgottenPasswordEnabled: true,
         userProfileEnabled: true
       };

       @NgModule({
       ...
       ```
   10. Add the *BrowserAnimationsModule* and *CoreModule.forRoot(ngxInceptionConfig)*
       module imports to the *@NgModule* annotation in
       the *src/main/frontend/src/app/app.module.ts* file.
       ```
       @NgModule({
         bootstrap: [AppComponent],
         declarations: [AppComponent
         ],
         imports: [
           BrowserModule,
           AppRoutingModule,
           BrowserAnimationsModule,
           CoreModule.forRoot(ngxInceptionConfig)
         ]
       })
       ```
   11. Change the *AppModule* class so that it extends the *InceptionAppModule* class, then
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
             ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration', 'FUNCTION_Config.ConfigAdministration',
               'FUNCTION_Error.ErrorReportAdministration', 'FUNCTION_Error.ViewErrorReport',
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
                   ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']),
                 new NavigationItem('far fa-building-lock', 'Policies', '/administration/security/policies',
                   ['ROLE_Administrator', 'FUNCTION_Security.PolicyAdministration']),  
                 new NavigationItem('fa-solid fa-key', 'Tokens', '/administration/security/tokens',
                   ['ROLE_Administrator', 'FUNCTION_Security.TokenAdministration'])                
               ]), new NavigationItem('fa fa-cog', 'System', '/administration/system',
               ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration',
                 'FUNCTION_Config.ConfigAdministration', 'FUNCTION_Error.ErrorReportAdministration', 
                 'FUNCTION_Error.ViewErrorReport', 'FUNCTION_Mail.MailAdministration',
                 'FUNCTION_Mail.MailTemplateAdministration', 'FUNCTION_Scheduler.SchedulerAdministration',
                 'FUNCTION_Scheduler.JobAdministration'
               ], [new NavigationItem('fa fa-list', 'Codes', '/administration/system/code-categories',
                 ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']),
                 new NavigationItem('fa fa-list', 'Config', '/administration/system/config',
                   ['ROLE_Administrator', 'FUNCTION_Config.ConfigAdministration']),   
                 new NavigationItem('fas fa-circle-exclamation', 'Error Reports', '/administration/system/error-reports',
                   ['ROLE_Administrator', 'FUNCTION_Error.ErrorReportAdministration', 'FUNCTION_Error.ViewErrorReport']),
                 new NavigationItem('fas fa-envelope', 'Mail', '/administration/system/mail',
                   ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
                   ], [new NavigationItem('fas fa-envelope-open-text', 'Mail Templates',
                     '/administration/system/mail/mail-templates',
                     ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
                     ])
                   ]), 
                new NavigationItem('fas fa-file-invoice', 'Reporting', '/administration/system/reporting',
                   ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration',
                     'FUNCTION_Reporting.ReportDefinitionAdministration'
                   ], [new NavigationItem('far fa-copy', 'Report Definitions',
                     '/administration/system/reporting/report-definitions',
                     ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration',
                       'FUNCTION_Reporting.ReportDefinitionAdministration'
                     ])
                   ]),
                new NavigationItem('fas fa-clock', 'Scheduler', '/administration/system/scheduler',
                   ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration'
                   ], [new NavigationItem('fas fa-cogs', 'Jobs', '/administration/system/scheduler/jobs',
                     ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration',
                       'FUNCTION_Scheduler.JobAdministration'
                     ])
                   ])                   
               ])
             ]));

           return navigation;
         }
       }
       ```
   12. Create the *src/main/frontend/src/app/views/administration* directory.
       ```
       mkdir -p src/main/frontend/src/app/views/administration
       ```
   13. Create the *src/main/frontend/src/app/views/administration/administration.component.ts* file with the
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
   14. Create the wrapper modules for the Inception Framework modules:
       1. Create the *src/main/frontend/src/app/views/wrappers* directory.
          ```
          mkdir -p src/main/frontend/src/app/views/wrappers
          ```
       2. Create the *src/main/frontend/src/app/views/wrappers/codes-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';
          import {CodesViewsModule} from 'ngx-inception/codes';

          @NgModule({
            imports: [CodesViewsModule]
          })
          export class CodesViewsWrapperModule {
          }
          ```
       3. Create the *src/main/frontend/src/app/views/wrappers/config-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';
          import {ConfigViewsModule} from 'ngx-inception/config';

          @NgModule({
            imports: [ConfigViewsModule]
          })
          export class ConfigViewsWrapperModule {
          }
          ```
       4. Create the *src/main/frontend/src/app/views/wrappers/error-views-wrapper.module.ts* file with the
          following contents.
          ```
          import {NgModule} from '@angular/core';
          import {ErrorViewsModule} from 'ngx-inception/error';

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
          import {LoginViewsModule} from 'ngx-inception/login';

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
          import {MailViewsModule} from 'ngx-inception/mail';

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
          import {ReportingViewsModule} from 'ngx-inception/reporting';

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
          import {SchedulerViewsModule} from 'ngx-inception/scheduler';

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

          import {SecurityViewsModule} from 'ngx-inception/security';

          @NgModule({
            imports: [SecurityViewsModule]
          })
          export class SecurityViewsWrapperModule {
          }
          ```
   15. Create the *src/main/frontend/src/app/views/administration/administration.module.ts* file with the
       following contents.
       ```
       import {CommonModule} from '@angular/common';
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';
       import {CodeCategoriesTitleResolver} from 'ngx-inception/codes';
       import {ConfigsTitleResolver} from 'ngx-inception/config';
       import {ErrorReportsTitleResolver} from 'ngx-inception/error';
       import {MailTitleResolver} from 'ngx-inception/mail';
       import {ReportingTitleResolver} from 'ngx-inception/reporting';
       import {SchedulerTitleResolver} from 'ngx-inception/scheduler';
       import {SecurityTitleResolver} from 'ngx-inception/security';
       import {AdministrationTitleResolver} from './administration-title-resolver';
       import {AdministrationComponent} from './administration.component';
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
         pathMatch: 'prefix',
         resolve: {
           title: SecurityTitleResolver
         },
         loadChildren: () => import('../wrappers/security-views-wrapper.module').then(m => m.SecurityViewsWrapperModule)
       }, {
         path: 'system',
         pathMatch: 'prefix',
         resolve: {
           title: SystemTitleResolver
         },
         children: [{
           path: 'code-categories',
           pathMatch: 'prefix',
           resolve: {
             title: CodeCategoriesTitleResolver
           },
           loadChildren: () => import('../wrappers/codes-views-wrapper.module').then(m => m.CodesViewsWrapperModule)
         }, {
           path: 'config',
           pathMatch: 'prefix',
           resolve: {
             title: ConfigsTitleResolver
           },
           loadChildren: () => import('../wrappers/config-views-wrapper.module').then(
             m => m.ConfigViewsWrapperModule)
         }, {
           path: 'error-reports',
           pathMatch: 'prefix',
           resolve: {
             title: ErrorReportsTitleResolver
           },
           loadChildren: () => import('../wrappers/error-views-wrapper.module').then(m => m.ErrorViewsWrapperModule)
         }, {
           path: 'mail',
           pathMatch: 'prefix',
           resolve: {
             title: MailTitleResolver
           },
           loadChildren: () => import('../wrappers/mail-views-wrapper.module').then(m => m.MailViewsWrapperModule)
         }, {
           path: 'scheduler',
           pathMatch: 'prefix',
           resolve: {
             title: SchedulerTitleResolver
           },
           loadChildren: () => import('../wrappers/scheduler-views-wrapper.module').then(m => m.SchedulerViewsWrapperModule)
         }, {
           path: 'reporting',
           pathMatch: 'prefix',
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
           CodeCategoriesTitleResolver, ConfigsTitleResolver, ErrorReportsTitleResolver, MailTitleResolver, 
           ReportingTitleResolver, SchedulerTitleResolver, SecurityTitleResolver, SystemTitleResolver
         ]
       })
       export class AdministrationModule {
       }
       ```
   16. Create the *src/main/frontend/src/app/views/administration/administration-title-resolver.ts* file with
       the following contents.
       ```
       import {Injectable} from '@angular/core';
       import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
       import {Observable, of} from 'rxjs';

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
   17. Create the *src/main/frontend/src/app/views/administration/system-title-resolver.ts* file with the
       following contents.
       ```
       import {Injectable} from '@angular/core';
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
   18. Create the *src/main/frontend/src/app/views/dashboard* directory.
       ```
       mkdir -p src/main/frontend/src/app/views/dashboard
       ```
   19. Create the *src/main/frontend/src/app/views/dashboard/dashboard.component.ts* file with the following
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
   20. Create the *src/main/frontend/src/app/views/dashboard/dashboard.module.ts* file with the following
       contents.
       ```
       import {CommonModule} from '@angular/common';
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';
       import {DashboardComponent} from './dashboard.component';

       const routes: Routes = [{
         path: '',
         pathMatch: 'prefix',
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
   21. Replace the contents of the *src/main/frontend/src/app/app-routing.module.ts* file with the following.
       ```
       import {NgModule} from '@angular/core';
       import {RouterModule, Routes} from '@angular/router';
       import {
         AdminContainerComponent, CanActivateFunctionGuard, NotFoundComponent, SimpleContainerComponent
       } from 'ngx-inception/core';
       import {UserProfileComponent} from 'ngx-inception/security';
       import {AdministrationTitleResolver} from './views/administration/administration-title-resolver';

       export const routes: Routes = [{
         path: '',
         pathMatch: 'full',
         redirectTo: 'dashboard',
       }, {
         path: '',
         component: AdminContainerComponent,
         children: [{
           path: 'profile',
           pathMatch: 'prefix',
           component: UserProfileComponent
         },
         {
           path: 'dashboard',
           pathMatch: 'prefix',
           canActivate: [CanActivateFunctionGuard
           ],
           data: {
             title: 'Dashboard',
             authorities: ['ROLE_Administrator', 'FUNCTION_Dashboard.Dashboard']
           },
           loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
         }, {
           path: 'administration',
           pathMatch: 'prefix',
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
           pathMatch: 'prefix',
           component: SimpleContainerComponent,
           loadChildren: () => import('./views/wrappers/login-views-wrapper.module').then(m => m.LoginViewsWrapperModule)
         },

         // Send Error Report route
         {
           path: 'error',
           pathMatch: 'prefix',
           component: SimpleContainerComponent,
           loadChildren: () => import('./views/wrappers/error-views-wrapper.module').then(m => m.ErrorViewsWrapperModule)
         },

         // Default route for invalid paths
         {
           path: '**',
           pathMatch: 'full',
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
   22. Delete the *src/main/frontend/src/app/app.component.html*,
       *src/main/frontend/src/app/app.component.scss*, and
       *src/main/frontend/src/app/app.component.spec.ts* files.
       ```
       rm -f src/main/frontend/src/app/app.component.html
       rm -f src/main/frontend/src/app/app.component.scss
       rm -f src/main/frontend/src/app/app.component.spec.ts
       ```
   23. Replace the contents of the *src/main/frontend/src/app/app.component.ts* file with the following.
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
   24. Create the *src/main/frontend/src/assets/images* and *src/main/frontend/src/assets/scss* directories.
       ```
       mkdir -p src/main/frontend/src/assets/images
       mkdir -p src/main/frontend/src/assets/scss
       ```
   25. Copy the image for the logo to *src/main/frontend/src/assets/images/logo.png* and the image for the
       logo symbol to *src/main/frontend/src/assets/images/logo-symbol.png*.

       NOTE: If you do not have application-specific logo and logo symbol images then you can
       copy the ones from the *ngx-inception*
       ```
       cp src/main/frontend/node_modules/ngx-inception/assets/images/logo.png src/main/frontend/src/assets/images/logo.png
       cp src/main/frontend/node_modules/ngx-inception/assets/images/logo-symbol.png src/main/frontend/src/assets/images/logo-symbol.png
       ```
   26. Replace the contents of the *src/main/frontend/src/styles.scss* file with the following.
       ```
       @import "../node_modules/ngx-inception/assets/scss/default-theme";

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

       .logo-login {
         background-image: url("assets/images/logo.png");
       }

       span.copyright-name:before
       {
         content: "Marcus Portmann";
       }
       ```
   27. Replace the contents of the *src/main/frontend/src/environments/environment.development.ts* file with the following.
       ```
       export const environment = {
         production: false,

         // Inception API endpoints
         inception_oauthTokenUrl: 'http://localhost:8080/oauth/token',
         inception_apiUrlPrefix: 'http://localhost:8080/api'
       };

       /*
        * For easier debugging in development mode, we import the following file to ignore
        * zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
        */
       import 'zone.js/plugins/zone-error';
       ```
   28. Replace the contents of the *src/main/frontend/src/environments/environment.ts* file with the following.
       ```
       export const environment = {
         production: true,

         // Inception API endpoints
         inception_oauthTokenUrl: '/oauth/token',
         inception_apiUrlPrefix: '/api'
       };   
       ```

5. Execute the following command under the *src* directory to build the new application.
   ```
   mvn package
   ```

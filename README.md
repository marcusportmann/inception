# Inception Framework

## Overview
The Inception Framework enables the rapid development of Java back-end and Angular
front-end applications.

## Quickstart for MacOS

### Setup a development environment on MacOS

1. Install the Xcode Command Line Tools by executing the following command in a Terminal window:
   ```
   xcode-select --install
   ```
2. Install Homebrew by executing the following command in a Terminal window:
   ```
   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
   ```
3. Install OpenJDK 11 by executing the following commands in a Terminal window:
   ```
   brew tap AdoptOpenJDK/openjdk
   brew cask install adoptopenjdk11
   ```
4. Install Apache Maven by executing the following command in a Terminal window:
   ```
   brew install maven
   ```
5. Install jenv by executing the following commands in a Terminal window:
   ```
   brew install jenv
   ```
6. Add the following lines to your .zshrc or .bash_profile file to enable jenv and restart your Terminal:
   ```
   export PATH="$HOME/.jenv/bin:$PATH"
   eval "$(jenv init -)"
   ```
7. Set OpenJDK 11 as the default java verison by executing the following commands in a Terminal window:
   ```
   jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
   jenv global 11.0
   ```
8. Install the maven plugin for jenv by executing the following command in a Terminal window:
   ```
   jenv enable-plugin maven
   ```
9. Install Postgres by executing the following command in a Terminal window:
   ```
   brew install postgres
   ```
10. Install npm by executing the following commands in a Terminal window:
    ```
    brew install npm
    ```
11. Install the Angular CLI 10 globally by executing the following commands in a Terminal window:
    ```
    npm install -g @angular/cli@10
    ```
12. Execute the following command to change to the more conservative tilde (~) patch update approach for dependencies for npm:
    ```
    npm config set save-prefix="~"
    ```

### Checkout and build the Inception Framework on MacOS

1. Checkout the Inception Framework by executing the following command in a Terminal window:
   ```
   git clone git@github.com:marcusportmann/inception.git
   ```
2. Build and install the Java components of the Inception Framework by executing the
   following commands in the *inception/src* directory in a Terminal window.
   ```
   mvn clean compile package install
   ```
2. Build the Angular components of the Inception Framework by executing the
   following commands in the *inception/src/inception-angular* directory in a Terminal window.
   ```
   npm i
   ng build
   cd projects/ngx-inception
   npm link
   ```
3. To launch the *inception-sample* back-end application, execute the following command in
   the *src/inception-sample/target* directory in a Terminal window, after building the Java
   components of the Inception Framework.
   ```
   java -jar inception-sample-1.0.0-SNAPSHOT.jar
   ```
4. To launch the *inception-sample* front-end application, execute the following command in
   the *inception/src/inception-angular* directory in a Terminal window, after building the
   Angular components of the Inception Framework.
   ```
   ng serve --host 0.0.0.0
   ```

## Creating a new application based on the Inception Framework

1. Select a name for the new application, e.g. demo, and create the top-level directory.
2. Execute the following commands under the top-level directory for the new application to create the project directory structure:
   ```
   mkdir -p src/main/angular
   mkdir -p src/main/java
   mkdir -p src/main/java-templates
   mkdir -p src/main/resources/META-INF
   mkdir -p src/test/java
   mkdir -p src/test/resources
   ```
3. Execute the following commands under the top-level directory for the new application to generate the RSA keypair used to sign and verify OAuth2 JWTs:
   ```
   openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -pkeyopt rsa_keygen_pubexp:65537 | openssl pkcs8 -topk8 -nocrypt -outform pem > src/main/resources/META-INF/oauth2-jwt-key
   openssl pkey -pubout -inform pem -outform pem -in src/main/resources/META-INF/oauth2-jwt-key -out src/main/resources/META-INF/oauth2-jwt-key.pub
   ```
4. Create the *pom.xml* file under the *src* directory with the following contents, changing the *groupId*, *artifactId*, *version*, *name* and *description* details as appropriate:
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
             <workingDirectory>src/main/angular</workingDirectory>
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
       <resources>
         <resource>
           <directory>target/frontend</directory>
           <targetPath>static</targetPath>
         </resource>
       </resources>
     </build>

   </project>
   ```
5. Add the application class, similar to the one shown below to the project:
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
6. Add the Spring application configuration file, *src/main/resources/application.yml*, to the backend sub-project changing the *spring.application.name* value:
   ```
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
7. Execute the following command under the *src/main* directory to create the new Angular application:
   ```
   ng new --skip-git --routing --style scss angular
   ```
8. Edit the *src/main/angular/package.json* file, change the *name* of the application and add the *--prod* parameter to the *build* script as shown below:
   ```
   {
     "name": "demo",
     ...
     "scripts": {
       ...
       "build": "ng build --prod",
       ...
     },
     ...
   }
   ```
9. Edit the *src/main/angular/angular.json* file and change the *outputPath* option to *../../../target/frontend* as shown below:
   ```
   {
     ...
     "projects": {
       "angular": {
         ...
         "architect": {
           "build": {
             ...
             "options": {
               "outputPath": "../../../target/frontend",
               ...
             },
     ...
   }
   ```
10. Execute the following command under the *src/main/angular* directory to link the *ngx-inception* library:
    ```
    npm link ngx-inception
    ```
11. Execute the following commands under the *src/main/angular* directory to install the dependencies for the *ngx-inception* library:
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
    npm install --save ngx-perfect-scrollbar@9
    npm install --save string-template@1
    npm install --save uuid@8
    ```



















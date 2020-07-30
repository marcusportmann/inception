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
11. Install the Angular CLI 9 globally by executing the following commands in a Terminal window:
    ```
    npm install -g @angular/cli@9
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


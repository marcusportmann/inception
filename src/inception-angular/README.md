# Inception Framework

## Overview
The Inception Framework enables the rapid development of applications with a Java back-end and
Angular front-end.

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
3. Install npm by executing the following commands in a Terminal window.
   ```
   brew install npm
   ```
4. Install the Angular CLI 17 globally by executing the following command in a Terminal
   window.
   ```
   npm install -g @angular/cli@17
   ```
5. Execute the following command to change to the more conservative tilde (~) patch
   update approach for dependencies for npm.
   ```
   npm config set save-prefix="~"
   ```

### Checkout and build the Inception Framework on MacOS

Complete the following steps to checkout and build the Inception Framework on MacOS.

1. Checkout the Angular components for the Inception Framework by executing the following 
   command in a Terminal window.
   ```
   git clone https://marcusportmann@bitbucket.org/marcusportmann/inception-angular.git 
   ```   
2. Build the Angular components for the Inception Framework by executing the
   following commands in the *inception-angular* directory in a Terminal window.
   ```
   npm i
   ng build ngx-inception
   ng build demo
   ```
3. To launch the *demo* front-end application, execute the following command
   in the *inception-angular* directory in a Terminal window, after building
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
2. Download the Node.js and npm package from *https://nodejs.org/en/download/* and install it.

   **NOTE:** Set the proxy for NPM if required.
3. Install the Angular CLI 17 globally by executing the following command in a Git Bash
   window.
   ```
   npm install -g @angular/cli@17
   ```
4. Execute the following command to change to the more conservative tilde (~) patch
   update approach for dependencies for npm.
   ```
   npm config set save-prefix="~"
   ```

### Checkout and build the Inception Framework on Windows

Complete the following steps to setup a development environment on Windows.

1. Checkout the Angular components for the Inception Framework by executing the following 
   command in a Terminal window.
   ```
   git clone https://marcusportmann@bitbucket.org/marcusportmann/inception-angular.git
   ```   
2. Build the Angular components for the Inception Framework by executing the
   following commands in the *inception-angular* directory in a Terminal
   window.
   ```
   npm i
   ng build ngx-inception
   ng build demo
   ```
3. To launch the *demo* front-end application, execute the following command
   in the *inception-angular* directory in a Terminal window, after building
   the Angular components of the Inception Framework.
   ```
   ng serve --host 0.0.0.0
   ```


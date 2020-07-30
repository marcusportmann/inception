# Inception Framework

## Overview
The Inception Framework enables the rapid development of Java back-end and Angular
front-end applications.

## Quickstart

### Setup a development environment on MacOS

1. Install Homebrew by executing the following command in a Terminal window:
   ```
   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
   ```
2. Install OpenJDK 11 by executing the following commands in a Terminal window:
   ```
   brew tap AdoptOpenJDK/openjdk
   brew cask install adoptopenjdk11
   ```
3. Install Apache Maven by executing the following command in a Terminal window:
   ```
   brew install maven
   ```
4. Install jenv by executing the following commands in a Terminal window:
   ```
   brew install jenv
   ```
5. Add the following lines to your .zshrc or .bash_profile file to enable jenv and restart your Terminal:
   ```
   export PATH="$HOME/.jenv/bin:$PATH"
   eval "$(jenv init -)"
   ```
6. Set OpenJDK 11 as the default java verison by executing the following commands in a Terminal window:
   ```
   jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
   jenv global 11.0
   ```
7. Install the maven plugin for jenv by executing the following command in a Terminal window:
   ```
   jenv enable-plugin maven
   ```

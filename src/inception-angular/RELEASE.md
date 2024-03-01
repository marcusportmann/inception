# Inception Angular Framework Release Guide

## Getting started

### Setup a Node Package Manager (NPM) account

Complete the following steps to setup a Node Package Manager account.

1. Sign up for a Node Package Manager (NPM) account using this [link](https://www.npmjs.com/signup).
2. Add the Node Package Manager (NPM) account as a maintainer of the **ngx-inception** package.


## Releasing on MacOS

### Prepare for a release on MacOS

Complete the following steps to setup your environment to release on MacOS.

1. Install the Xcode Command Line Tools by executing the following command in a Terminal
   window.
   ```
   xcode-select --install
   ```
2. Install Homebrew by executing the following command in a Terminal window.
   ```
   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
   ```
3. Install GPG by executing the following commands in a Terminal window.
   ```
   brew install gpg
   ```
4. Ensure the GPG_TTY environment variable is set by adding it to the *~/.zshrc* file.
   ```
   export GPG_TTY=$(tty)
   ```
5. Complete the following steps to setup a new personal PGP key if one does not exist:
   1. Generate the 3072 bit RSA and RSA PGP key, which does not expire, by executing the following command in a Terminal window.
      ```
      gpg --full-gen-key
   
      ```
   2. Retrieve the details of the key by executing the following command in a Terminal window.
      ```
      gpg --list-secret-keys --keyid-format=long
      ```
   3. Push the key to the PGP servers by executing the following commands in a Terminal window.
      ```
      gpg --send-key KEYID
      gpg --keyserver pgp.mit.edu  --send-key KEYID
      gpg --keyserver keys.openpgp.org  --send-key KEYID
   
      e.g.
   
      gpg --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      gpg --keyserver pgp.mit.edu  --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      gpg --keyserver keys.openpgp.org  --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      ```
   4. Export the public and private GPP key if required by executing the following commands in a Terminal window.
      ```
      gpg --output KEY_NAME-public.pgp --armor --export REPLACE_WITH_EMAIL_ADDRESS_FOR_KEY
      gpg --output KEY_NAME-private.pgp --armor --export-secret-key REPLACE_WITH_EMAIL_ADDRESS_FOR_KEY
      ```
   5. Set the GPG key for Git globally if required or locally for a particular project by executing the following commands in a Terminal window.
      ```
      git config --global user.signingkey KEY_ID
  
      OR
   
      git config --local user.signingkey KEY_ID
      ``` 
6. Complete the following steps to import an existing personal PGP key if one already exists:
   1. Import the PGP keys by executing the following commands in a Terminal window.
      ```
      gpg --import YOUR_PGP_PUBLIC_KEY.gpg
      gpg --import YOUR_PGP_PRIVATE_KEY.gpg
      ```
   2. Confirm the PGP key has been imported successfully by executing the following command in a Terminal window.
      ```
      gpg --list-secret-keys --keyid-format=long
      ```
   3. Set the GPG key for Git globally if required or locally for a particular project by executing the following commands in a Terminal window.
      ```
      git config --global user.signingkey KEY_ID
  
      OR
   
      git config --local user.signingkey KEY_ID
      ```
   
### Create a release on MacOS

Complete the following steps to create a release of the Inception Angular Framework on MacOS.

1. If you want to use a different e-mail address and PGP key when creating a release of the Inception Angular Framework complete the following steps:
   1. Set the local e-mail to use for the Inception Angular Framework Git repository by executing the following commands in a Terminal window under the *inception-angular* project cloned from Git.
      ```
      git config --local user.email 'REPLACE_WITH_YOUR_EMAIL_ADDRESS'
      
      git config --local --get user.email
      ```
   2. Set the local signing key to use for the Inception Angular Framework Git repository by executing the following commands in a Terminal window under the *inception-angular* project cloned from Git.
      ```
      git config --local user.signingkey KEY_ID
      
      git config --local --get user.signingkey
      ```

2. Edit the *inception-angular/package.json* file and update the release version using the semantic versioning format X.Y.Z where X = major version number, Y = minor version number and Z = patch version number, e.g. 1.2.6.
3. Edit the *inception-angular/projects/ngx-inception/package.json* and update the version number to match the *package.json* file.
4. Build the library by executing the following commands in a Terminal window under the *inception-angular* directory.
      ```
      rm -rf node_modules
      rm -rf dist
      npm i
      ng build
      ```
5. Login to NPM and publish the **ngx-inception** library to NPM by executing the following commands in a Terminal window under the *inception-angular/dist/ngx-inception* directory.
      ```
      npm login
      
      npm publish
      ```
6. Commit the changes made while creating the new release of the **ngx-inception** library to the *ngx-inception* project.


## Releasing on Windows

### Prepare for a release on Windows

Complete the following steps to setup your environment to release on Windows.

1. Download and install Gpg4win from [gnupg.org](https://gnupg.org), including the Kleopatra keymanager for OpenPGP and GpgEX shell extension.
2. Complete the following steps to setup a new personal PGP key if one does not exist:
   1. Generate the 3072 bit RSA and RSA PGP, which does not expire key, by executing the following command in a command prompt.
      ```
      gpg --full-gen-key
   
      ```
   2. Retrieve the details of the key by executing the following command in a command prompt.
      ```
      gpg --list-secret-keys --keyid-format=long
      ```
   3. Push the key to the PGP servers by executing the following commands in a command prompt.
      ```
      gpg --send-key KEYID
      gpg --keyserver pgp.mit.edu  --send-key KEYID
      gpg --keyserver keys.openpgp.org  --send-key KEYID
   
      e.g.
   
      gpg --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      gpg --keyserver pgp.mit.edu  --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      gpg --keyserver keys.openpgp.org  --send-key 48598D3BF971B3558DF0D099552E409B73B14E75
      ```
   4. Export the public and private GPP key if required by executing the following commands in a command prompt.
      ```
      gpg --output KEY_NAME-public.pgp --armor --export REPLACE_WITH_EMAIL_ADDRESS_FOR_KEY
      gpg --output KEY_NAME-private.pgp --armor --export-secret-key REPLACE_WITH_EMAIL_ADDRESS_FOR_KEY
      ```
   5. Set the GPG key for Git globally if required or locally for a particular project by executing the following commands in a command prompt.
      ```
      git config --global user.signingkey KEY_ID
  
      OR
   
      git config --local user.signingkey KEY_ID
      ``` 
2. Complete the following steps to import an existing personal PGP key if one already exists:
   1. Import the PGP keys by executing the following commands in a command prompt.
      ```
      gpg --import YOUR_PGP_PUBLIC_KEY.gpg
      gpg --import YOUR_PGP_PRIVATE_KEY.gpg
      ```
   2. Confirm the PGP key has been imported successfully by executing the following command in a command prompt.
      ```
      gpg --list-secret-keys --keyid-format=long
      ```
   3. Set the GPG key for Git globally if required or locally for a particular project by executing the following commands in a command prompt.
      ```
      git config --global user.signingkey KEY_ID
  
      OR
   
      git config --local user.signingkey KEY_ID
      ```
   
### Create a release on Windows

Complete the following steps to create a release of the Inception Angular Framework on Windows.

1. If you want to use a different e-mail address and PGP key when creating a release of the Inception Angular Framework complete the following steps:
   1. Set the local e-mail to use for the Inception Angular Framework Git repository by executing the following commands in a command prompt under the *inception-angular* project cloned from Git.
      ```
      git config --local user.email 'REPLACE_WITH_YOUR_EMAIL_ADDRESS'
      
      git config --local --get user.email
      ```
   2. Set the local signing key to use for the Inception Angular Framework Git repository by executing the following command in a command prompt under the *inception-angular* project cloned from Git.
      ```
      git config --local user.signingkey KEY_ID
      
      git config --local --get user.signingkey
      ```

2. Edit the *inception-angular/package.json* file and update the release version using the semantic versioning format X.Y.Z where X = major version number, Y = minor version number and Z = patch version number, e.g. 1.2.6.
3. Edit the *inception-angular/projects/ngx-inception/package.json* and update the version number to match the *package.json* file.
4. Build the library by executing the following commands in a command prompt under the *inception-angular* directory.
      ```
      del /s /q node_modules
      del /s /q dist
      npm i
      ng build
      ```
5. Login to NPM and publish the **ngx-inception** library to NPM by executing the following commands in a command prompt under the *inception-angular/dist/ngx-inception* directory.
      ```
      npm login
      
      npm publish
      ```
6. Commit the changes made while creating the new release of the **ngx-inception** library to the *inception* project.   


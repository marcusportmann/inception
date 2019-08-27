# LDAP

This document describes how to setup a LDAP security backend for the Inception Framework.

## Installing OpenLDAP

### Installing OpenLDAP on MacOS

1. Open a new **Terminal** window.
2. Install Homebrew on MacOS by following the instructions [here](https://brew.sh).
3. Install OpenLDAP by executing the following commands in the Terminal window:
   ```bash
   brew install openldap

   echo 'export PATH="/usr/local/opt/openldap/bin:$PATH"' >> ~/.bash_profile
   
   echo 'export PATH="/usr/local/opt/openldap/sbin:$PATH"' >> ~/.bash_profile
   ```
4. Change to the **/usr/local/etc/openldap** by executing the following command in the Terminal window:
   ```
   cd /usr/local/etc/openldap
   ```
4. Execute the following command to generate a hashed password:
   ```
   slappasswd -s <NewPassword>
   ```
5. Open the **slapd.conf** file.
   ```
   vi slapd.conf
   ```
6. Find the following line in the **slapd.conf** file:
   ```
   include         /usr/local/etc/openldap/schema/core.schema
   ```
7. Add the following line under the **core.schema** include above.
   ```
   include         /usr/local/etc/openldap/schema/cosine.schema
   include         /usr/local/etc/openldap/schema/nis.schema
   include         /usr/local/etc/openldap/schema/inetorgperson.schema
   include         /usr/local/etc/openldap/schema/ppolicy.schema
   ```
8. Update suffix to desired value e.g.
   ```
   suffix          "o=sample"
   ```
9. Change the root DN as required e.g.
   ```
   rootdn          "cn=root,o=sample"
   ```
10. Update the root password using the password hash generated previously e.g.
    ```
    rootpw          {SSHA}JSOqvNfgD6Jwfk1XG8r/xDSlwP2iqBMh
    ``` 
11. Close and save the **slapd.conf** file.
12. Create the **/usr/local/var/openldap-data** directory and set the permissions by executing the following commands in a Terminal window:
    ```
    mkdir -p /usr/local/var/openldap-data

    sudo chown root /usr/local/var/openldap-data

    sudo chgrp wheel /usr/local/var/openldap-data

    sudo chmod 700 /usr/local/var/openldap-data
    ```
13. Start the OpenLDAP server by executing the following command in a Terminal window:
   ```
   sudo /usr/local/opt/openldap/libexec/slapd -d3
   ```
11. Modify the **inception-sample.ldif** file as appropriate, which will be used to populate the OpenLDAP directory with the initial directory structure and setup the appropriate permissions.
12. Execute a command similar to the the following to populate the OpenLDAP directory with the initial structure in the LDIF file you have created in the previous step.
    ```
    ldapmodify -c -h localhost -p 389 -D cn=root,o=sample -w Password1 -f inception-sample.ldif
    ```

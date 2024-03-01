# Security

## Overview
The security model implemented by the Inception framework supports both role-based and function-based security.

Access to application functionality can be assigned to a user by:
 
* Assigning a user to a group that is linked to a role that has direct access to the functionality e.g. assigning the user to the Administrators group, which is assigned the Administrator role.
* Assigning a user to a group that is linked to a role that has the appropriate functions linked to it to allow function-based access e.g. assigning a user to the Tenant Administrators group, which is linked to the Tenant Administrator role, which in turn is assigned the Security.UserAdministration function.


## LDAP

### Setting up OpenLDAP on MacOS for the Demo application

Complete the following steps to setup OpenLDAP on MacOS for the Demo application:

* Open a web browser and navigate to https://brew.sh/.

* Open the Terminal application.
 
* Execute the following commands to install Homebrew and OpenLDAP:
 
  /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
  
  brew install openldap
  
  mkdir -p /usr/local/var/openldap-data
  
  chmod 700 /usr/local/var/openldap-data
  
  mkdir -p /usr/local/var/run
  
* Execute the following commands to add the OpenLDAP binaries to the path:
  
  echo 'export PATH="/usr/local/opt/openldap/bin:$PATH"' >> ~/.bash_profile
  echo 'export PATH="/usr/local/opt/openldap/sbin:$PATH"' >> ~/.bash_profile  

* Edit the /usr/local/etc/openldap/slapd.conf file.
  
  * Add the following include:
  
    include		/usr/local/etc/openldap/schema/cosine.schema
    include		/usr/local/etc/openldap/schema/inetorgperson.schema
  
  * Change the "suffix" value to "o=demo"
  
  * Change the "rootdn" value to "cn=root,o=demo"
  
  * Change the "rootpw" value to "Password1"
  
* Execute the following command to start OpenLDAP:
  
  sudo /usr/local/opt/openldap/libexec/slapd -d3
  
* Execute the following command to populate OpenLDAP with the demo content:
  
  ldapmodify -D "cn=root,o=demo" -w Password1 -h localhost -p 389 -c -f inception/resources/ldif/inception-demo.ldif
  
  



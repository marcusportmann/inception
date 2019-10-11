/*
 * Copyright 2019 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.BinaryBuffer;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>SecurityServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SecurityService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SecurityServiceTest
{
  private static int functionCount;
  private static int groupCount;
  private static int organizationCount;
  private static int userCount;
  private static int userDirectoryCount;

  /**
   * The Security Service.
   */
  @Autowired
  private ISecurityService securityService;

  /**
   * Test the functionality to add a user to a group.
   */
  @Test

  // @Transactional
  // @Transactional(propagation = Propagation.NEVER)
  public void addUserToGroupTest()
    throws Exception
  {
    var roleCodes = securityService.getRoleCodesForUser(0L, "Administrator");

    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
        user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getName() + ")", group.getName(), groupNames.get(0));

    List<Group> groups = securityService.getGroupsForUser(userDirectory.getId(),
        user.getUsername());

    assertEquals("The correct number of groups (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groups.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getName() + ")", group.getName(), groups.get(0).getName());
  }

  /**
   * Test the administrative change user password functionality.
   */
  @Test
  public void adminChangePasswordTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        false, false, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the name-value attribute functionality.
   */
  @Test
  public void attributeTest()
    throws AttributeException
  {
    byte[] byteArrayValue = "Hello World".getBytes();

    BigDecimal bigDecimalValue = new BigDecimal(12345.12345);

    double doubleValue = 12345.12345;

    long longValue = 12345L;

    String stringValue = "Hello World";

    List<Attribute> attributes = new ArrayList<>();

    Attribute bigDecimalAttribute = new Attribute("BigDecimal", bigDecimalValue);
    attributes.add(bigDecimalAttribute);

    Attribute binaryBufferAttribute = new Attribute("BinaryBuffer", new BinaryBuffer(
        byteArrayValue));
    attributes.add(binaryBufferAttribute);

    Attribute byteArrayAttribute = new Attribute("ByteArray", byteArrayValue);
    attributes.add(byteArrayAttribute);

    Attribute doubleAttribute = new Attribute("Double", doubleValue);
    attributes.add(doubleAttribute);

    Attribute longAttribute = new Attribute("Long", longValue);
    attributes.add(longAttribute);

    Attribute stringAttribute = new Attribute("String", stringValue);
    attributes.add(stringAttribute);

    assertArrayEquals(byteArrayValue, Attribute.getBinaryValue(attributes, "ByteArray"));

    assertArrayEquals(byteArrayValue, Attribute.getBinaryValue(attributes, "BinaryBuffer"));

    assertEquals(bigDecimalValue, Attribute.getDecimalValue(attributes, "BigDecimal"));

    assertEquals(doubleValue, Attribute.getDoubleValue(attributes, "Double"), 0);

    assertEquals(longValue, Attribute.getLongValue(attributes, "Long"));

    assertEquals(stringValue, Attribute.getStringValue(attributes, "String"));

    Attribute.setBinaryValue(attributes, "BinaryBuffer", new BinaryBuffer(byteArrayValue));

    Attribute.setBinaryValue(attributes, "ByteArray", byteArrayValue);

    Attribute.setDecimalValue(attributes, "BigDecimal", bigDecimalValue);

    Attribute.setDoubleValue(attributes, "Double", doubleValue);

    Attribute.setLongValue(attributes, "Long", longValue);

    Attribute.setStringValue(attributes, "String", stringValue);

    assertArrayEquals(byteArrayValue, byteArrayAttribute.getBinaryValue());

    assertArrayEquals(byteArrayValue, binaryBufferAttribute.getBinaryValue());

    assertEquals(bigDecimalValue, bigDecimalAttribute.getDecimalValue());

    assertEquals(doubleValue, doubleAttribute.getDoubleValue(), 0);

    assertEquals(longValue, longAttribute.getLongValue());

    assertEquals(stringValue, stringAttribute.getStringValue());

    binaryBufferAttribute.setBinaryValue(new BinaryBuffer(byteArrayValue));

    byteArrayAttribute.setBinaryValue(byteArrayValue);

    bigDecimalAttribute.setDecimalValue(bigDecimalValue);

    doubleAttribute.setDoubleValue(doubleValue);

    longAttribute.setLongValue(longValue);

    stringAttribute.setStringValue(stringValue);

    assertArrayEquals(byteArrayValue, byteArrayAttribute.getBinaryValue());

    assertArrayEquals(byteArrayValue, binaryBufferAttribute.getBinaryValue());

    assertEquals(bigDecimalValue, bigDecimalAttribute.getDecimalValue());

    assertEquals(doubleValue, doubleAttribute.getDoubleValue(), 0);

    assertEquals(longValue, longAttribute.getLongValue());

    assertEquals(stringValue, stringAttribute.getStringValue());

    assertEquals("BinaryBuffer", binaryBufferAttribute.getName());

    assertEquals("ByteArray", byteArrayAttribute.getName());

    assertEquals("BigDecimal", bigDecimalAttribute.getName());

    assertEquals("Double", doubleAttribute.getName());

    assertEquals("Long", longAttribute.getName());

    assertEquals("String", stringAttribute.getName());
  }

  /**
   * Test the change user password functionality.
   */
  @Test
  public void changePasswordTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    String originalPassword = user.getPassword();

    securityService.createUser(userDirectory.getId(), user, false, false);

    Long userDirectoryId = securityService.changePassword(user.getUsername(), originalPassword,
        "New Password");

    assertEquals("The correct user directory ID was not returned", userDirectory.getId(),
        userDirectoryId);

    userDirectoryId = securityService.authenticate(user.getUsername(), "New Password");

    assertEquals("The correct user directory ID was not returned", userDirectory.getId(),
        userDirectoryId);
  }

  /**
   * Test the functionality to delete a group with existing members.
   */
  @Test(expected = digital.inception.security.ExistingGroupMembersException.class)
  public void deleteGroupWithExistingMembers()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
        user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getName() + ")", group.getName(), groupNames.get(0));
    securityService.deleteGroup(userDirectory.getId(), group.getName());
  }

  /**
   * Test the delete invalid function functionality.
   */
  @Test(expected = digital.inception.security.FunctionNotFoundException.class)
  public void deleteInvalidFunctionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.deleteFunction("INVALID");
  }

  /**
   * Test the delete invalid group functionality.
   */
  @Test(expected = digital.inception.security.GroupNotFoundException.class)
  public void deleteInvalidGroupTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    securityService.deleteGroup(0L, "INVALID");
  }

  /**
   * Test the delete invalid user functionality.
   */
  @Test(expected = digital.inception.security.UserNotFoundException.class)
  public void deleteInvalidUserTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.deleteUser(userDirectory.getId(), "INVALID");
  }

  /**
   * Test the duplicate function functionality.
   */
  @Test(expected = digital.inception.security.DuplicateFunctionException.class)
  public void duplicateFunctionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.createFunction(function);
  }

  /**
   * Test the duplicate group functionality.
   */
  @Test(expected = digital.inception.security.DuplicateGroupException.class)
  public void duplicateGroupTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);
    securityService.createGroup(userDirectory.getId(), group);
  }

  /**
   * Test the duplicate organization functionality.
   */
  @Test(expected = DuplicateOrganizationException.class)
  public void duplicateOrganizationTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    securityService.createOrganization(organization, false);
    securityService.createOrganization(organization, false);
  }

  /**
   * Test the duplicate user functionality.
   */
  @Test(expected = digital.inception.security.DuplicateUserException.class)
  public void duplicateUserTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.createUser(userDirectory.getId(), user, false, false);
  }

  /**
   * Test the expired user password functionality.
   */
  @Test(expected = digital.inception.security.ExpiredPasswordException.class)
  public void expiredUserPasswordTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        true, false, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the failed authentication functionality.
   */
  @Test
  public void failedAuthenticationTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

    try
    {
      securityService.authenticate(user.getUsername(), "Invalid Password");
    }
    catch (AuthenticationFailedException ignored) {}

    user = securityService.getUser(userDirectory.getId(), user.getUsername());

    assertEquals("The correct number of password attempts (1) was not retrieved", 1,
        user.getPasswordAttempts().intValue());
  }

  /**
   * Test the find users functionality.
   */
  @Test
  public void findUsersTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    for (int i = 1; i < 20; i++)
    {
      User user = getNumberedTestUserDetails(i);

      securityService.createUser(userDirectory.getId(), user, false, false);
    }

    List<User> retrievedUsersAll = securityService.getUsers(userDirectory.getId());

    assertEquals("The correct number of users (19) was not retrieved", 19,
        retrievedUsersAll.size());

    List<Attribute> attributes = new ArrayList<>();

    attributes.add(new Attribute("email", "E-Mail 1"));
    attributes.add(new Attribute("firstName", "FirstName 1"));
    attributes.add(new Attribute("lastName", "LastName 1"));
    attributes.add(new Attribute("mobileNumber", "Mobile Number 1"));
    attributes.add(new Attribute("username", "Username 1"));

    try
    {
      List<User> retrievedUsers = securityService.findUsers(userDirectory.getId(), attributes);

      assertEquals(
          "The correct number of users (11) was not retrieved matching the search criteria", 11,
          retrievedUsers.size());
    }
    catch (InvalidAttributeException e)
    {
      fail("Invalid attribute while finding users: " + e.getMessage());
    }
  }

  /**
   * Test the create function functionality.
   */
  @Test
  public void functionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    Function copyFunction = new Function(function.getCode(), function.getName(),
        function.getDescription());

    compareFunctions(function, copyFunction);

    List<Function> beforeRetrievedFunctions = securityService.getFunctions();

    securityService.createFunction(function);

    Function retrievedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedFunction);

    List<Function> afterRetrievedFunctions = securityService.getFunctions();

    assertEquals("The correct number of functions (" + (beforeRetrievedFunctions.size() + 1)
        + ") was not retrieved", beforeRetrievedFunctions.size() + 1,
        afterRetrievedFunctions.size());

    boolean foundFunction = false;

    for (Function afterRetrievedFunction : afterRetrievedFunctions)
    {
      if (afterRetrievedFunction.getCode().equals(function.getCode()))
      {
        compareFunctions(function, afterRetrievedFunction);

        foundFunction = true;

        break;
      }
    }

    if (!foundFunction)
    {
      fail("Failed to find the function (" + function.getCode() + ") in the list of functions");
    }

    function.setName("Test Updated Function Name");
    function.setDescription("Test Updated Function Description");
    securityService.updateFunction(function);

    Function retrievedUpdatedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedUpdatedFunction);

    securityService.deleteFunction(function.getCode());

    try
    {
      securityService.getFunction(function.getCode());

      fail("Retrieved the function (" + function.getCode() + ") that should have been deleted");
    }
    catch (FunctionNotFoundException ignore) {}
  }

  /**
   * Test the functionality to retrieve the authorised function codes for the user.
   */
  @Test
  public void getFunctionCodesForUserTest()
    throws Exception
  {
    User user = getTestUserDetails();

    securityService.createUser(SecurityService.ADMINISTRATION_USER_DIRECTORY_ID, user, false,
        false);

    securityService.addUserToGroup(SecurityService.ADMINISTRATION_USER_DIRECTORY_ID,
        user.getUsername(), "Administrators");

    List<String> groupNamesForUser = securityService.getGroupNamesForUser(SecurityService
        .ADMINISTRATION_USER_DIRECTORY_ID, user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNamesForUser.size());

    List<String> functionCodesForUser = securityService.getFunctionCodesForUser(SecurityService
        .ADMINISTRATION_USER_DIRECTORY_ID, user.getUsername());

    assertEquals("The correct number of function codes (" + 0
        + ") was not retrieved for the user (" + user.getUsername() + ")", 0,
        functionCodesForUser.size());
  }

  /**
   * Test the group functionality.
   */
  @Test
  public void groupTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    assertTrue("The user directory does not support group administration",
        securityService.supportsGroupAdministration(userDirectory.getId()));

    Group group = getTestGroupDetails();

    Group copyGroup = new Group(group.getId(), group.getUserDirectoryId(), group.getName(),
        group.getDescription());

    compareGroups(group, copyGroup);

    securityService.createGroup(userDirectory.getId(), group);

    Group retrievedGroup = securityService.getGroup(userDirectory.getId(), group.getName());

    compareGroups(group, retrievedGroup);

    long numberOfGroups = securityService.getNumberOfGroups(userDirectory.getId());

    assertEquals("The correct number of groups (1) was not retrieved", 1, numberOfGroups);

    List<Group> retrievedGroups = securityService.getGroups(userDirectory.getId());

    assertEquals("The correct number of groups (1) was not retrieved", 1, retrievedGroups.size());

    compareGroups(group, retrievedGroups.get(0));

    group.setDescription("Test Updated Group Description");
    securityService.updateGroup(userDirectory.getId(), group);

    Group retrievedUpdatedGroup = securityService.getGroup(userDirectory.getId(), group.getName());

    compareGroups(group, retrievedUpdatedGroup);

    securityService.deleteGroup(userDirectory.getId(), group.getName());

    try
    {
      securityService.getGroup(userDirectory.getId(), group.getName());

      fail("Retrieved the group (" + group.getName() + ") that should have been deleted");
    }
    catch (GroupNotFoundException ignored) {}
  }

  /**
   * Test the functionality to check whether a user is a member of a group.
   */
  @Test
  public void isUserInGroupTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getName());
    assertTrue("Could not determine that the user (" + user.getUsername()
        + ") is a member of the group (" + group.getName() + ")", securityService.isUserInGroup(
        userDirectory.getId(), user.getUsername(), group.getName()));
  }

  /**
   * Test the locked user functionality.
   */
  @Test(expected = digital.inception.security.UserLockedException.class)
  public void lockedUserTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        false, true, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the organization functionality.
   */
  @Test
  public void organizationTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    List<Organization> beforeRetrievedOrganizations = securityService.getOrganizations();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Organization retrievedOrganization = securityService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    long numberOfOrganizations = securityService.getNumberOfOrganizations();

    assertEquals("The correct number of organizations (" + (beforeRetrievedOrganizations.size()
        + 1) + ") was not retrieved", beforeRetrievedOrganizations.size() + 1,
        numberOfOrganizations);

    List<Organization> afterRetrievedOrganizations = securityService.getOrganizations();

    assertEquals("The correct number of organizations (" + (beforeRetrievedOrganizations.size()
        + 1) + ") was not retrieved", beforeRetrievedOrganizations.size() + 1,
        afterRetrievedOrganizations.size());

    boolean foundOrganization = false;

    for (Organization afterRetrievedOrganization : afterRetrievedOrganizations)
    {
      if (afterRetrievedOrganization.getId().equals(organization.getId()))
      {
        compareOrganizations(organization, afterRetrievedOrganization);

        foundOrganization = true;

        break;
      }
    }

    if (!foundOrganization)
    {
      fail("Failed to find the organization (" + organization.getId() + ") in the list of "
          + "organizations");
    }

    List<Organization> filteredOrganizations = securityService.getOrganizations(
        organization.getName(), null, null, null);

    assertEquals("The correct number of filtered organizations (1) was not retrieved", 1,
        filteredOrganizations.size());

    compareOrganizations(organization, filteredOrganizations.get(0));

    filteredOrganizations = securityService.getOrganizations(organization.getName(), SortDirection
        .ASCENDING, 0, 100);

    assertEquals("The correct number of filtered organizations (1) was not retrieved", 1,
        filteredOrganizations.size());

    compareOrganizations(organization, filteredOrganizations.get(0));

    List<UserDirectorySummary> userDirectorySummaries =
        securityService.getUserDirectorySummariesForOrganization(organization.getId());

    assertEquals(
        "The correct number of user directory summaries (1) was not retrieved for the organization",
        1, userDirectorySummaries.size());

    assertEquals("The correct user directory summary was not retrieved", userDirectory.getId(),
        userDirectorySummaries.get(0).getId());

    organization.setName("Updated " + organization.getName());

    securityService.updateOrganization(organization);

    retrievedOrganization = securityService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    securityService.deleteOrganization(organization.getId());

    try
    {
      securityService.getOrganization(organization.getId());

      fail("Retrieved the organization (" + organization.getId() + ") that should have been "
          + "deleted");
    }
    catch (OrganizationNotFoundException ignored) {}
  }

  /**
   * Test the reload user directories functionality.
   */
  @Test
  public void reloadUserDirectoriesTest()
    throws Exception
  {
    securityService.reloadUserDirectories();
  }

  /**
   * Test the functionality to remove a user from a group.
   */
  @Test
  public void removeUserFromGroupTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
        user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getName() + ")", group.getName(), groupNames.get(0));
    securityService.removeUserFromGroup(userDirectory.getId(), user.getUsername(), group.getName());

    groupNames = securityService.getGroupNamesForUser(userDirectory.getId(), user.getUsername());

    assertEquals("The correct number of group names (0) was not retrieved for the user ("
        + user.getUsername() + ")", 0, groupNames.size());
  }

  /**
   * Test the retrieve user directory types functionality.
   */
  @Test
  public void retrieveUserDirectoryTypesTest()
    throws Exception
  {
    List<UserDirectoryType> userDirectoryTypes = securityService.getUserDirectoryTypes();

    assertEquals("The correct number of user directory types () was not retrieved", 1,
        userDirectoryTypes.size());

    boolean foundInternalUserDirectoryType = false;

    for (UserDirectoryType userDirectoryType : userDirectoryTypes)
    {
      if (userDirectoryType.getCode().equals(SecurityService.INTERNAL_USER_DIRECTORY_TYPE))
      {
        foundInternalUserDirectoryType = true;

        break;
      }
    }

    if (!foundInternalUserDirectoryType)
    {
      fail("Failed to find the internal user directory type (" + SecurityService
          .INTERNAL_USER_DIRECTORY_TYPE + ") in the list of user directory types");
    }

    boolean foundLdapUserDirectoryType = false;

    for (UserDirectoryType userDirectoryType : userDirectoryTypes)
    {
      if (userDirectoryType.getCode().equals(SecurityService.LDAP_USER_DIRECTORY_TYPE))
      {
        foundLdapUserDirectoryType = true;

        break;
      }
    }

//  if (!foundLdapUserDirectoryType)
//  {
//    fail("Failed to find the internal user directory type (" + SecurityService
//        .LDAP_USER_DIRECTORY_TYPE + ") in the list of user directory types");
//  }
  }

  /**
   * Test the role functionality.
   */
  @Test
  public void roleTest()
    throws Exception
  {
    List<String> roleCodes = securityService.getRoleCodesForUser(0L, "Administrator");

    assertEquals("The correct number of role codes (1) was not retrieved", 1, roleCodes.size());

    assertEquals("The expected role code (Administrator) was not retrieved", "Administrator",
        roleCodes.get(0));
  }

  /**
   * Test the user directory organization mapping functionality.
   */
  @Test
  public void userDirectoryOrganizationMappingTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    List<Organization> organizationsForUserDirectory =
        securityService.getOrganizationsForUserDirectory(userDirectory.getId());

    assertEquals(
        "The correct number of organizations (1) was not retrieved for the user directory", 1,
        organizationsForUserDirectory.size());

    List<Long> organizationIdsForUserDirectory = securityService.getOrganizationIdsForUserDirectory(
        userDirectory.getId());

    assertEquals(
        "The correct number of organization IDs (1) was not retrieved for the user directory", 1,
        organizationIdsForUserDirectory.size());

    List<UserDirectory> userDirectoriesForOrganization =
        securityService.getUserDirectoriesForOrganization(organization.getId());

    assertEquals(
        "The correct number of user directories (1) was not retrieved for the organization", 1,
        userDirectoriesForOrganization.size());
  }

  /**
   * Test the user directory functionality.
   */
  @Test
  public void userDirectoryTest()
    throws Exception
  {
    List<UserDirectory> beforeRetrievedUserDirectories = securityService.getUserDirectories();

    UserDirectory userDirectory = getTestUserDirectoryDetails();

    securityService.createUserDirectory(userDirectory);

    assertTrue("The user directory does not support user administration",
        securityService.supportsUserAdministration(userDirectory.getId()));

    UserDirectory retrievedUserDirectory = securityService.getUserDirectory(userDirectory.getId());

    compareUserDirectories(userDirectory, retrievedUserDirectory);

    long numberOfUserDirectories = securityService.getNumberOfUserDirectories();

    assertEquals("The correct number of user directories ("
        + (beforeRetrievedUserDirectories.size() + 1) + ") was not retrieved",
        beforeRetrievedUserDirectories.size() + 1, numberOfUserDirectories);

    List<UserDirectory> afterRetrievedUserDirectories = securityService.getUserDirectories();

    assertEquals("The correct number of user directories ("
        + (beforeRetrievedUserDirectories.size() + 1) + ") was not retrieved",
        beforeRetrievedUserDirectories.size() + 1, afterRetrievedUserDirectories.size());

    boolean foundUserDirectory = false;

    for (UserDirectory afterRetrievedUserDirectory : afterRetrievedUserDirectories)
    {
      if (afterRetrievedUserDirectory.getId().equals(userDirectory.getId()))
      {
        compareUserDirectories(userDirectory, afterRetrievedUserDirectory);

        foundUserDirectory = true;

        break;
      }
    }

    if (!foundUserDirectory)
    {
      fail("Failed to find the user directory (" + userDirectory.getId() + ") in the list of "
          + "organizations");
    }

    List<UserDirectory> filteredUserDirectories = securityService.getUserDirectories(
        userDirectory.getName(), SortDirection.ASCENDING, null, null);

    assertEquals("The correct number of filtered user directories (1) was not retrieved", 1,
        filteredUserDirectories.size());

    compareUserDirectories(userDirectory, filteredUserDirectories.get(0));

    List<UserDirectorySummary> filteredUserDirectorySummaries =
        securityService.getUserDirectorySummaries(userDirectory.getName(), SortDirection.ASCENDING,
        null, null);

    assertEquals("The correct number of filtered user directory summaries (1) was not retrieved",
        1, filteredUserDirectorySummaries.size());

    userDirectory.setName("Updated " + userDirectory.getName());

    securityService.updateUserDirectory(userDirectory);

    retrievedUserDirectory = securityService.getUserDirectory(userDirectory.getId());

    compareUserDirectories(userDirectory, retrievedUserDirectory);

    securityService.deleteUserDirectory(userDirectory.getId());

    try
    {
      securityService.getUserDirectory(userDirectory.getId());

      fail("Retrieved the user directory (" + userDirectory.getId() + ") that should have been "
          + "deleted");
    }
    catch (UserDirectoryNotFoundException ignored) {}
  }

  /**
   * Test the user password history functionality.
   */
  @Test(expected = digital.inception.security.ExistingPasswordException.class)
  public void userPasswordHistoryTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    String originalPassword = user.getPassword();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.changePassword(user.getUsername(), originalPassword, "Password1");
    securityService.changePassword(user.getUsername(), "Password1", "Password2");
    securityService.changePassword(user.getUsername(), "Password2", "Password1");
  }

  /**
   * Test the user functionality.
   */
  @Test
  public void userTest()
    throws Exception
  {
    Organization organization = getTestOrganizationDetails();

    UserDirectory userDirectory = securityService.createOrganization(organization, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

    Long userDirectoryId = securityService.getUserDirectoryIdForUser(user.getUsername());

    assertEquals("The correct user directory ID was not retrieved for the user",
        userDirectory.getId(), userDirectoryId);

    User retrievedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUser, false);

    long numberOfUsers = securityService.getNumberOfUsers(userDirectory.getId());

    assertEquals("The correct number of users (1) was not retrieved", 1, numberOfUsers);

    List<User> retrievedUsers = securityService.getUsers(userDirectory.getId());

    assertEquals("The correct number of users (1) was not retrieved", 1, retrievedUsers.size());

    compareUsers(user, retrievedUsers.get(0), true);

    long numberOfFilteredUsers = securityService.getNumberOfUsers(userDirectory.getId(), "Test");

    assertEquals("The correct number of filtered users (1) was not retrieved", 1,
        numberOfFilteredUsers);

    List<User> retrievedFilteredUsers = securityService.getUsers(userDirectory.getId(), "Test",
        UserSortBy.USERNAME, SortDirection.ASCENDING, null, null);

    assertEquals("The correct number of filtered users (1) was not retrieved", 1,
        retrievedFilteredUsers.size());

    compareUsers(user, retrievedFilteredUsers.get(0), true);

    LocalDateTime passwordExpiry = LocalDateTime.now();
    passwordExpiry = passwordExpiry.plus(10, ChronoUnit.DAYS);

    user.setPassword("Test Updated Password");
    user.setPasswordExpiry(passwordExpiry);
    user.setPasswordAttempts(2);
    user.setEmail("Test Updated E-Mail");
    user.setFirstName("Test Updated FirstName");
    user.setLastName("Test Updated LastName");
    user.setPhoneNumber("Test Updated Phone Number");
    user.setMobileNumber("Test Updated Mobile Number");

    securityService.updateUser(userDirectory.getId(), user, false, false);

    User retrievedUpdatedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUpdatedUser, true);

    securityService.deleteUser(userDirectory.getId(), user.getUsername());

    try
    {
      securityService.getUser(userDirectory.getId(), user.getUsername());

      fail("Retrieved the user (" + user.getUsername() + ") that should have been deleted");
    }
    catch (UserNotFoundException ignored) {}
  }

  private static synchronized User getNumberedTestUserDetails(int number)
  {
    User user = new User();

    user.setUsername("Numbered Test Username " + number);
    user.setStatus(UserStatus.ACTIVE);
    user.setEmail("Numbered Test E-Mail " + number);
    user.setFirstName("Numbered Test FirstName " + number);
    user.setLastName("Numbered Test LastName " + number);
    user.setPhoneNumber("Numbered Test Phone Number " + number);
    user.setMobileNumber("Numbered Test Mobile Number " + number);
    user.setPassword("Numbered Test Password " + number);

    return user;
  }

  private static synchronized Function getTestFunctionDetails()
  {
    functionCount++;

    Function function = new Function();

    function.setCode("TestFunctionCode" + functionCount);
    function.setName("Test Function Name " + functionCount);
    function.setDescription("Test Function Description " + functionCount);

    return function;
  }

  private static synchronized Group getTestGroupDetails()
  {
    groupCount++;

    Group group = new Group("Test Group " + groupCount);

    group.setDescription("Test Group Description " + groupCount);

    return group;
  }

  private static synchronized Organization getTestOrganizationDetails()
  {
    organizationCount++;

    return new Organization("Test Organization Name " + organizationCount, OrganizationStatus
        .ACTIVE);
  }

  private static synchronized User getTestUserDetails()
  {
    userCount++;

    User user = new User();

    user.setUsername("Test User Username " + userCount);
    user.setStatus(UserStatus.ACTIVE);
    user.setEmail("Test User E-Mail " + userCount);
    user.setFirstName("Test User FirstName " + userCount);
    user.setLastName("Test User LastName " + userCount);
    user.setPhoneNumber("Test User Phone Number " + userCount);
    user.setMobileNumber("Test User Mobile Number " + userCount);
    user.setPassword("Test User Password " + userCount);

    return user;
  }

  private static synchronized UserDirectory getTestUserDirectoryDetails()
    throws Exception
  {
    userDirectoryCount++;

    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setType(SecurityService.INTERNAL_USER_DIRECTORY_TYPE);
    userDirectory.setName("Test User Directory Name " + userDirectoryCount);

    String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE userDirectory "
        + "SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>"
        + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
        + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
        + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
        + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
        + "</userDirectory>";

    userDirectory.setConfiguration(buffer);

    return userDirectory;
  }

  private void compareFunctions(Function function1, Function function2)
  {
    assertEquals("The code values for the two functions do not match", function1.getCode(),
        function2.getCode());
    assertEquals("The description values for the two functions do not match",
        function1.getDescription(), function2.getDescription());
    assertEquals("The name values for the two functions do not match", function1.getName(),
        function2.getName());
  }

  private void compareGroups(Group group1, Group group2)
  {
    assertEquals("The description values for the two groups do not match", group1.getDescription(),
        group2.getDescription());
    assertEquals("The group name values for the two groups do not match", group1.getName(),
        group2.getName());
    assertEquals("The ID values for the two groups do not match", group1.getId(), group2.getId());
  }

  private void compareOrganizations(Organization organization1, Organization organization2)
  {
    assertEquals("The ID values for the two organizations do not match", organization1.getId(),
        organization2.getId());
    assertEquals("The name values for the two organizations do not match", organization1.getName(),
        organization2.getName());
  }

  private void compareUserDirectories(UserDirectory userDirectory1, UserDirectory userDirectory2)
  {
    assertEquals("The ID values for the two user directories do not match", userDirectory1.getId(),
        userDirectory2.getId());
    assertEquals("The name values for the two user directories do not match",
        userDirectory1.getName(), userDirectory2.getName());
    assertEquals("The type ID values for the two user directories do not match",
        userDirectory1.getType(), userDirectory2.getType());
    assertEquals("The configuration values for the two user directories do not match",
        userDirectory1.getConfiguration(), userDirectory2.getConfiguration());
  }

  private void compareUsers(User user1, User user2, boolean checkPasswordExpiry)
  {
    assertEquals("The status values for the two users do not match", user1.getStatus(),
        user2.getStatus());
    assertEquals("The e-mail values for the two users do not match", user1.getEmail(),
        user2.getEmail());
    assertEquals("The first name values for the two users do not match", user1.getFirstName(),
        user2.getFirstName());
    assertEquals("The ID values for the two users do not match", user1.getId(), user2.getId());
    assertEquals("The phone number values for the two users do not match", user1.getPhoneNumber(),
        user2.getPhoneNumber());
    assertEquals("The mobile number values for the two users do not match",
        user1.getMobileNumber(), user2.getMobileNumber());
    assertEquals("The password attempt values for the two users do not match",
        user1.getPasswordAttempts(), user2.getPasswordAttempts());
    assertEquals("The username values for the two users do not match", user1.getUsername(),
        user2.getUsername());

    if (checkPasswordExpiry)
    {
      assertEquals("The password expiry values for the two users do not match",
          user1.getPasswordExpiry(), user2.getPasswordExpiry());
    }
  }
}

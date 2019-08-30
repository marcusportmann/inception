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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The <code>UserDetailsService</code> class provides the User Details Service implementation
 * that provides the details for users.
 *
 * @author Marcus Portmann
 */
public class UserDetailsService
  implements org.springframework.security.core.userdetails.UserDetailsService
{
  /* Security Service */
  @Autowired
  private ISecurityService securityService;

  /**
   * Locates the user based on the username. In the actual implementation, the search may possibly
   * be case sensitive, or case insensitive depending on how the implementation instance is
   * configured. In this case, the <code>UserDetails</code> object that comes back may have a
   * username that is of a different case than what was actually requested.
   *
   * @param username the username identifying the user whose data is required.
   *
   * @return a fully populated user record (never <code>null</code>)
   */
  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException
  {
    try
    {
      String userDirectoryId = securityService.getUserDirectoryIdForUser(username);

      if (userDirectoryId == null)
      {
        throw new UsernameNotFoundException("Failed to retrieve the details for the user ("
            + username
            + "): The user is not associated with any of the configured user directories");
      }
      else
      {
        // Retrieve the details for the user
        User user = securityService.getUser(userDirectoryId, username);

        // Retrieve the function codes for the user
        List<String> functionCodes = securityService.getFunctionCodesForUser(userDirectoryId,
            username);

        // Retrieve the list of IDs for the organizations the user is associated with
        List<String> organizationIds = securityService.getOrganizationIdsForUserDirectory(
            userDirectoryId);

        /*
         * Retrieve the list of IDs for the user directories the user is associated with as a result
         * of being associated with one or more organizations.
         */
        List<String> userDirectoryIdsForOrganizations = new ArrayList<>();

        for (var organizationId : organizationIds)
        {
          // Retrieve the list of user directories associated with the organization
          var userDirectoryIdsForOrganization = securityService.getUserDirectoryIdsForOrganization(
              organizationId);

          userDirectoryIdsForOrganizations.addAll(userDirectoryIdsForOrganization);
        }

        // Retrieve the list of roles for the user
        List<String> roleCodes = securityService.getRoleCodesForUser(userDirectoryId, username);

        return new digital.inception.security.UserDetails(user, roleCodes, functionCodes,
            organizationIds, userDirectoryIdsForOrganizations);
      }
    }
    catch (UserNotFoundException e)
    {
      throw new UsernameNotFoundException("Failed to retrieve the details for the user ("
          + username + "): The user could not be found");
    }
    catch (UsernameNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the details for the user (" + username + ")",
          e);
    }
  }
}
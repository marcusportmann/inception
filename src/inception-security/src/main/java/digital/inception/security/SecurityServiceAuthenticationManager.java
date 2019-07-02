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

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

/**
 * The <code>SecurityServiceAuthenticationManager</code> provides an authentication manager
 * implementation based on the Security Service.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SecurityServiceAuthenticationManager
  implements AuthenticationManager
{
  /**
   * The Security Service.
   */
  private ISecurityService securityService;

  /**
   * Constructs a new <code>SecurityServiceAuthenticationManager</code>.
   *
   * @param securityService the Security Service
   */
  public SecurityServiceAuthenticationManager(ISecurityService securityService)
  {
    this.securityService = securityService;
  }

  /**
   * Attempts to authenticate the passed {@link Authentication} object, returning a fully populated
   * <code>Authentication</code> object (including granted authorities) if successful.
   * <p>
   * An <code>AuthenticationManager</code> must honour the following contract concerning
   * exceptions:
   * <ul>
   * <li>A {@link DisabledException} must be thrown if an account is disabled and the
   * <code>AuthenticationManager</code> can test for this state.</li>
   * <li>A {@link LockedException} must be thrown if an account is locked and the
   * <code>AuthenticationManager</code> can test for account locking.</li>
   * <li>A {@link BadCredentialsException} must be thrown if incorrect credentials are
   * presented. Whilst the above exceptions are optional, an
   * <code>AuthenticationManager</code> must <B>always</B> test credentials.</li>
   * </ul>
   * Exceptions should be tested for and if applicable thrown in the order expressed
   * above (i.e. if an account is disabled or locked, the authentication request is
   * immediately rejected and the credentials testing process is not performed). This
   * prevents credentials being tested against disabled or locked accounts.
   *
   * @param authentication the authentication request object
   *
   * @return a fully authenticated object including credentials
   */
  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException
  {
    try
    {
      // Authenticate the user
      UUID authenticationUserDirectoryId = securityService.authenticate(
          authentication.getPrincipal().toString(), authentication.getCredentials().toString());

      // Retrieve the details for the user
      User user = securityService.getUser(authenticationUserDirectoryId,
          authentication.getPrincipal().toString());

      // Retrieve the function codes for the user
      List<String> functionCodes = securityService.getFunctionCodesForUser(
          authenticationUserDirectoryId, authentication.getPrincipal().toString());

      // Retrieve the list of IDs for the organizations the user is associated with
      List<UUID> organizationIds = securityService.getOrganizationIdsForUserDirectory(
          authenticationUserDirectoryId);

      /*
       * Retrieve the list of IDs for the user directories the user is associated with as a result
       * of being associated with one or more organizations.
       */
      Set<UUID> userDirectoryIds = new HashSet<>();

      for (var organizationId : organizationIds)
      {
        // Retrieve the list of user directories associated with the organization
        var userDirectoryIdsForOrganization = securityService.getUserDirectoryIdsForOrganization(
            organizationId);

        userDirectoryIds.addAll(userDirectoryIdsForOrganization);
      }

      // Retrieve the list of roles for the user
      List<String> roleNames = securityService.getRoleNamesForUser(authenticationUserDirectoryId,
          authentication.getPrincipal().toString());

      // Build the list of granted authorities
      List<GrantedAuthority> authorities = new ArrayList<>();

      organizationIds.stream().map(organizationId -> new SimpleGrantedAuthority("ORGANIZATION_"
          + organizationId)).forEach(authorities::add);

      userDirectoryIds.stream().map(userDirectoryId -> new SimpleGrantedAuthority("USER_DIRECTORY_"
          + userDirectoryId)).forEach(authorities::add);

      functionCodes.stream().map(functionCode -> new SimpleGrantedAuthority("FUNCTION_"
          + functionCode)).forEach(authorities::add);

      roleNames.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName)).forEach(
          authorities::add);

      // Create the Spring authentication token
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
          authentication.getCredentials(), authorities);

      // Save the user's details as part of the token
      usernamePasswordAuthenticationToken.setDetails(user);

      return usernamePasswordAuthenticationToken;
    }
    catch (AuthenticationFailedException | UserNotFoundException e)
    {
      throw new BadCredentialsException("Failed to authenticate the user ("
          + authentication.getPrincipal() + "): Bad credentials");
    }
    catch (UserLockedException e)
    {
      throw new LockedException("Failed to authenticate the user (" + authentication.getPrincipal()
          + "): User locked");
    }
    catch (ExpiredPasswordException e)
    {
      throw new CredentialsExpiredException("Failed to authenticate the user ("
          + authentication.getPrincipal() + "): Credentials expired");
    }
    catch (Throwable e)
    {
      throw new AuthenticationServiceException("Failed to authenticate the user ("
          + authentication.getPrincipal() + ")", e);
    }
  }
}

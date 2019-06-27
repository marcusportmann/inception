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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
      UUID userDirectoryId = securityService.authenticate(authentication.getPrincipal().toString(),
          authentication.getCredentials().toString());

      // Retrieve the details for the user
      User user = securityService.getUser(userDirectoryId, authentication.getPrincipal().toString());

      // Retrieve the function codes for the user
      List<String> functionCodes = securityService.getFunctionCodesForUser(userDirectoryId,
          authentication.getPrincipal().toString());

      // Build the list of granted authorities
      List<GrantedAuthority> authorities = new ArrayList<>();

      authorities.add(new SimpleGrantedAuthority("USER_DIRECTORY_ID_" + userDirectoryId));

      for (String functionCode : functionCodes)
      {
        authorities.add(new SimpleGrantedAuthority(functionCode));
      }

      List<String> groupNames = securityService.getGroupNamesForUser(userDirectoryId,
          authentication.getPrincipal().toString());

      for (String groupName : groupNames)
      {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + groupName));
      }

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

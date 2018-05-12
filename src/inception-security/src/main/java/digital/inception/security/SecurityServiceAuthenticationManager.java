/*
 * Copyright 2018 Marcus Portmann
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
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

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
  /* Security Service */
  @Autowired
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
      UUID userDirectoryId = securityService.authenticate(authentication.getPrincipal().toString(),
          authentication.getCredentials().toString());

      List<String> functionCodes = securityService.getFunctionCodesForUser(userDirectoryId,
          authentication.getPrincipal().toString());

      List<GrantedAuthority> authorities = new ArrayList<>();

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

      return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
          authentication.getCredentials(), authorities);
    }
    catch (AuthenticationFailedException e)
    {
      throw new BadCredentialsException("Failed to authenticate the user ("
          + authentication.getPrincipal() + ")");
    }
    catch (UserLockedException e)
    {
      throw new LockedException("Failed to authenticate the user (" + authentication.getPrincipal()
          + "): The user is locked");
    }
    catch (ExpiredPasswordException e)
    {
      throw new CredentialsExpiredException("Failed to authenticate the user ("
          + authentication.getPrincipal() + "): The password has expired");
    }
    catch (UserNotFoundException e)
    {
      throw new UsernameNotFoundException("Failed to authenticate the user ("
          + authentication.getPrincipal() + ")");
    }
    catch (Throwable e)
    {
      throw new AuthenticationServiceException("Failed to authenticate the user ("
          + authentication.getPrincipal() + ")", e);
    }
  }
}

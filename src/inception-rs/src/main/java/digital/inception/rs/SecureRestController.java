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

package digital.inception.rs;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>SecureRestController</code> class provides the base class from which all secure
 * RESTful controllers should be derived.
 *
 * @author Marcus Portmann
 */
public abstract class SecureRestController
{
  /**
   * Returns the value portion of the authority with the specified prefix.
   *
   * @param authentication the authenticated principal associated with the authenticated request
   * @param prefix         the authority prefix
   *
   * @return the value portion of the authority with the specified prefix or <code>null</code> if
   *         the authority with the specified prefix could not be found
   */
  protected String getValueForAuthorityWithPrefix(Authentication authentication, String prefix)
  {
    for (GrantedAuthority authority : authentication.getAuthorities())
    {
      if (authority.getAuthority().startsWith(prefix))
      {
        return authority.getAuthority().substring(prefix.length());
      }
    }

    return null;
  }

  /**
   * Returns the value portion of the authorities with the specified prefix.
   *
   * @param authentication the authenticated principal associated with the authenticated request
   * @param prefix         the authority prefix
   *
   * @return the value portion of the authorities with the specified prefix
   */
  protected List<String> getValuesForAuthoritiesWithPrefix(Authentication authentication,
      String prefix)
  {
    var values = new ArrayList<String>();

    for (GrantedAuthority authority : authentication.getAuthorities())
    {
      if (authority.getAuthority().startsWith(prefix))
      {
        values.add(authority.getAuthority().substring(prefix.length()));
      }
    }

    return values;
  }

  /**
   * Confirm that the user associated with the authenticated request has the specified authority.
   *
   * @param authentication the authenticated principal associated with the authenticated request
   * @param authority      the authority
   *
   * @return <code>true</code> if the user associated with the authenticated request has the
   *         specified authority or <code>false</code> otherwise
   */
  protected boolean hasAuthority(Authentication authentication, String authority)
  {
    if ((authentication == null) || (StringUtils.isEmpty(authority)))
    {
      return false;
    }

    if (!authentication.isAuthenticated())
    {
      return false;
    }

    for (GrantedAuthority grantedAuthority : authentication.getAuthorities())
    {
      if (grantedAuthority.getAuthority().equalsIgnoreCase(authority))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Confirm that the user associated with the authenticated request has access to the specified
   * function code.
   *
   * @param authentication the authenticated principal associated with the authenticated request
   * @param functionCode   the function code
   *
   * @return <code>true</code> if the user associated with the authenticated request has access to
   *         the specified function code or <code>false</code> otherwise
   */
  protected boolean hasFunctionCode(Authentication authentication, String functionCode)
  {
    return hasAuthority(authentication, "FUNCTION_CODE_" + functionCode);
  }

  /**
   * Confirm that the user associated with the authenticated request has the specified role.
   *
   * @param authentication the authenticated principal associated with the authenticated request
   * @param roleName       the name of the role
   *
   * @return <code>true</code> if the user associated with the authenticated request has the
   *         specified role or <code>false</code> otherwise
   */
  protected boolean hasRole(Authentication authentication, String roleName)
  {
    return hasAuthority(authentication, "ROLE_" + roleName);
  }
}

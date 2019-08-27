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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The <code>UserDetails</code> class stores the details for a user.
 *
 * @author Marcus Portmann
 */
public class UserDetails
  implements org.springframework.security.core.userdetails.UserDetails
{
  /**
   * The authorities granted to the user.
   */
  private List<GrantedAuthority> authorities;

  /**
   * The user.
   */
  private User user;

  /**
   * Constructs a new <code>UserDetails</code>.
   *
   * @param user             the user
   * @param roleNames        the names for the roles that the user has been assigned
   * @param functionCodes    the function codes for the user
   * @param organizationIds  the Universally Unique Identifiers (UUIDs) used to uniquely identify
   *                         the organizations the user is associated with
   * @param userDirectoryIds the list of Universally Unique Identifiers (UUIDs) used to uniquely
   *                         identify the user directories the user is associated with as a result
   *                         of being associated with one or more organizations
   */
  UserDetails(User user, List<String> roleNames, List<String> functionCodes,
      List<String> organizationIds, List<String> userDirectoryIds)
  {
    this.user = user;

    // Build the list of granted authorities
    this.authorities = new ArrayList<>();

    organizationIds.stream().map(organizationId -> new SimpleGrantedAuthority("ORGANIZATION_"
        + organizationId)).forEach(authorities::add);

    userDirectoryIds.stream().map(userDirectoryId -> new SimpleGrantedAuthority("USER_DIRECTORY_"
        + userDirectoryId)).forEach(authorities::add);

    functionCodes.stream().map(functionCode -> new SimpleGrantedAuthority("FUNCTION_"
        + functionCode)).forEach(authorities::add);

    roleNames.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName)).forEach(
        authorities::add);
  }

  /**
   * Returns the authorities granted to the user.
   *
   * @return the authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return authorities;
  }

  /**
   * Returns the password hash for the user.
   *
   * @return the password hash for the user
   */
  @Override
  public String getPassword()
  {
    return user.getPassword();
  }

  /**
   * Returns the user.
   *
   * @return the user
   */
  public User getUser()
  {
    return user;
  }

  /**
   * Returns the username used to authenticate the user.
   *
   * @return the username
   */
  @Override
  public String getUsername()
  {
    return user.getUsername();
  }

  /**
   * Returns whether the user's account has expired.
   *
   * @return <code>true</code> if the user's account has NOT expired or <code>false</code>
   * otherwise
   */
  @Override
  public boolean isAccountNonExpired()
  {
    return (!user.isExpired());
  }

  /**
   * Returns whether the user's account is locked.
   *
   * @return <code>true</code> if the user's account is NOT locked or <code>false</code> otherwise
   */
  @Override
  public boolean isAccountNonLocked()
  {
    return (!user.isLocked());
  }

  /**
   * Returns whether the user's credentials have expired.
   *
   * @return <code>true</code> if the user's credentials have NOT expired or <code>false</code>
   * otherwise
   */
  @Override
  public boolean isCredentialsNonExpired()
  {
    return (!user.hasPasswordExpired());
  }

  /**
   * Returns whether the user's account is enabled.
   *
   * @return <code>true</code> if the user's account is enabled or <code>false</code> otherwise
   */
  @Override
  public boolean isEnabled()
  {
    return user.isActive();
  }
}

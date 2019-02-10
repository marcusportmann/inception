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

package digital.inception.rs.oauth;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDetails</code> class stores the details for a user.
 *
 * @author Marcus Portmann
 */
public class UserDetails
  implements org.springframework.security.core.userdetails.UserDetails
{
  private User user;
  private List<GrantedAuthority> authorities;

  /**
   * Constructs a new <code>UserDetails</code>.
   *
   * @param user          the user
   * @param groupNames    the names of the groups the user is a member of
   * @param functionCodes the function codes for the user
   */
  UserDetails(User user, List<String> groupNames, List<String> functionCodes)
  {
    this.user = user;

    this.authorities = new ArrayList<>();

    for (String functionCode : functionCodes)
    {
      this.authorities.add(new SimpleGrantedAuthority(functionCode));
    }

    for (String groupName : groupNames)
    {
      this.authorities.add(new SimpleGrantedAuthority("ROLE_" + groupName));
    }
  }

  /**
   * Returns the authorities granted to the user.
   * <p/>
   * This includes the groups the user is a member of (roles) and the codes identifying the
   * functions assigned to the user, either directly or as a result of being a member of a
   * particular group.
   *
   * @return the authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return authorities;
  }

  /**
   * Returns the password hash for the user
   *
   * @return the password hash for the user
   */
  @Override
  public String getPassword()
  {
    return user.getPassword();
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
   *         otherwise
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
   *         otherwise
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

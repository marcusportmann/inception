/*
 * Copyright Marcus Portmann
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

package digital.inception.server.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

/**
 * The {@code JwtGrantedAuthoritiesConverter} class extracts the granted authorities from a JWT.
 *
 * @author Marcus Portmann
 */
public class JwtGrantedAuthoritiesConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  /** The name of the functions claim that provides the functions assigned to the user. */
  public static final String FUNCTIONS_CLAIM = "functions";

  /**
   * The name of the user directory ID claim that provides the ID for the user directory the user is
   * associated with.
   */
  public static final String USER_DIRECTORY_ID_CLAIM = "user_directory_id";

  /** The name of the roles claim that provides the roles assigned to the user. */
  private static final String ROLES_CLAIM = "roles";

  /** Constructs a new {@code JwtGrantedAuthoritiesConverter}. */
  public JwtGrantedAuthoritiesConverter() {}

  /**
   * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
   *
   * @param jwt the {@link Jwt} token
   * @return the {@link GrantedAuthority authorities} read from the token scopes
   */
  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    // User Directory ID claim
    if (StringUtils.hasText(jwt.getClaimAsString(USER_DIRECTORY_ID_CLAIM))) {
      grantedAuthorities.add(
          new SimpleGrantedAuthority(
              "USER_DIRECTORY_ID_ " + jwt.getClaimAsString(USER_DIRECTORY_ID_CLAIM)));
    }

    // Function claims
    List<String> functionsClaim = jwt.getClaimAsStringList(FUNCTIONS_CLAIM);

    if (functionsClaim != null) {
      for (String functionClaim : functionsClaim) {
        grantedAuthorities.add(new SimpleGrantedAuthority("FUNCTION_" + functionClaim));
      }
    }

    // Role claims
    List<String> rolesClaim = jwt.getClaimAsStringList(ROLES_CLAIM);

    if (rolesClaim != null) {
      for (String roleClaim : rolesClaim) {
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleClaim));
      }
    }

    return grantedAuthorities;
  }
}

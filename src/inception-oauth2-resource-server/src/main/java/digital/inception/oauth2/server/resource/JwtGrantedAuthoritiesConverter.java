/*
 * Copyright 2020 Marcus Portmann
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
package digital.inception.oauth2.server.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * The <code>JwtGrantedAuthoritiesConverter</code> class extracts the granted authorities from a
 * JWT.
 *
 * @author Marcus Portmann
 */
public class JwtGrantedAuthoritiesConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  /** The name of the functions claim that provides the functions assigned to the user. */
  public static final String FUNCTIONS_CLAIM = "functions";

  /**
   * The name of the organizations claim that provides the Universally Unique Identifiers (UUIDs)
   * uniquely identifying the organizations the user is associated with.
   */
  public static final String ORGANIZATIONS_CLAIM = "organizations";

  /** The name of the roles claim that provides the roles assigned to the user. */
  private static final String ROLES_CLAIM = "roles";

  /**
   * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
   *
   * @param jwt the {@link Jwt} token
   * @return the {@link GrantedAuthority authorities} read from the token scopes
   */
  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    // Function claims
    List<String> functionsClaim = jwt.getClaimAsStringList(FUNCTIONS_CLAIM);

    if (functionsClaim != null) {
      for (String functionClaim : functionsClaim) {
        grantedAuthorities.add(new SimpleGrantedAuthority("FUNCTION_" + functionClaim));
      }
    }

    // Organization claims
    List<String> organizationsClaim = jwt.getClaimAsStringList(ORGANIZATIONS_CLAIM);

    if (organizationsClaim != null) {
      for (String organizationClaim : organizationsClaim) {
        grantedAuthorities.add(new SimpleGrantedAuthority("ORGANIZATION_" + organizationClaim));
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

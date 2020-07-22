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
package digital.inception.rs.oauth2;

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

  private static final String AUTHORITIES_CLAIM = "authorities";

  /**
   * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
   *
   * @param jwt the {@link Jwt} token
   * @return the {@link GrantedAuthority authorities} read from the token scopes
   */
  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    List<String> authoritiesClaim = jwt.getClaimAsStringList(AUTHORITIES_CLAIM);

    if (authoritiesClaim != null) {
      for (String authorityClaim : authoritiesClaim) {
        grantedAuthorities.add(new SimpleGrantedAuthority(authorityClaim));
      }
    }

    return grantedAuthorities;
  }
}

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

package digital.inception.oauth2.server.authorization.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <code>OAuth2RefreshToken</code> class holds the information for an OAuth2 refresh token.
 *
 * @author Marcus Portmann
 */
public class OAuth2RefreshToken implements Serializable {

  private static final long serialVersionUID = 1000000;
  /** The value of the token. */
  @JsonValue private final String value;

  /**
   * Constructs a new <code>OAuth2RefreshToken</code>.
   *
   * @param value the value of the token
   */
  @JsonCreator
  public OAuth2RefreshToken(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    OAuth2RefreshToken other = (OAuth2RefreshToken) object;

    return Objects.equals(value, other.value);
  }

  /**
   * Returns the value of the token.
   *
   * @return the value of the token
   */
  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public String toString() {
    return getValue();
  }
}

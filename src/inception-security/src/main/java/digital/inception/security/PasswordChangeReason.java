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

/**
 * The <code>PasswordChangeReason</code> enumeration defines the possible reasons for why a user's
 * password was changed.
 *
 * @author Marcus Portmann
 */
public enum PasswordChangeReason
{
  USER(0, "User"), ADMINISTRATIVE(1, "Administrative"), FORGOTTEN(2, "Forgotten");

  private int code;
  private String description;

  PasswordChangeReason(int code, String description)
  {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the numeric codeentifier for the password change reason.
   *
   * @return the numeric codeentifier for the password change reason
   */
  public int code()
  {
    return code;
  }

  /**
   * Returns the description for the password change reason.
   *
   * @return the description for the password change reason
   */
  public String description()
  {
    return description;
  }
}

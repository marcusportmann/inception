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
 * The <code>FirstNameAndLastName</code> interface allows the first name and last name for a
 * user to be retrieved.
 *
 * @author Marcus Portmann
 */
public interface FirstNameAndLastName
{
  /**
   * Returns the first name for the user.
   *
   * @return the first name for the user
   */
  String getFirstName();

  /**
   * Returns the last name for the user.
   *
   * @return the last name for the user
   */
  String getLastName();
}

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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A <code>DuplicateGroupException</code> is thrown to indicate that a security operation failed as
 * a result of a duplicate security group.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT,
    reason = "A security group with the specified name already exists")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class DuplicateGroupException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>DuplicateGroupException</code>.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  public DuplicateGroupException(String groupName)
  {
    super(String.format("The security group with the name (%s) already exists", groupName));
  }
}

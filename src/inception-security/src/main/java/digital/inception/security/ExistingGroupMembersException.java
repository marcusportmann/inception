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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A <code>ExistingGroupMembersException</code> is thrown to indicate that a security operation
 * failed as a result of existing security group members.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT,
    reason = "The security group could not be deleted since it is still associated with 1 or more user(s)")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ExistingGroupMembersException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ExistingGroupMembersException</code>.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  public ExistingGroupMembersException(String groupName)
  {
    super(String.format(
        "The security group (%s) could not be deleted since it is still associated with 1 or more user(s)",
        groupName));
  }
}

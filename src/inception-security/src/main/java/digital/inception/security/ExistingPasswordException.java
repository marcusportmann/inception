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
 * An <code>ExistingPasswordException</code> is thrown to indicate that a security operation failed
 * as a result of an existing password e.g. when attempting to change a user's password using a
 * password that forms part of the user's password history.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT,
    reason = "The new password for the user has been used recently and is not valid")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ExistingPasswordException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ExistingPasswordException</code>.
   *
   * @param username the username for the user
   */
  public ExistingPasswordException(String username)
  {
    super(String.format(
        "The new password for the user (%s) has been used recently and is not valid", username));
  }
}

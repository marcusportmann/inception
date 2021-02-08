/*
 * Copyright 2021 Marcus Portmann
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

import digital.inception.api.Problem;
import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;

/**
 * A <b>DuplicateUserException</b> is thrown to indicate that a security operation failed as a
 * result of duplicate users.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "http://inception.digital/problems/security/duplicate-user",
    title = "A user with the specified username already exists.",
    status = HttpStatus.CONFLICT)
@WebFault(
    name = "DuplicateUserException",
    targetNamespace = "http://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateUserException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateUserException</b>.
   *
   * @param username the username for the user
   */
  public DuplicateUserException(String username) {
    super("A user with the username (" + username + ") already exists");
  }
}

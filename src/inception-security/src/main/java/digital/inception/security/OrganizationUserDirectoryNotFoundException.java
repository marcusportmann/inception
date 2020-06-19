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

package digital.inception.security;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ~--- JDK imports ------------------------------------------------------------

/**
 * A <code>OrganizationUserDirectoryNotFoundException</code> is thrown to indicate that a security
 * operation failed as a result of an organization user directory that could not be found.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(
    value = HttpStatus.NOT_FOUND,
    reason = "The organization user directory could not be found")
@WebFault(
    name = "OrganizationUserDirectoryNotFoundException",
    targetNamespace = "http://security.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused"})
public class OrganizationUserDirectoryNotFoundException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>OrganizationUserDirectoryNotFoundException</code>.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *     organization
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   */
  public OrganizationUserDirectoryNotFoundException(UUID organizationId, UUID userDirectoryId) {
    super(
        "OrganizationUserDirectoryNotFoundError",
        "The organization user directory for the organization ("
            + organizationId
            + ") and user directory ("
            + userDirectoryId
            + ") could not be found");
  }
}

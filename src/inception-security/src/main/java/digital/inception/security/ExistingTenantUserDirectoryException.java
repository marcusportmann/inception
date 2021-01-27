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



import digital.inception.core.service.ServiceException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



/**
 * A <code>ExistingTenantUserDirectoryException</code> is thrown to indicate that a security
 * operation failed as a result of an existing tenant user directory.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The tenant user directory already exists")
@WebFault(
    name = "ExistingTenantUserDirectoryException",
    targetNamespace = "http://security.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused"})
public class ExistingTenantUserDirectoryException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ExistingTenantUserDirectoryException</code>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param userDirectoryId the Universally Unique Identifier (UUID) for the user
   *     directory
   */
  public ExistingTenantUserDirectoryException(UUID tenantId, UUID userDirectoryId) {
    super(
        "ExistingTenantUserDirectoryError",
        "The tenant user directory for the tenant ("
            + tenantId
            + ") and user directory ("
            + userDirectoryId
            + ") already exists");
  }
}

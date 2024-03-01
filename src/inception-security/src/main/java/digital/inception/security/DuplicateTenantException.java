/*
 * Copyright Marcus Portmann
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

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;
import java.util.UUID;

/**
 * A <b>DuplicateTenantException</b> is thrown to indicate that a security operation failed as a
 * result of a duplicate tenant.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/duplicate-tenant",
    title = "A tenant with the specified ID or name already exists.",
    status = 409)
@WebFault(
    name = "DuplicateTenantException",
    targetNamespace = "http://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateTenantException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateTenantException</b>.
   *
   * @param name the name of the tenant
   */
  public DuplicateTenantException(String name) {
    super("The tenant (" + name + ") already exists");
  }

  /**
   * Constructs a new <b>DuplicateTenantException</b>.
   *
   * @param tenantId the ID for the tenant
   */
  public DuplicateTenantException(UUID tenantId) {
    super("The tenant (" + tenantId + ") already exists");
  }
}

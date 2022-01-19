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

package digital.inception.party;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <b>MandateNotFoundException</b> exception is thrown to indicate an error condition as a
 * result of a mandate that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "http://inception.digital/problems/party/mandate-not-found",
    title = "The mandate could not be found.",
    status = 404)
@WebFault(
    name = "MandateNotFoundException",
    targetNamespace = "http://inception.digital/party",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MandateNotFoundException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>MandateNotFoundException</b>.
   *
   * @param tenantId the ID for the tenant
   * @param mandateId the ID for the mandate
   */
  public MandateNotFoundException(UUID tenantId, UUID mandateId) {
    super("The mandate (" + mandateId + ") could not be found for the tenant (" + tenantId + ")");
  }
}

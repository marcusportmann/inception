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

package digital.inception.audit.ws;

import digital.inception.audit.service.AuditService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.context.ApplicationContext;

/**
 * The <b>AuditWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "AuditService",
    name = "IAuditService",
    targetNamespace = "https://inception.digital/audit")
@SOAPBinding
@SuppressWarnings({"unused", "WeakerAccess", "ValidExternallyBoundObject"})
public class AuditWebService extends AbstractWebServiceBase {

  /** The Audit Service. */
  private final AuditService auditService;

  /**
   * Constructs a new <b>AuditWebService</b>.
   *
   * @param applicationContext the Spring application context
   * @param auditService the Audit Service
   */
  public AuditWebService(ApplicationContext applicationContext, AuditService auditService) {
    super(applicationContext);

    this.auditService = auditService;
  }
}

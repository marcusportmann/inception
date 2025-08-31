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

package digital.inception.operations.exception;

import digital.inception.core.exception.Problem;
import digital.inception.core.exception.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code ExternalReferenceTypeNotFoundException} exception is thrown to indicate an error
 * condition as a result of an external reference type that could not be found. condition as a
 * result of an external reference type that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/external-reference-type-not-found",
    title = "The external reference type could not be found.",
    status = 404)
@WebFault(
    name = "ExternalReferenceTypeNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ExternalReferenceTypeNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code ExternalReferenceTypeNotFoundException}.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   */
  public ExternalReferenceTypeNotFoundException(String externalReferenceTypeCode) {
    super("The external reference type (" + externalReferenceTypeCode + ") could not be found");
  }
}

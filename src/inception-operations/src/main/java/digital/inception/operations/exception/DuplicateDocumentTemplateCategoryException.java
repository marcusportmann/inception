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
 * The {@code DuplicateDocumentTemplateCategoryException} exception is thrown to indicate an error
 * condition as a result of an attempt to create a duplicate document template category, i.e., a
 * document template category with the specified ID already exists.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/duplicate-document-template-category",
    title = "A document template category with the specified ID already exists.",
    status = 409)
@WebFault(
    name = "DuplicateDocumentTemplateCategoryException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateDocumentTemplateCategoryException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code DuplicateDocumentTemplateCategoryException}.
   *
   * @param documentTemplateCategoryId the ID for the document template category
   */
  public DuplicateDocumentTemplateCategoryException(String documentTemplateCategoryId) {
    super("The document template category (" + documentTemplateCategoryId + ") already exists");
  }
}

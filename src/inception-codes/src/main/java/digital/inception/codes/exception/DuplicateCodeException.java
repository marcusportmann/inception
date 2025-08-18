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

package digital.inception.codes.exception;

import digital.inception.core.exception.Problem;
import digital.inception.core.exception.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code DuplicateCodeException} exception is thrown to indicate an error condition as a result
 * of an attempt to create a duplicate code, i.e., a code with the specified ID already exists for
 * the code category.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/codes/duplicate-code",
    title = "A code with the specified ID already exists for the code category.",
    status = 409)
@WebFault(
    name = "DuplicateCodeException",
    targetNamespace = "https://inception.digital/codes",
    faultBean = "digital.inception.core.exception.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateCodeException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code DuplicateCodeException}.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   */
  public DuplicateCodeException(String codeCategoryId, String codeId) {
    super(
        "The code (" + codeId + ") already exists for the code category (" + codeCategoryId + ")");
  }
}

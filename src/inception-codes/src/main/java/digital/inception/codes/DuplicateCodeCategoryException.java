/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.codes;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <b>DuplicateCodeCategoryException</b> exception is thrown to indicate an error condition as a
 * result of an attempt to create a duplicate code category i.e a code category with the specified
 * ID already exists.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "http://inception.digital/problems/codes/duplicate-code-category",
    title = "A code category with the specified ID already exists.",
    status = 409)
@WebFault(
    name = "DuplicateCodeCategoryException",
    targetNamespace = "http://inception.digital/codes",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateCodeCategoryException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateCodeCategoryException</b>.
   *
   * @param codeCategoryId the ID for the code category
   */
  public DuplicateCodeCategoryException(String codeCategoryId) {
    super("The code category with ID (" + codeCategoryId + ") already exists");
  }
}

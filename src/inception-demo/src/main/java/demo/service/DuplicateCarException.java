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

package demo.service;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;
import java.util.UUID;

/**
 * The {@code DuplicateCarException} exception is thrown to indicate an error condition as a result
 * of an attempt to create a duplicate car.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://demo/problems/duplicate-car-exception",
    title = "The car already exists.",
    status = 409)
@WebFault(
    name = "DuplicateCarException",
    targetNamespace = "https://demo",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateCarException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Creates a new {@code DuplicateCarException} instance.
   *
   * @param id the ID for the car
   */
  public DuplicateCarException(UUID id) {
    super("The car (" + id + ") already exists");
  }
}

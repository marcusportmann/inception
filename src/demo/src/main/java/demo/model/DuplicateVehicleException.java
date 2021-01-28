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

package demo.model;

import digital.inception.core.service.ServiceException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>DuplicateVehicleException</b> exception is thrown to indicate an error condition as a
 * result of an attempt to create a duplicate vehicle.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The vehicle already exists")
@WebFault(
    name = "DuplicateVehicleException",
    targetNamespace = "http://demo",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class DuplicateVehicleException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateVehicleException</b>.
   *
   * @param id the Universally Unique Identifier (UUID) for the vehicle
   */
  public DuplicateVehicleException(UUID id) {
    super("DuplicateVehicleError", "The vehicle with the ID (" + id + ") already exists");
  }
}

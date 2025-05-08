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

package digital.inception.core.service;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The {@code InvalidArgumentError} class holds the invalid argument error information.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InvalidArgumentError", namespace = "https://inception.digital/core")
@XmlType(
    name = "InvalidArgumentError",
    namespace = "https://inception.digital/core",
    propOrder = {"parameter", "validationErrors"})
@SuppressWarnings({"unused"})
public class InvalidArgumentError extends ServiceError implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name of the parameter associated with the invalid argument error. */
  @XmlElement(name = "Parameter", required = true)
  private String parameter;

  /** The validation errors associated with the invalid argument error. */
  @XmlElementWrapper(name = "ValidationErrors", required = true)
  @XmlElement(name = "ValidationError")
  private List<ValidationError> validationErrors;

  /** Creates a new {@code InvalidArgumentError} instance. */
  public InvalidArgumentError() {}

  /**
   * Creates a new {@code InvalidArgumentError} instance.
   *
   * @param message the message for the invalid argument error
   * @param parameter the name of the parameter associated with the invalid argument error
   * @param validationErrors the validation errors associated with the invalid argument
   */
  public InvalidArgumentError(
      String message, String parameter, List<ValidationError> validationErrors) {
    super(message);
    this.parameter = parameter;
    this.validationErrors = validationErrors;
  }

  /**
   * Returns the name of the parameter associated with the invalid argument error.
   *
   * @return the name of the parameter associated with the invalid argument error
   */
  public String getParameter() {
    return parameter;
  }

  /**
   * Returns the validation errors associated with the invalid argument error.
   *
   * @return the validation errors associated with the invalid argument error
   */
  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }
}

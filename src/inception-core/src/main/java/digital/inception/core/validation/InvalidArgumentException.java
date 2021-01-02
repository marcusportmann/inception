/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.core.validation;

// ~--- non-JDK imports --------------------------------------------------------

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InvalidArgumentException</code> exception is thrown to indicate an error condition as a
 * result of an invalid argument.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@WebFault(
    name = "InvalidArgumentException",
    targetNamespace = "http://validation.inception.digital",
    faultBean = "digital.inception.validation.InvalidArgumentError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings("unused")
public class InvalidArgumentException extends Exception {

  private static final long serialVersionUID = 1000000;

  /** The invalid argument error information. */
  private final InvalidArgumentError invalidArgumentError;

  /** The name of the invalid argument. */
  private final String name;

  /** The validation errors associated with the invalid argument. */
  private List<ValidationError> validationErrors;

  /**
   * Constructs a new <code>InvalidArgumentException</code> with <code>null</code> as its message.
   *
   * @param name the name of the invalid argument
   */
  public InvalidArgumentException(String name) {
    super();

    this.name = name;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Constructs a new <code>InvalidArgumentException</code> with <code>null</code> as its message.
   *
   * @param name the name of the invalid argument
   * @param validationErrors the validation errors associated with the invalid argument
   */
  public InvalidArgumentException(String name, List<ValidationError> validationErrors) {
    super();

    this.name = name;
    this.validationErrors = validationErrors;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Constructs a new <code>InvalidArgumentException</code> with the specified message.
   *
   * @param name the name of the invalid argument
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public InvalidArgumentException(String name, String message) {
    super(message);

    this.name = name;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Constructs a new <code>InvalidArgumentException</code> with the specified cause and a message
   * of <code>(cause==null ? null : cause.toString())</code> (which typically contains the class and
   * message of cause).
   *
   * @param name the name of the invalid argument
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidArgumentException(String name, Throwable cause) {
    super(cause);

    this.name = name;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Constructs a new <code>InvalidArgumentException</code> with the specified message.
   *
   * @param name the name of the invalid argument
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param validationErrors the validation errors associated with the invalid argument
   */
  public InvalidArgumentException(
      String name, String message, List<ValidationError> validationErrors) {
    super(message);

    this.name = name;
    this.validationErrors = validationErrors;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Constructs a new <code>InvalidArgumentException</code> with the specified message and cause.
   *
   * @param name the name of the invalid argument
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidArgumentException(String name, String message, Throwable cause) {
    super(message, cause);

    this.name = name;
    this.invalidArgumentError = new InvalidArgumentError(this);
  }

  /**
   * Returns the fault info.
   *
   * @return the fault info
   */
  public InvalidArgumentError getFaultInfo() {
    return invalidArgumentError;
  }

  /**
   * Returns the invalid argument error info.
   *
   * @return the invalid argument error info
   */
  public InvalidArgumentError getInvalidArgumentError() {
    return invalidArgumentError;
  }

  /**
   * Returns the name of the invalid argument.
   *
   * @return the name of the invalid argument
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the validation errors associated with the invalid argument.
   *
   * @return the validation errors associated with the invalid argument
   */
  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }
}

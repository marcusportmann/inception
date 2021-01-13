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

package digital.inception.core.validation;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.xml.LocalDateTimeAdapter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InvalidArgumentError</code> class holds the invalid argument error information.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InvalidArgumentError", namespace = "http://validation.inception.digital")
@XmlType(
    name = "InvalidArgumentError",
    namespace = "http://validation.inception.digital",
    propOrder = {"when", "message", "name", "detail", "validationErrors"})
@SuppressWarnings({"unused"})
public class InvalidArgumentError {

  /** The detail for the invalid argument error */
  @XmlElement(name = "Detail", required = true)
  private String detail;

  /** The message for the invalid argument error. */
  @XmlElement(name = "Message", required = true)
  private String message;

  /** The name of the argument associated with the invalid argument error */
  @XmlElement(name = "Name", required = true)
  private String name;

  /** The optional validation errors associated with the invalid argument error */
  @XmlElement(name = "ValidationErrors")
  private List<ValidationError> validationErrors;

  /** The date and time the invalid argument error occurred. */
  @XmlElement(name = "When", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime when;

  /** Constructs a new <code>InvalidArgumentError</code>. */
  public InvalidArgumentError() {}

  /**
   * Constructs a new <code>InvalidArgumentError</code>.
   *
   * @param cause the cause of the invalid argument error
   */
  public InvalidArgumentError(InvalidArgumentException cause) {
    this.when = LocalDateTime.now();
    this.message = (cause.getMessage() != null) ? cause.getMessage() : "Invalid Argument";
    this.name =
        (StringUtils.hasText(cause.getName()))
            ? ValidationError.capitalizePropertyName(cause.getName())
            : "Unknown";

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintWriter pw = new PrintWriter(baos);

      pw.println(message);
      pw.println();
      cause.printStackTrace(pw);
      pw.flush();
      this.detail = baos.toString();
    } catch (Throwable e) {
      this.detail = "Failed to dump the stack for the exception (" + cause + "): " + e.getMessage();
    }

    try {
      if (cause.getValidationErrors() != null) {
        this.validationErrors = new ArrayList<>();

        for (ValidationError validationError : cause.getValidationErrors()) {
          ValidationError newValidationError = (ValidationError) validationError.clone();

          newValidationError.setProperty(
              ValidationError.capitalizePropertyName(newValidationError.getProperty()));

          this.validationErrors.add(newValidationError);
        }
      }
    } catch (Throwable e) {
      this.validationErrors = cause.getValidationErrors();
    }
  }

  /**
   * Returns the detail for the invalid argument error.
   *
   * @return the detail for the invalid argument error
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Returns the message for the invalid argument error.
   *
   * @return the message for the invalid argument error
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the name of the argument associated with the invalid argument error.
   *
   * @return the name of the argument associated with the invalid argument error
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the optional validation errors associated with the invalid argument error.
   *
   * @return the optional validation errors associated with the invalid argument error
   */
  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }

  /**
   * Returns the date and time the invalid argument error occurred.
   *
   * @return the date and time the invalid argument error occurred
   */
  public LocalDateTime getWhen() {
    return when;
  }
}

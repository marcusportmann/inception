/*
 * Copyright 2019 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.xml.LocalDateTimeAdapter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ServiceError</code> class holds the service error information.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ServiceError", namespace = "http://core.inception.digital")
@XmlType(name = "ServiceError", namespace = "http://core.inception.digital",
    propOrder = {"when", "code", "message", "detail"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class ServiceError {

  /**
   * The code identifying the service error.
   */
  @XmlElement(name = "Code", required = true)
  private String code;

  /**
   * The detail for the service error
   */
  @XmlElement(name = "Detail", required = true)
  private String detail;

  /**
   * The message for the service error.
   */
  @XmlElement(name = "Message", required = true)
  private String message;

  /**
   * The date and time the service error occurred.
   */
  @XmlElement(name = "When", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime when;

  /**
   * Constructs a new <code>ServiceError</code>.
   */
  public ServiceError() {
  }

  /**
   * Constructs a new <code>ServiceError</code>.
   *
   * @param code  the code identifying the service error
   * @param cause the cause of the service error
   */
  public ServiceError(String code, Throwable cause) {
    this.code = code;
    this.when = LocalDateTime.now();
    this.message = (cause.getMessage() != null)
        ? cause.getMessage()
        : cause.getClass().getSimpleName();

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
  }

  /**
   * Returns the code identifying the service error.
   *
   * @return the code identifying the service error
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the detail for the service error.
   *
   * @return the detail for the service error
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Returns the message for the service error.
   *
   * @return the message for the service error
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the date and time the service error occurred.
   *
   * @return the date and time the service error occurred
   */
  public LocalDateTime getWhen() {
    return when;
  }
}

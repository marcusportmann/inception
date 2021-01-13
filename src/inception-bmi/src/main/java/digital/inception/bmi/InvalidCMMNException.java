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

package digital.inception.bmi;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InvalidCMMNException</code> exception is thrown to indicate an error condition as a
 * result of invalid CMMN 1.1 XML data.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The CMMN 1.1 XML data is invalid")
@WebFault(
    name = "InvalidCMMNException",
    targetNamespace = "http://bmi.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused"})
public class InvalidCMMNException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /** Constructs a new <code>InvalidCMMNException</code>. */
  public InvalidCMMNException() {
    super("InvalidCMMNError", "The CMMN 1.1 XML data is invalid");
  }

  /**
   * Constructs a new <code>InvalidCMMNException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public InvalidCMMNException(String message) {
    super("InvalidCMMNError", message);
  }

  /**
   * Constructs a new <code>InvalidCMMNException</code>.
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidCMMNException(Throwable cause) {
    super("InvalidCMMNError", "The CMMN 1.1 XML data is invalid", cause);
  }
}

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

import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>InvalidBPMNException</b> exception is thrown to indicate an error condition as a result of
 * invalid BPMN 2.0 XML data.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The BPMN 2.0 XML data is invalid")
@WebFault(
    name = "InvalidBPMNException",
    targetNamespace = "http://bmi.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused"})
public class InvalidBPMNException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /** Constructs a new <b>InvalidBPMNException</b>. */
  public InvalidBPMNException() {
    super("InvalidBPMNError", "The BPMN 2.0 XML data is invalid");
  }

  /**
   * Constructs a new <b>InvalidBPMNException</b> with the specified message.
   *
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   */
  public InvalidBPMNException(String message) {
    super("InvalidBPMNError", message);
  }

  /**
   * Constructs a new <b>InvalidBPMNException</b>.
   *
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidBPMNException(Throwable cause) {
    super("InvalidBPMNError", "The BPMN 2.0 XML data is invalid", cause);
  }
}

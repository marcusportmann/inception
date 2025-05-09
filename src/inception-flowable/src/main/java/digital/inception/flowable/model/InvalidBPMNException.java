/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.flowable.model;
//
// import digital.inception.core.service.Problem;
// import digital.inception.core.service.ServiceException;
// import jakarta.xml.bind.annotation.XmlAccessType;
// import jakarta.xml.bind.annotation.XmlAccessorType;
// import jakarta.xml.ws.WebFault;
// import java.io.Serial;
//
/// **
// * The {@code InvalidBPMNException} exception is thrown to indicate an error condition as a result
// of
// * invalid BPMN 2.0 XML data.
// *
// * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
// *
// * @author Marcus Portmann
// */
// @Problem(
//    type = "https://inception.digital/problems/workflow/invalid-bpmn",
//    title = "The BPMN 2.0 XML data is invalid.",
//    status = 400)
// @WebFault(
//    name = "InvalidBPMNException",
//    targetNamespace = "https://inception.digital/workflow",
//    faultBean = "digital.inception.core.service.ServiceError")
// @XmlAccessorType(XmlAccessType.PROPERTY)
// @SuppressWarnings("unused")
// public class InvalidBPMNException extends ServiceException {
//
//  @Serial private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new {@code InvalidBPMNException} instance with the specified message.
//   *
//   * @param message The message saved for later retrieval by the {@code getMessage()} method.
//   */
//  public InvalidBPMNException(String message) {
//    super(message);
//  }
//
//  /**
//   * Constructs a new {@code InvalidBPMNException}.
//   *
//   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A
//   *     {@code null} value is permitted if the cause is nonexistent or unknown)
//   */
//  public InvalidBPMNException(Throwable cause) {
//    super("The BPMN 2.0 XML data is invalid", cause);
//  }
// }

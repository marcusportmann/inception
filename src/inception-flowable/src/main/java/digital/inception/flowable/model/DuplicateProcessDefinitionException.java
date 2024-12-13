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
// * The <b>DuplicateProcessDefinitionException</b> exception is thrown to indicate an error
// condition
// * as a result of an attempt to create a duplicate process definition, i.e. a process definition
// * with the specified ID already exists.
// *
// * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
// *
// * @author Marcus Portmann
// */
// @Problem(
//    type = "https://inception.digital/problems/workflow/duplicate-process-definition",
//    title = "A process definition with the specified ID already exists.",
//    status = 409)
// @WebFault(
//    name = "DuplicateProcessDefinitionException",
//    targetNamespace = "https://inception.digital/workflow",
//    faultBean = "digital.inception.core.service.ServiceError")
// @XmlAccessorType(XmlAccessType.PROPERTY)
// @SuppressWarnings("unused")
// public class DuplicateProcessDefinitionException extends ServiceException {
//
//  @Serial private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new <b>DuplicateProcessDefinitionException</b>.
//   *
//   * @param processDefinitionId the ID for the process definition
//   */
//  public DuplicateProcessDefinitionException(String processDefinitionId) {
//    super("The process definition with ID (" + processDefinitionId + ") already exists");
//  }
// }

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
// package digital.inception.operations.exception;
//
// import digital.inception.core.exception.Problem;
// import digital.inception.core.exception.ServiceException;
// import jakarta.xml.bind.annotation.XmlAccessType;
// import jakarta.xml.bind.annotation.XmlAccessorType;
// import jakarta.xml.ws.WebFault;
// import java.io.Serial;
//
/// **
// * The {@code DuplicateWorkflowAttributeDefinitionException} exception is thrown to indicate an
// * error condition as a result of an attempt to create a duplicate workflow attribute definition,
// * i.e., a workflow attribute definition with the specified code already exists.
// *
// * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
// *
// * @author Marcus Portmann
// */
// @Problem(
//    type =
// "https://inception.digital/problems/operations/duplicate-workflow-attribute-definition",
//    title = "A workflow attribute definition with the specified code already exists.",
//    status = 409)
// @WebFault(
//    name = "DuplicateWorkflowAttributeDefinitionException",
//    targetNamespace = "https://inception.digital/operations",
//    faultBean = "digital.inception.core.service.ServiceError")
// @XmlAccessorType(XmlAccessType.PROPERTY)
// public class DuplicateWorkflowAttributeDefinitionException extends ServiceException {
//
//  @Serial private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new {@code DuplicateWorkflowAttributeDefinitionException}.
//   *
//   * @param workflowAttributeDefinitionCode the code for the workflow attribute definition
//   */
//  public DuplicateWorkflowAttributeDefinitionException(String workflowAttributeDefinitionCode) {
//    super(
//        "The workflow attribute definition ("
//            + workflowAttributeDefinitionCode
//            + ") already exists");
//  }
// }

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

package digital.inception.operations.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.ExternalReference;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowVariable;
import java.util.List;
import java.util.UUID;

/**
 * The {@code ValidationService} interface defines the functionality provided by a Validation
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface ValidationService {

  /**
   * Check whether a document attribute with the specified code is valid for a document.
   *
   * @param tenantId the ID for the tenant
   * @param documentDefinitionId the ID for the document definition the document is associated with
   * @param attributeCode the code for the document attribute
   * @return {@code true} if a document attribute with the specified code is valid or {@code false}
   *     otherwise
   * @throws ServiceUnavailableException if the validity of the document attribute code could not be
   *     verified
   */
  boolean isValidDocumentAttribute(UUID tenantId, String documentDefinitionId, String attributeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether a workflow attribute with the specified code is valid for a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionId the ID for the workflow definition the workflow is associated with
   * @param attributeCode the code for the workflow attribute
   * @return {@code true} if a workflow attribute with the specified code is valid or {@code false}
   *     otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the validity of the workflow attribute code could not be
   *     verified
   */
  boolean isValidWorkflowAttribute(UUID tenantId, String workflowDefinitionId, String attributeCode)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the document attributes that have been provided are allowed for the document
   * definition.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the attributes being validated, e.g.
   *     initiateDocumentRequest.attributes
   * @param documentDefinitionId the ID for the document definition
   * @param documentAttributes the document attributes to validate
   * @throws InvalidArgumentException if a document attribute is not valid
   * @throws ServiceUnavailableException if the document attributes could not be validated
   */
  void validateAllowedDocumentAttributes(
      UUID tenantId,
      String parameter,
      String documentDefinitionId,
      List<DocumentAttribute> documentAttributes)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the workflow attributes that have been provided are allowed for the workflow
   * definition.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the attributes being validated, e.g.
   *     initiateWorkflowRequest.attributes
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowAttributes the workflow attributes to validate
   * @throws InvalidArgumentException if a workflow attribute is not valid
   * @throws ServiceUnavailableException if the workflow attributes could not be validated
   */
  void validateAllowedWorkflowAttributes(
      UUID tenantId,
      String parameter,
      String workflowDefinitionId,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the workflow variables that have been provided are allowed for the workflow
   * definition.
   *
   * @param parameter the parameter path to the variables being validated, e.g.
   *     initiateWorkflowRequest.variables
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowVariables the workflow variables to validate
   * @throws InvalidArgumentException if a workflow variable is not valid
   * @throws ServiceUnavailableException if the workflow variables could not be validated
   */
  void validateAllowedWorkflowVariables(
      String parameter, String workflowDefinitionId, List<WorkflowVariable> workflowVariables)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate the external references.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the external references being validated, e.g.
   *     initiateWorkflowRequest.externalReferences
   * @param objectType the type of object the external references are associated with
   * @param externalReferences the external references to validate
   * @throws InvalidArgumentException if an external reference is not valid
   * @throws ServiceUnavailableException if the external references could not be validated
   */
  void validateExternalReferences(
      UUID tenantId,
      String parameter,
      ObjectType objectType,
      List<? extends ExternalReference> externalReferences)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the required document attributes have been provided for the document
   * definition.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the attributes being validated, e.g.
   *     initiateDocumentRequest.attributes
   * @param documentDefinitionId the ID for the document definition
   * @param documentAttributes the document attributes to validate
   * @throws InvalidArgumentException if a required document attribute has not been provided
   * @throws ServiceUnavailableException if the required document attributes could not be validated
   */
  void validateRequiredDocumentAttributes(
      UUID tenantId,
      String parameter,
      String documentDefinitionId,
      List<DocumentAttribute> documentAttributes)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the required workflow attributes have been provided for the workflow
   * definition.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the attributes being validated, e.g.
   *     initiateWorkflowRequest.attributes
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowAttributes the workflow attributes to validate
   * @throws InvalidArgumentException if a required workflow attribute has not been provided
   * @throws ServiceUnavailableException if the required workflow attributes could not be validated
   */
  void validateRequiredWorkflowAttributes(
      UUID tenantId,
      String parameter,
      String workflowDefinitionId,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate whether the required workflow variables have been provided for the workflow
   * definition.
   *
   * @param parameter the parameter path to the variables being validated, e.g.
   *     initiateWorkflowRequest.variables
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowVariables the workflow variables to validate
   * @throws InvalidArgumentException if a required workflow variable has not been provided
   * @throws ServiceUnavailableException if the required workflow variables could not be validated
   */
  void validateRequiredWorkflowVariables(
      String parameter, String workflowDefinitionId, List<WorkflowVariable> workflowVariables)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate the workflow data using the specified validation schema.
   *
   * @param validationSchemaType the validation schema type
   * @param validationSchema the JSON or XML (XSD) validation schema
   * @param workflowData the JSON or XML data for the workflow
   * @return {@code true} if the workflow data was validated successfully or {@code false} otherwise
   */
  boolean validateWorkflowData(
      ValidationSchemaType validationSchemaType, String validationSchema, String workflowData);
}

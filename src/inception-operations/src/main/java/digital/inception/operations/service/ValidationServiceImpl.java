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
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentAttributeDefinition;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.ExternalReference;
import digital.inception.operations.model.ExternalReferenceType;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowAttributeDefinition;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowVariable;
import digital.inception.operations.model.WorkflowVariableDefinition;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code ValidationServiceImpl} class provides the Validation Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class ValidationServiceImpl extends AbstractServiceBase implements ValidationService {

  /** The Document Service. */
  private DocumentService documentService;

  /** The Operations Reference Service. */
  private OperationsReferenceService operationsReferenceService;

  /** The Workflow Service. */
  private WorkflowService workflowService;

  /**
   * Constructs a new {@code ValidationServiceImpl}.
   *
   * @param applicationContext the Spring application context
   */
  public ValidationServiceImpl(ApplicationContext applicationContext) {
    super(applicationContext);
  }

  @Override
  public boolean isValidDocumentAttribute(
      DocumentDefinition documentDefinition, String attributeCode, String attributeValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (documentDefinition == null) {
      throw new InvalidArgumentException("documentDefinition");
    }

    if (!StringUtils.hasText(attributeCode)) {
      throw new InvalidArgumentException("attributeCode");
    }

    try {
      // Find the attribute definition by code
      DocumentAttributeDefinition documentAttributeDefinition =
          documentDefinition.getAttributeDefinitions().stream()
              .filter(attributeDefinition -> attributeCode.equals(attributeDefinition.getCode()))
              .findFirst()
              .orElse(null);

      // If no matching attribute definition, it's not valid
      if (documentAttributeDefinition == null) {
        return false;
      }

      // If no pattern is defined, existence is sufficient for validity
      String pattern = documentAttributeDefinition.getPattern();
      if (!StringUtils.hasText(pattern)) {
        return true;
      }

      // Validate the value against the regex pattern (full match)
      if (!StringUtils.hasText(attributeValue)) {
        return false;
      }
      return Pattern.compile(pattern).matcher(attributeValue).matches();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the document attribute ("
              + attributeCode
              + ") for the document with the document definition ("
              + documentDefinition.getId()
              + ")",
          e);
    }
  }

  @Override
  public boolean isValidWorkflowAttribute(
      WorkflowDefinition workflowDefinition, String attributeCode, String attributeValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (workflowDefinition == null) {
      throw new InvalidArgumentException("workflowDefinition");
    }

    if (!StringUtils.hasText(attributeCode)) {
      throw new InvalidArgumentException("attributeCode");
    }

    try {
      // Find the attribute definition by code
      WorkflowAttributeDefinition workflowAttributeDefinition =
          workflowDefinition.getAttributeDefinitions().stream()
              .filter(attributeDefinition -> attributeCode.equals(attributeDefinition.getCode()))
              .findFirst()
              .orElse(null);

      // If no matching attribute definition, it's not valid
      if (workflowAttributeDefinition == null) {
        return false;
      }

      // If no pattern is defined, existence is sufficient for validity
      String pattern = workflowAttributeDefinition.getPattern();
      if (!StringUtils.hasText(pattern)) {
        return true;
      }

      // Validate the value against the regex pattern (full match)
      if (!StringUtils.hasText(attributeValue)) {
        return false;
      }
      return Pattern.compile(pattern).matcher(attributeValue).matches();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the workflow attribute ("
              + attributeCode
              + ") for the workflow with the workflow definition ("
              + workflowDefinition.getId()
              + ")",
          e);
    }
  }

  @Override
  public void validateAllowedDocumentAttributes(
      String parameter,
      DocumentDefinition documentDefinition,
      List<DocumentAttribute> documentAttributes)
      throws InvalidArgumentException, ServiceUnavailableException {
    // Early exit if no document attributes to validate
    if (documentAttributes == null || documentAttributes.isEmpty()) {
      return;
    }

    for (DocumentAttribute documentAttribute : documentAttributes) {
      if (!isValidDocumentAttribute(
          documentDefinition, documentAttribute.getCode(), documentAttribute.getValue())) {
        throw new InvalidArgumentException(
            parameter, "the document attribute (" + documentAttribute.getCode() + ") is invalid");
      }
    }
  }

  @Override
  public void validateAllowedWorkflowAttributes(
      String parameter,
      WorkflowDefinition workflowDefinition,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException {
    // Early exit if no workflow attributes to validate
    if (workflowAttributes == null || workflowAttributes.isEmpty()) {
      return;
    }

    for (WorkflowAttribute workflowAttribute : workflowAttributes) {
      if (!isValidWorkflowAttribute(
          workflowDefinition, workflowAttribute.getCode(), workflowAttribute.getValue())) {
        throw new InvalidArgumentException(
            parameter, "the workflow attribute (" + workflowAttribute.getCode() + ") is invalid");
      }
    }
  }

  @Override
  public void validateAllowedWorkflowVariables(
      String parameter,
      WorkflowDefinition workflowDefinition,
      List<WorkflowVariable> workflowVariables)
      throws InvalidArgumentException, ServiceUnavailableException {
    // Early exit if no workflow variables to validate
    if (workflowVariables == null || workflowVariables.isEmpty()) {
      return;
    }

    try {
      if (!workflowDefinition.getVariableDefinitions().isEmpty()) {
        // Create a Set of valid variable names for O(1) lookup
        Set<String> validVariableNames =
            workflowDefinition.getVariableDefinitions().stream()
                .map(WorkflowVariableDefinition::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Find the first invalid variable using Stream API
        Optional<String> invalidVariableName =
            workflowVariables.stream()
                .map(WorkflowVariable::getName)
                .filter(name -> !validVariableNames.contains(name.toLowerCase()))
                .findFirst();

        if (invalidVariableName.isPresent()) {
          throw new InvalidArgumentException(
              parameter, "the workflow variable (" + invalidVariableName.get() + ") is invalid");
        }
      }

    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the allowed workflow variables for the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public void validateExternalReferences(
      UUID tenantId,
      String parameter,
      ObjectType objectType,
      List<? extends ExternalReference> externalReferences)
      throws InvalidArgumentException, ServiceUnavailableException {
    // Early exit if no external references to validate
    if (externalReferences == null || externalReferences.isEmpty()) {
      return;
    }

    try {
      List<ExternalReferenceType> externalReferenceTypes =
          getOperationsReferenceService().getExternalReferenceTypes(tenantId);

      // Create a Set of valid reference type codes for O(1) lookup instead of O(n) nested loops
      Set<String> validReferenceCodes =
          externalReferenceTypes.stream()
              .filter(
                  type -> ((type.getObjectType() == null) || (objectType == type.getObjectType())))
              .map(ExternalReferenceType::getCode)
              .map(String::toLowerCase) // Normalize for case-insensitive comparison
              .collect(Collectors.toSet());

      // Find the first invalid external reference using Stream API
      Optional<String> invalidReferenceType =
          externalReferences.stream()
              .map(ExternalReference::getType)
              .filter(type -> !validReferenceCodes.contains(type.toLowerCase()))
              .findFirst();

      if (invalidReferenceType.isPresent()) {
        throw new InvalidArgumentException(
            parameter,
            "the external reference type ("
                + invalidReferenceType.get()
                + ") is invalid for the object type ("
                + objectType
                + ")");
      }
    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the external references for the object type ("
              + objectType
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void validateRequiredDocumentAttributes(
      String parameter,
      DocumentDefinition documentDefinition,
      List<DocumentAttribute> documentAttributes)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      List<DocumentAttributeDefinition> requiredDocumentAttributeDefinitions =
          documentDefinition.getAttributeDefinitions().stream()
              .filter(DocumentAttributeDefinition::isRequired)
              .toList();

      // Early exit if no required document attribute definitions exist
      if (requiredDocumentAttributeDefinitions.isEmpty()) {
        return;
      }

      // Create a Set for O(1) lookup performance instead of O(n) stream operations
      Set<String> providedDocumentAttributeCodes =
          documentAttributes.stream().map(DocumentAttribute::getCode).collect(Collectors.toSet());

      // Filter and validate in a single pass
      String missingDocumentAttributeCode =
          requiredDocumentAttributeDefinitions.stream()
              .map(DocumentAttributeDefinition::getCode)
              .filter(code -> providedDocumentAttributeCodes.stream().noneMatch(code::equals))
              .findFirst()
              .orElse(null);

      if (missingDocumentAttributeCode != null) {
        throw new InvalidArgumentException(
            parameter, "the document attribute (" + missingDocumentAttributeCode + ") is required");
      }
    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the required document attributes for the document definition ("
              + documentDefinition.getId()
              + ")",
          e);
    }
  }

  @Override
  public void validateRequiredWorkflowAttributes(
      String parameter,
      WorkflowDefinition workflowDefinition,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      List<WorkflowAttributeDefinition> requiredWorkflowAttributeDefinitions =
          workflowDefinition.getAttributeDefinitions().stream()
              .filter(WorkflowAttributeDefinition::isRequired)
              .toList();

      // Early exit if no required workflow attribute definitions exist
      if (requiredWorkflowAttributeDefinitions.isEmpty()) {
        return;
      }

      // Create a Set for O(1) lookup performance instead of O(n) stream operations
      Set<String> providedWorkflowAttributeCodes =
          workflowAttributes.stream().map(WorkflowAttribute::getCode).collect(Collectors.toSet());

      // Filter and validate in a single pass
      String missingWorkflowAttributeCode =
          requiredWorkflowAttributeDefinitions.stream()
              .map(WorkflowAttributeDefinition::getCode)
              .filter(code -> providedWorkflowAttributeCodes.stream().noneMatch(code::equals))
              .findFirst()
              .orElse(null);

      if (missingWorkflowAttributeCode != null) {
        throw new InvalidArgumentException(
            parameter, "the workflow attribute (" + missingWorkflowAttributeCode + ") is required");
      }
    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the required workflow attributes for the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public void validateRequiredWorkflowVariables(
      String parameter,
      WorkflowDefinition workflowDefinition,
      List<WorkflowVariable> workflowVariables)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      if (!workflowDefinition.getVariableDefinitions().isEmpty()) {
        // Create a Set of provided variable names for O(1) lookup
        Set<String> providedVariableNames =
            workflowVariables.stream()
                .map(WorkflowVariable::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Find the first missing required variable
        Optional<String> missingRequiredVariable =
            workflowDefinition.getVariableDefinitions().stream()
                .filter(WorkflowVariableDefinition::isRequired)
                .map(WorkflowVariableDefinition::getName)
                .filter(name -> !providedVariableNames.contains(name.toLowerCase()))
                .findFirst();

        if (missingRequiredVariable.isPresent()) {
          throw new InvalidArgumentException(
              parameter,
              "the workflow variable (" + missingRequiredVariable.get() + ") is required");
        }
      }

    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the required workflow variables for the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public boolean validateWorkflowData(
      ValidationSchemaType validationSchemaType, String validationSchema, String workflowData) {
    // TODO: Implement JSON and XML validation
    return true;
  }

  /**
   * Returns lazily evaluated Document Service to avoid circular references.
   *
   * @return the lazily evaluated Document Service to avoid circular references.
   */
  private DocumentService getDocumentService() {
    if (documentService == null) {
      documentService = getApplicationContext().getBean(DocumentService.class);
    }

    return documentService;
  }

  /**
   * Returns the lazily evaluated Operations Reference Service to avoid circular references.
   *
   * @return the lazily evaluated Operations Reference Service to avoid circular references.
   */
  private OperationsReferenceService getOperationsReferenceService() {
    if (operationsReferenceService == null) {
      operationsReferenceService =
          getApplicationContext().getBean(OperationsReferenceService.class);
    }

    return operationsReferenceService;
  }

  /**
   * Returns the lazily evaluated Workflow Service to avoid circular references.
   *
   * @return the lazily evaluated Workflow Service to avoid circular references.
   */
  private WorkflowService getWorkflowService() {
    if (workflowService == null) {
      workflowService = getApplicationContext().getBean(WorkflowService.class);
    }

    return workflowService;
  }
}

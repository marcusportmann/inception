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

import digital.inception.core.validation.ValidationSchemaType;

/**
 * The {@code WorkflowDataValidationService} interface defines the functionality provided by a
 * Workflow Data Validation Service implementation.
 *
 * @author Marcus Portmann
 */
public interface WorkflowDataValidationService {

  /**
   * Validate the workflow data using the specified validation schema.
   *
   * @param validationSchemaType the validation schema type
   * @param validationSchema the XML (XSD) or JSON validation schema
   * @param workflowData the XML or JSON data for the workflow
   * @return {@code true} if the workflow data was validated successfully or {@code false} otherwise
   */
  boolean validateWorkflowData(
      ValidationSchemaType validationSchemaType, String validationSchema, String workflowData);
}

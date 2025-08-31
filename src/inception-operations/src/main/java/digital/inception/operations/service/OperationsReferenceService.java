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
import digital.inception.operations.exception.DuplicateExternalReferenceTypeException;
import digital.inception.operations.exception.ExternalReferenceTypeNotFoundException;
import digital.inception.operations.model.ExternalReferenceType;
import java.util.List;
import java.util.UUID;

/**
 * The {@code OperationsReferenceService} interface defines the functionality provided by an
 * Operations Reference Service implementation.
 *
 * @author Marcus Portmann
 */
public interface OperationsReferenceService {

  /**
   * Create the external reference type.
   *
   * @param externalReferenceType the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateExternalReferenceTypeException if the external reference type already exists
   * @throws ServiceUnavailableException if the external reference type could not be created
   */
  void createExternalReferenceType(ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          DuplicateExternalReferenceTypeException,
          ServiceUnavailableException;

  /**
   * Delete the external reference type.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be deleted
   */
  void deleteExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the external reference type.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   * @return the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be retrieved
   */
  ExternalReferenceType getExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the external reference types.
   *
   * @return the external reference types
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference types could not be retrieved
   */
  List<ExternalReferenceType> getExternalReferenceTypes()
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the external reference types for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the external reference types for the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference types could not be retrieved for
   *     the tenant
   */
  List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the external reference type.
   *
   * @param externalReferenceType the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be updated
   */
  void updateExternalReferenceType(ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;
}

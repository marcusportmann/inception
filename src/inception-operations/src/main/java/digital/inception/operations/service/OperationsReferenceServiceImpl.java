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
import digital.inception.operations.exception.DuplicateExternalReferenceTypeException;
import digital.inception.operations.exception.ExternalReferenceTypeNotFoundException;
import digital.inception.operations.model.ExternalReferenceType;
import digital.inception.operations.persistence.jpa.ExternalReferenceTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code OperationsReferenceServiceImpl} class provides the Operations Reference Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class OperationsReferenceServiceImpl extends AbstractServiceBase
    implements OperationsReferenceService {

  /** The External Reference Type Repository. */
  private final ExternalReferenceTypeRepository externalReferenceTypeRepository;

  /**
   * Constructs a new {@code OperationsReferenceServiceImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param externalReferenceTypeRepository the External Reference Type Repository
   */
  public OperationsReferenceServiceImpl(
      ApplicationContext applicationContext,
      ExternalReferenceTypeRepository externalReferenceTypeRepository) {
    super(applicationContext);

    this.externalReferenceTypeRepository = externalReferenceTypeRepository;
  }

  @Override
  @CacheEvict(cacheNames = "externalReferenceTypes", allEntries = true)
  public void createExternalReferenceType(ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          DuplicateExternalReferenceTypeException,
          ServiceUnavailableException {
    validateArgument("externalReferenceType", externalReferenceType);

    try {
      if (externalReferenceTypeRepository.existsById(externalReferenceType.getCode())) {
        throw new DuplicateExternalReferenceTypeException(externalReferenceType.getCode());
      }

      externalReferenceTypeRepository.saveAndFlush(externalReferenceType);
    } catch (DuplicateExternalReferenceTypeException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the external reference type (" + externalReferenceType.getCode() + ")",
          e);
    }
  }

  @Override
  @CacheEvict(cacheNames = "externalReferenceTypes", allEntries = true)
  public void deleteExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      throw new InvalidArgumentException("externalReferenceTypeCode");
    }

    try {
      if (!externalReferenceTypeRepository.existsById(externalReferenceTypeCode)) {
        throw new ExternalReferenceTypeNotFoundException(externalReferenceTypeCode);
      }

      externalReferenceTypeRepository.deleteById(externalReferenceTypeCode);
    } catch (ExternalReferenceTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the external reference type (" + externalReferenceTypeCode + ")", e);
    }
  }

  @Override
  public ExternalReferenceType getExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      throw new InvalidArgumentException("externalReferenceTypeCode");
    }

    try {
      Optional<ExternalReferenceType> externalReferenceTypeOptional =
          externalReferenceTypeRepository.findById(externalReferenceTypeCode);

      if (externalReferenceTypeOptional.isEmpty()) {
        throw new ExternalReferenceTypeNotFoundException(externalReferenceTypeCode);
      }

      return externalReferenceTypeOptional.get();
    } catch (ExternalReferenceTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the external reference type (" + externalReferenceTypeCode + ")", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "externalReferenceTypes", key = "'ALL'")
  public List<ExternalReferenceType> getExternalReferenceTypes()
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      return externalReferenceTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the external reference types", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "externalReferenceTypes", key = "#tenantId")
  public List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return externalReferenceTypeRepository.findForTenantOrGlobal(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the external reference types for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  @CacheEvict(cacheNames = "externalReferenceTypes", allEntries = true)
  public void updateExternalReferenceType(ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    validateArgument("externalReferenceType", externalReferenceType);

    try {
      if (!externalReferenceTypeRepository.existsById(externalReferenceType.getCode())) {
        throw new ExternalReferenceTypeNotFoundException(externalReferenceType.getCode());
      }

      externalReferenceTypeRepository.saveAndFlush(externalReferenceType);
    } catch (ExternalReferenceTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the external reference type (" + externalReferenceType.getCode() + ")",
          e);
    }
  }
}

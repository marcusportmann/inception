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

package digital.inception.operations.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.operations.exception.DuplicateExternalReferenceTypeException;
import digital.inception.operations.exception.ExternalReferenceTypeNotFoundException;
import digital.inception.operations.model.ExternalReferenceType;
import digital.inception.operations.service.OperationsReferenceService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code OperationsReferenceApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class OperationsReferenceApiControllerImpl extends SecureApiController
    implements OperationsReferenceApiController {

  /** The Operations Reference Service. */
  private final OperationsReferenceService operationsReferenceService;

  /**
   * Constructs a new {@code OperationsReferenceApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param operationsReferenceService the Operations Reference Service
   */
  public OperationsReferenceApiControllerImpl(
      ApplicationContext applicationContext,
      OperationsReferenceService operationsReferenceService) {
    super(applicationContext);

    this.operationsReferenceService = operationsReferenceService;
  }

  @Override
  public void createExternalReferenceType(ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          DuplicateExternalReferenceTypeException,
          ServiceUnavailableException {
    operationsReferenceService.createExternalReferenceType(externalReferenceType);
  }

  @Override
  public void deleteExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    operationsReferenceService.deleteExternalReferenceType(externalReferenceTypeCode);
  }

  @Override
  public ExternalReferenceType getExternalReferenceType(String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    return operationsReferenceService.getExternalReferenceType(externalReferenceTypeCode);
  }

  @Override
  public List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return operationsReferenceService.getExternalReferenceTypes(tenantId);
  }

  @Override
  public void updateExternalReferenceType(
      String externalReferenceTypeCode, ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      throw new InvalidArgumentException("externalReferenceTypeCode");
    }

    if (!Objects.equals(externalReferenceTypeCode, externalReferenceType.getCode())) {
      throw new InvalidArgumentException("externalReferenceType.code");
    }

    operationsReferenceService.updateExternalReferenceType(externalReferenceType);
  }
}

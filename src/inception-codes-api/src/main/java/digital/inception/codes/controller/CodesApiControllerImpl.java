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

package digital.inception.codes.controller;

import digital.inception.api.SecureApiController;
import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategoryNotFoundException;
import digital.inception.codes.model.CodeCategorySummary;
import digital.inception.codes.model.CodeNotFoundException;
import digital.inception.codes.model.DuplicateCodeCategoryException;
import digital.inception.codes.model.DuplicateCodeException;
import digital.inception.codes.service.CodesService;
import digital.inception.core.api.ApiUtil;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>CodesApiControllerImpl</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class CodesApiControllerImpl extends SecureApiController implements CodesApiController {

  /** The Codes Service. */
  private final CodesService codesService;

  /**
   * Constructs a new <b>CodesApiControllerImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param codesService the Codes Service
   */
  public CodesApiControllerImpl(ApplicationContext applicationContext, CodesService codesService) {
    super(applicationContext);

    this.codesService = codesService;
  }

  @Override
  public void createCode(String codeCategoryId, Code code)
      throws InvalidArgumentException,
          DuplicateCodeException,
          CodeCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (code == null) {
      throw new InvalidArgumentException("code");
    }

    if (!codeCategoryId.equals(code.getCodeCategoryId())) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    codesService.createCode(code);
  }

  @Override
  public void createCodeCategory(CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, ServiceUnavailableException {
    codesService.createCodeCategory(codeCategory);
  }

  @Override
  public void deleteCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    codesService.deleteCode(codeCategoryId, codeId);
  }

  @Override
  public void deleteCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    codesService.deleteCodeCategory(codeCategoryId);
  }

  @Override
  public Code getCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    return codesService.getCode(codeCategoryId, codeId);
  }

  @Override
  public List<CodeCategory> getCodeCategories() throws ServiceUnavailableException {
    return codesService.getCodeCategories();
  }

  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodeCategory(codeCategoryId);
  }

  @Override
  public String getCodeCategoryData(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    String data = codesService.getCodeCategoryData(codeCategoryId);

    return ApiUtil.quote(StringUtils.hasText(data) ? data : "");
  }

  @Override
  public OffsetDateTime getCodeCategoryLastModified(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodeCategoryLastModified(codeCategoryId);
  }

  @Override
  public String getCodeCategoryName(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(codesService.getCodeCategoryName(codeCategoryId));
  }

  @Override
  public List<CodeCategorySummary> getCodeCategorySummaries() throws ServiceUnavailableException {
    return codesService.getCodeCategorySummaries();
  }

  @Override
  public String getCodeName(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(codesService.getCodeName(codeCategoryId, codeId));
  }

  @Override
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodesForCodeCategory(codeCategoryId);
  }

  @Override
  public void updateCode(String codeCategoryId, String codeId, Code code)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!StringUtils.hasText(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    if (code == null) {
      throw new InvalidArgumentException("code");
    }

    if (!codeCategoryId.equals(code.getCodeCategoryId())) {
      throw new InvalidArgumentException("code");
    }

    if (!codeId.equals(code.getId())) {
      throw new InvalidArgumentException("code");
    }

    codesService.updateCode(code);
  }

  public void updateCodeCategory(String codeCategoryId, CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (codeCategory == null) {
      throw new InvalidArgumentException("codeCategory");
    }

    if (!codeCategoryId.equals(codeCategory.getId())) {
      throw new InvalidArgumentException("codeCategory");
    }

    codesService.updateCodeCategory(codeCategory);
  }
}

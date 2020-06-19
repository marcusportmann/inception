/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ISO8601Util;
import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodesRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags = "Codes API")
@RestController
@RequestMapping(value = "/api/codes")
@SuppressWarnings({"unused"})
public class CodesRestController extends SecureRestController {

  /**
   * The Codes Service.
   */
  private ICodesService codesService;

  /**
   * The JSR-303 Validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>CodesRestController</code>.
   *
   * @param codesService the Codes Service
   * @param validator    the JSR-303 validator
   */
  public CodesRestController(ICodesService codesService, Validator validator) {
    this.codesService = codesService;
    this.validator = validator;
  }

  /**
   * Create the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param code           the code to create
   */
  @ApiOperation(value = "Create the code", notes = "Create the code")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "The code was created successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 409, message = "A code with the specified ID already exists",
          response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void createCode(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "code", value = "The code", required = true)
  @RequestBody Code code)
      throws InvalidArgumentException, DuplicateCodeException, CodeCategoryNotFoundException,
      CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (code == null) {
      throw new InvalidArgumentException("code");
    }

    if (!code.getCodeCategoryId().equals(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    Set<ConstraintViolation<Code>> constraintViolations = validator.validate(code);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("code", ValidationError.toValidationErrors(
          constraintViolations));
    }

    codesService.createCode(code);
  }

  /**
   * Create the code category.
   *
   * @param codeCategory the code category to create
   */
  @ApiOperation(value = "Create the code category", notes = "Create the code category")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The code category was created successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 409, message = "A code category with the specified ID already exists",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void createCodeCategory(@ApiParam(name = "codeCategory", value = "The code category",
      required = true)
  @RequestBody CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, CodesServiceException {
    if (codeCategory == null) {
      throw new InvalidArgumentException("codeCategory");
    }

    Set<ConstraintViolation<CodeCategory>> constraintViolations = validator.validate(codeCategory);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("codeCategory", ValidationError.toValidationErrors(
          constraintViolations));
    }

    codesService.createCodeCategory(codeCategory);
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param codeId         the ID used to uniquely identify the code
   */
  @ApiOperation(value = "Delete the code", notes = "Delete the code")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "The code was deleted successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.DELETE, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void deleteCode(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "codeId",
      value = "The ID used to uniquely identify the code", required = true)
  @PathVariable String codeId)
      throws InvalidArgumentException, CodeNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtils.isEmpty(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    codesService.deleteCode(codeCategoryId, codeId);
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   */
  @ApiOperation(value = "Delete the code category", notes = "Delete the code category")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The code category was deleted successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void deleteCodeCategory(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    codesService.deleteCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the code
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @ApiOperation(value = "Retrieve the code", notes = "Retrieve the code")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public Code getCode(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category the code is associated with",
      required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "codeId",
      value = "The ID uniquely identifying the code", required = true)
  @PathVariable String codeId)
      throws InvalidArgumentException, CodeNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtils.isEmpty(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    return codesService.getCode(codeCategoryId, codeId);
  }

  /**
   * Retrieve the code categories.
   *
   * @return the code categories
   */
  @ApiOperation(value = "Retrieve the code categories", notes = "Retrieve the code categories")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public List<CodeCategory> getCodeCategories()
      throws CodesServiceException {
    return codesService.getCodeCategories();
  }

  /**
   * Retrieve the code category
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the code category
   */
  @ApiOperation(value = "Retrieve the code category", notes = "Retrieve the code category")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public CodeCategory getCodeCategory(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return codesService.getCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the XML or JSON data for a code category
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the XML or JSON data for the code category
   */
  @ApiOperation(value = "Retrieve the XML or JSON data for a code category",
      notes = "Retrieve the XML or JSON data for a code category")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/data", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public String getCodeCategoryData(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    String data = codesService.getCodeCategoryData(codeCategoryId);

    return RestUtil.quote(StringUtils.isEmpty(data)
        ? ""
        : data);
  }

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the name of the code category
   */
  @ApiOperation(value = "Retrieve the name of the code category",
      notes = "Retrieve the name of the code category")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/name", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public String getCodeCategoryName(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return RestUtil.quote(codesService.getCodeCategoryName(codeCategoryId));
  }

  /**
   * Retrieve the code category summaries.
   *
   * @return the code category summaries
   */
  @ApiOperation(value = "Retrieve the code category summaries",
      notes = "Retrieve the code category summaries")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-category-summaries", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public List<CodeCategorySummary> getCodeCategorySummaries()
      throws CodesServiceException {
    return codesService.getCodeCategorySummaries();
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the date and time the code category was last updated
   */
  @ApiOperation(value = "Retrieve the date and time the code category was last updated",
      notes = "Retrieve the date and time the code category was last updated")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/updated", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public String getCodeCategoryUpdated(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return RestUtil.quote(ISO8601Util.fromLocalDateTime(codesService.getCodeCategoryUpdated(
        codeCategoryId)));
  }

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @ApiOperation(value = "Retrieve the name of the code", notes = "Retrieve the name of the code")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes/{codeId}/name",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public String getCodeName(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category the code is associated with",
      required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "codeId",
      value = "The ID uniquely identifying the code", required = true)
  @PathVariable String codeId)
      throws InvalidArgumentException, CodeNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtils.isEmpty(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    return RestUtil.quote(codesService.getCodeName(codeCategoryId, codeId));
  }

  /**
   * Retrieve the codes for a code category
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the codes for the code category
   */
  @ApiOperation(value = "Retrieve the codes for a code category",
      notes = "Retrieve the codes for a code category")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public List<Code> getCodes(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return codesService.getCodes(codeCategoryId);
  }

  /**
   * Update the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param codeId         the ID used to uniquely identify the code
   * @param code           the code to create
   */
  @ApiOperation(value = "Update the code", notes = "Update the code")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "The code was updated successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.PUT, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void updateCode(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "codeId",
      value = "The ID used to uniquely identify the code", required = true)
  @PathVariable String codeId, @ApiParam(name = "code", value = "The code", required = true)
  @RequestBody Code code)
      throws InvalidArgumentException, CodeNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtils.isEmpty(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    if (code == null) {
      throw new InvalidArgumentException("code");
    }

    if (!code.getCodeCategoryId().equals(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!code.getId().equals(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    Set<ConstraintViolation<Code>> constraintViolations = validator.validate(code);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("code", ValidationError.toValidationErrors(
          constraintViolations));
    }

    codesService.updateCode(code);
  }

  /**
   * Update the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param codeCategory   the code category
   */
  @ApiOperation(value = "Update the code category", notes = "Update the code category")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The code category was updated successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The code category could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/code-categories/{codeCategoryId}", method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Codes.CodeAdministration')")
  public void updateCodeCategory(@ApiParam(name = "codeCategoryId",
      value = "The ID used to uniquely identify the code category", required = true)
  @PathVariable String codeCategoryId, @ApiParam(name = "codeCategory", value = "The code category",
      required = true)
  @RequestBody CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException {
    if (StringUtils.isEmpty(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (codeCategory == null) {
      throw new InvalidArgumentException("codeCategory");
    }

    if (!codeCategory.getId().equals(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    Set<ConstraintViolation<CodeCategory>> constraintViolations = validator.validate(codeCategory);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("codeCategory", ValidationError.toValidationErrors(
          constraintViolations));
    }

    codesService.updateCodeCategory(codeCategory);
  }
}

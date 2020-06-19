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

package digital.inception.error;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ErrorRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags = "Error API")
@RestController
@RequestMapping(value = "/api/error")
@SuppressWarnings({"unused"})
public class ErrorRestController extends SecureRestController {

  /**
   * The Error Service.
   */
  private IErrorService errorService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>ErrorRestController</code>.
   *
   * @param errorService the Error Service
   * @param validator    the JSR-303 validator
   */
  public ErrorRestController(IErrorService errorService, Validator validator) {
    this.errorService = errorService;
    this.validator = validator;
  }

  /**
   * Create the error report.
   *
   * @param errorReport the error report
   */
  @ApiOperation(value = "Create the error report", notes = "Create the error report")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The error report was created successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/error-reports", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createErrorReport(@ApiParam(name = "errorReport", value = "The error report",
      required = true)
  @RequestBody ErrorReport errorReport)
      throws InvalidArgumentException, ErrorServiceException {
    // Truncate the detail if required
    if ((errorReport.getDetail() != null)
        && (errorReport.getDetail().length() > ErrorReport.MAX_DETAIL_SIZE)) {
      errorReport.setDetail(errorReport.getDetail().substring(0, ErrorReport.MAX_DETAIL_SIZE - 3)
          + "...");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      errorReport.setWho(authentication.getPrincipal().toString());
    }

    if (errorReport == null) {
      throw new InvalidArgumentException("code");
    }

    Set<ConstraintViolation<ErrorReport>> constraintViolations = validator.validate(errorReport);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("errorReport", ValidationError.toValidationErrors(
          constraintViolations));
    }

    errorService.createErrorReport(errorReport);
  }
}

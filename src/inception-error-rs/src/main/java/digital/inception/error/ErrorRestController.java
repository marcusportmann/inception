/*
 * Copyright 2021 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.validation.InvalidArgumentException;
import digital.inception.rs.RestControllerError;
import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ErrorRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Error API")
@RestController
@RequestMapping(value = "/api/error")
@CrossOrigin
@SuppressWarnings({"unused"})
public class ErrorRestController extends SecureRestController {

  /** The Error Service. */
  private final IErrorService errorService;

  /**
   * Constructs a new <code>ErrorRestController</code>.
   *
   * @param errorService the Error Service
   */
  public ErrorRestController(IErrorService errorService) {
    this.errorService = errorService;
  }

  /**
   * Create the new error report.
   *
   * @param errorReport the error report
   */
  @Operation(summary = "Create the error report", description = "Create the error report")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The error report was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/error-reports",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createErrorReport(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The error report to create",
              required = true)
          @RequestBody
          ErrorReport errorReport)
      throws InvalidArgumentException, ErrorServiceException {
    if (errorReport == null) {
      throw new InvalidArgumentException("errorReport");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      errorReport.setWho(authentication.getPrincipal().toString());
    }

    errorService.createErrorReport(errorReport);
  }
}

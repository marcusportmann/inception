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

package demo.controller;

import demo.model.CarType;
import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>TestApiController</b> interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Test")
@RequestMapping(value = "/api/test")
public interface TestApiController {

  /**
   * Test an API call.
   *
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(summary = "Test an API call", description = "Test an API call")
  @RequestMapping(
      value = "/test-api-call",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  void testApiCall() throws ServiceUnavailableException;

  /**
   * Test the exception handling.
   *
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(summary = "Test the exception handling", description = "Test the exception handling")
  @RequestMapping(value = "/test-exception-handling", method = RequestMethod.GET)
  void testExceptionHandling() throws ServiceUnavailableException;

  /**
   * Test the offset date time mapping.
   *
   * @param offsetDateTime the offset date time
   * @return the offset date time
   */
  @Operation(
      summary = "Test the offset date time serialization",
      description = "Test the offset date time serialization")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @RequestMapping(
      value = "/test-offset-date-time",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  OffsetDateTime testOffsetDateTime(
      @Parameter(name = "offsetDateTime", description = "The offset date time", required = true)
          @RequestParam("offsetDateTime")
          OffsetDateTime offsetDateTime);

  /**
   * Test the Policy Decision Point authorization.
   *
   * @param pathVariable the path variable
   * @param anotherPathVariable another path variable
   * @param requestParameter the request parameter
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(
      summary = "Test the policy decision point authorization",
      description = "Test the policy decision point authorization")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The policy decision point authorization was successful"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = {"/test-pdp-authorization/{pathVariable}/{anotherPathVariable}"},
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("authorize()")
  void testPolicyDecisionPointAuthorization(
      @Parameter(name = "pathVariable", description = "The path variable", required = true)
          @PathVariable
          String pathVariable,
      @Parameter(
              name = "anotherPathVariable",
              description = "Another path variable",
              required = true)
          @PathVariable
          String anotherPathVariable,
      @Parameter(name = "requestParameter", description = "The request parameter", required = true)
          @RequestParam
          String requestParameter)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Test returning an enum.
   *
   * @return the car type enum value
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(summary = "Test returning an enum", description = "Test returning an enum")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/test-returning-enum",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  CarType testReturningEnum() throws ServiceUnavailableException;

  /**
   * Test returning a string.
   *
   * @param throwException should an exception be thrown
   * @return the string value
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(summary = "Test returning an string", description = "Test returning a string")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/test-returning-string",
      method = RequestMethod.GET,
      produces = "text/plain")
  String testReturningString(
      @Parameter(
              name = "throwException",
              description = "Throw an exception instead of returning a string",
              required = true)
          @RequestParam
          Boolean throwException)
      throws ServiceUnavailableException;

  /**
   * Test task execution.
   *
   * @param slowTask test the execution of a slow task
   * @throws ServiceUnavailableException if an error occurred
   */
  @Operation(summary = "Test task execution", description = "Test task execution")
  @RequestMapping(
      value = "/test-task-execution",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  void testTaskExecution(
      @Parameter(name = "slowTask", description = "Test the execution of a slow task") @RequestParam
          Boolean slowTask)
      throws ServiceUnavailableException;
}

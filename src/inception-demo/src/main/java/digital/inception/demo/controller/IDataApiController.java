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

package digital.inception.demo.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.demo.model.Data;
import digital.inception.demo.model.ReactiveData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;

/**
 * The <b>IDataApiController</b> interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Data")
@RequestMapping(value = "/api/data")
public interface IDataApiController {

  /**
   * Returns all the data.
   *
   * @return the data
   * @throws ServiceUnavailableException if the data could not be retrieved
   */
  @RequestMapping(
      value = "/all-data",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  List<Data> getAllData() throws ServiceUnavailableException;

  /**
   * Returns all the reactive data.
   *
   * @return the reactive data
   * @throws ServiceUnavailableException if the reactive data could not be retrieved
   */
  @RequestMapping(
      value = "/all-reactive-data",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_NDJSON_VALUE)
  // @PreAuthorize("hasAccess('GetAllReactiveData') or isSecurityDisabled() or
  // hasRole('Administrator')")
  @PreAuthorize("isSecurityDisabled() or hasRole('Administrator')")
  Flux<ReactiveData> getAllReactiveData() throws ServiceUnavailableException;

  /**
   * Retrieve the data.
   *
   * @return the data
   * @throws ServiceUnavailableException if the data could not be retrieved
   */
  @RequestMapping(
      value = "/data",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  Data getData() throws ServiceUnavailableException;

  /**
   * Process the data.
   *
   * @param data the data to process
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the data could not be processed
   */
  @Operation(summary = "Process the data", description = "Process the data")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "The data was processed successfully"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid argument",
              content =
              @Content(
                  mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetails.class))),
          @ApiResponse(
              responseCode = "403",
              description = "Access denied",
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
      value = "/process",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("authorize()")
  void process(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The data to process",
          required = true)
      @RequestBody
      Data data)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Validate the data.
   *
   * @param data the data to validate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the data could not be validated
   */
  @Operation(summary = "Validate the data", description = "Validate the data")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "The data was validated successfully"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid argument",
              content =
              @Content(
                  mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetails.class))),
          @ApiResponse(
              responseCode = "403",
              description = "Access denied",
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
      value = "/validate",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void validate(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The data to validate",
          required = true)
      @RequestBody
      Data data)
      throws InvalidArgumentException, ServiceUnavailableException;
}

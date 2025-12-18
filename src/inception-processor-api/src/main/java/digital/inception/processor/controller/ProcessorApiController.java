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

package digital.inception.processor.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.processor.ObjectProcessingTelemetry;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code ProcessorApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Processor")
@RequestMapping(value = "/api/processor")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface ProcessorApiController {

  /**
   * Retrieve the telemetry for the objects currently being processed.
   *
   * @return the config summaries
   * @throws ServiceUnavailableException if the telemetry for the objects currently being processed
   *     could not be retrieved
   */
  @Operation(
      summary = "Retrieve the telemetry for the objects currently being processed",
      description = "Retrieve the telemetry for the objects currently being processed")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The telemetry for the objects currently being processed was retrieved"),
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
      value = "/telemetry",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Processor.ProcessorAdministration')")
  List<ObjectProcessingTelemetry> getTelemetry() throws ServiceUnavailableException;
}

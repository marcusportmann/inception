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

package digital.inception.config.controller;

import digital.inception.config.exception.ConfigNotFoundException;
import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigSummary;
import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code ConfigApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Config")
@RequestMapping(value = "/api/config")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface ConfigApiController {

  /**
   * Delete the config.
   *
   * @param id the ID for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be deleted
   */
  @Operation(summary = "Delete the config", description = "Delete the config")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The config was deleted"),
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
            responseCode = "404",
            description = "The config could not be found",
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
      value = "/configs/{id}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  void deleteConfig(
      @Parameter(name = "id", description = "The ID for the config", required = true) @PathVariable
          String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the config.
   *
   * @param id the ID for the config
   * @return the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be retrieved
   */
  @Operation(summary = "Retrieve the config", description = "Retrieve the config")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The config was retrieved"),
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
            responseCode = "404",
            description = "The config could not be found",
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
      value = "/configs/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  Config getConfig(
      @Parameter(name = "id", description = "The ID for the config", required = true) @PathVariable
          String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the config summaries.
   *
   * @return the config summaries
   * @throws ServiceUnavailableException if the config summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve all the config summaries",
      description = "Retrieve all the config summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The config summaries were retrieved"),
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
      value = "/config-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  List<ConfigSummary> getConfigSummaries() throws ServiceUnavailableException;

  /**
   * Retrieve the config value.
   *
   * @param id the ID for the config
   * @return the config value
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config value could not be retrieved
   */
  @Operation(summary = "Retrieve the config value", description = "Retrieve the config value")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The config value was retrieved"),
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
            responseCode = "404",
            description = "The config could not be found",
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
      value = "/configs/{id}/value",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  String getConfigValue(
      @Parameter(name = "id", description = "The ID for the config", required = true) @PathVariable
          String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the configs.
   *
   * @return the configs
   * @throws ServiceUnavailableException if the configs could not be retrieved
   */
  @Operation(summary = "Retrieve all the configs", description = "Retrieve all the configs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The configs were retrieved"),
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
      value = "/configs",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  List<Config> getConfigs() throws ServiceUnavailableException;

  /**
   * Sets the config.
   *
   * @param config the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  @Operation(summary = "Set the config", description = "Set the config")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The config was set"),
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
      value = "/configs",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Config.ConfigAdministration')")
  void setConfig(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The config",
              required = true)
          @RequestBody
          Config config)
      throws InvalidArgumentException, ServiceUnavailableException;
}

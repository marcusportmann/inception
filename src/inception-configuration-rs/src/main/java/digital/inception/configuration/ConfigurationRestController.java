/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.configuration;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Configuration API")
@RestController
@RequestMapping(value = "/api/configuration")
@CrossOrigin
@SuppressWarnings({"unused"})
public class ConfigurationRestController extends SecureRestController {

  /** The Configuration Service. */
  private final IConfigurationService configurationService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <code>ConfigurationRestController</code>.
   *
   * @param configurationService the Configuration Service
   * @param validator the JSR-303 validator
   */
  public ConfigurationRestController(
      IConfigurationService configurationService, Validator validator) {
    this.configurationService = configurationService;
    this.validator = validator;
  }

  /**
   * Delete the configuration.
   *
   * @param key the key uniquely identifying the configuration
   */
  @Operation(summary = "Delete the configuration", description = "Delete the configuration")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The configuration was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The configuration could not be found",
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
      value = "/configurations/{key}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Configuration.ConfigurationAdministration')")
  public void deleteConfiguration(
      @Parameter(
              name = "key",
              description = "The key uniquely identifying the configuration",
              required = true)
          @PathVariable
          String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    if (StringUtils.isEmpty(key)) {
      throw new InvalidArgumentException("key");
    }

    configurationService.deleteConfiguration(key);
  }

  /**
   * Retrieve the configuration.
   *
   * @param key the key uniquely identifying the configuration
   * @return the configuration
   */
  @Operation(summary = "Retrieve the configuration", description = "Retrieve the configuration")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The configuration could not be found",
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
      value = "/configurations/{key}",
      method = RequestMethod.GET,
      produces = "application/json")
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Configuration.ConfigurationAdministration')")
  public Configuration getConfiguration(
      @Parameter(
              name = "key",
              description = "The key uniquely identifying the configuration",
              required = true)
          @PathVariable
          String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    if (StringUtils.isEmpty(key)) {
      throw new InvalidArgumentException("key");
    }

    return configurationService.getConfiguration(key);
  }

  /**
   * Retrieve the configuration value.
   *
   * @param key the key uniquely identifying the configuration
   * @return the configuration value
   */
  @Operation(
      summary = "Retrieve the configuration value",
      description = "Retrieve the configuration value")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The configuration could not be found",
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
      value = "/configurations/{key}/value",
      method = RequestMethod.GET,
      produces = "application/json")
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Configuration.ConfigurationAdministration')")
  public String getConfigurationValue(
      @Parameter(
              name = "key",
              description = "The key uniquely identifying the configuration",
              required = true)
          @PathVariable
          String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    if (StringUtils.isEmpty(key)) {
      throw new InvalidArgumentException("key");
    }

    return RestUtil.quote(configurationService.getString(key));
  }

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  @Operation(
      summary = "Retrieve all the configurations",
      description = "Retrieve all the configurations")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
      value = "/configurations",
      method = RequestMethod.GET,
      produces = "application/json")
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Configuration.ConfigurationAdministration')")
  public List<Configuration> getConfigurations() throws ConfigurationServiceException {
    return configurationService.getConfigurations();
  }

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  @Operation(summary = "Set the configuration", description = "Set the configuration")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The configuration was set successfully"),
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
      value = "/configurations",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Configuration.ConfigurationAdministration')")
  public void setConfiguration(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The configuration",
              required = true)
          @RequestBody
          Configuration configuration)
      throws InvalidArgumentException, ConfigurationServiceException {
    Set<ConstraintViolation<Configuration>> constraintViolations =
        validator.validate(configuration);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "configuration", ValidationError.toValidationErrors(constraintViolations));
    }

    configurationService.setConfiguration(configuration);
  }
}

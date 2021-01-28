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

package demo.rs;

import demo.model.DataServiceException;
import digital.inception.sms.smsportal.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * The <b>TestRestController</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Test API")
@RestController
@RequestMapping(value = "/api/test")
@CrossOrigin
@SuppressWarnings({"unused"})
public class TestRestController {

  private final WebClient.Builder webClientBuilder;

  /**
   * Constructs a new <b>TestRestController</b>.
   *
   * @param webClientBuilder the web client builder
   */
  public TestRestController(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  /** Test the exception handling. */
  @RequestMapping(value = "/test-exception-handling", method = RequestMethod.GET)
  public void testExceptionHandling() throws DataServiceException {
    throw new DataServiceException("Testing 1.. 2.. 3..");
  }

  /**
   * Test the local date time mapping.
   *
   * @param localDateTime the local date time
   */
  @Operation(
      summary = "Test the local date time serialization",
      description = "Test the local date time serialization")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @RequestMapping(
      value = "/test-local-date-time",
      method = RequestMethod.GET,
      produces = "application/json")
  public LocalDateTime testLocalDateTime(
      @Parameter(name = "localDateTime", description = "The local date time", required = true)
          @RequestParam("localDateTime")
          LocalDateTime localDateTime)
      throws DataServiceException {
    System.out.println("localDateTime = " + localDateTime);

    return localDateTime;
  }

  @RequestMapping(
      value = "/test-web-client",
      method = RequestMethod.GET,
      produces = "application/json")
  public AuthenticationResponse testWebClient() throws DataServiceException {
    try {
      WebClient webClient =
          webClientBuilder
              .baseUrl("https://rest.smsportal.com/v1")
              .defaultHeaders(header -> header.setBasicAuth("", ""))
              .build();

      Mono<AuthenticationResponse> response =
          webClient
              .get()
              .uri("/Authentication")
              .retrieve()
              .bodyToFlux(AuthenticationResponse.class)
              .single();

      AuthenticationResponse authenticationResponse = response.block();

      System.out.println("Token: " + authenticationResponse.getToken());

      return authenticationResponse;
    } catch (Throwable e) {
      throw new DataServiceException("Failed to test the web client", e);
    }
  }

  /**
   * Test the zoned date time mapping.
   *
   * @param zonedDateTime the zoned date time
   */
  @Operation(
      summary = "Test the zoned date time serialization",
      description = "Test the zoned date time serialization")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @RequestMapping(
      value = "/test-zoned-date-time",
      method = RequestMethod.GET,
      produces = "application/json")
  public ZonedDateTime testZonedDateTime(
      @Parameter(name = "zonedDateTime", description = "The zoned date time", required = true)
          @RequestParam("zonedDateTime")
          ZonedDateTime zonedDateTime)
      throws DataServiceException {
    System.out.println("zonedDateTime = " + zonedDateTime);

    return zonedDateTime;
  }
}

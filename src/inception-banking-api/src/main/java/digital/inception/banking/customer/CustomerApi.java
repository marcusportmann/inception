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

package digital.inception.banking.customer;

import digital.inception.api.ProblemDetails;
import digital.inception.api.SecureApi;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>CustomerApi</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Customer API")
@RestController
@RequestMapping(value = "/api/customers")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class CustomerApi extends SecureApi {

  /** The Customer Service. */
  private final ICustomerService customerService;

  /**
   * Constructs a new <b>CustomerRestController</b>.
   *
   * @param customerService the Customer Service
   */
  public CustomerApi(ICustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  @Operation(
      summary = "Create the individual customer",
      description = "Create the individual customer")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The individual customer was created successfully"),
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
            responseCode = "409",
            description = "A individual customer with the specified ID already exists",
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
      value = "/individuals",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  //  @PreAuthorize(
  //      "hasRole('Administrator') or hasAuthority('FUNCTION_Banking.BankingAdministration') or
  // hasAuthority('FUNCTION_Banking.CustomerAdministration')")
  public void createIndividualCustomer(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The individual customer to create",
              required = true)
          @RequestBody
          IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          ServiceUnavailableException {
    customerService.createIndividualCustomer(individualCustomer);
  }
}

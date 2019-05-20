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

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.validation.InvalidArgumentException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

import javax.validation.Validator;

/**
 * The <code>SecurityRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api/security")
@SuppressWarnings({ "unused" })
public class SecurityRestController
{
  /**
   * The Security Service.
   */
  private ISecurityService securityService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>SecurityRestController</code>.
   *
   * @param securityService the Security Service
   * @param validator       the JSR-303 validator
   */
  public SecurityRestController(ISecurityService securityService, Validator validator)
  {
    this.securityService = securityService;
    this.validator = validator;
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   */
  @ApiOperation(value = "Delete the organization", notes = "Delete the organization")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The organization was deleted successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The organization could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations/{organizationId}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('Application.OrganizationAdministration')")
  public void deleteOrganization(@ApiParam(name = "organizationId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the organization",
      required = true)
  @PathVariable UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException
  {
    if (organizationId == null)
    {
      throw new InvalidArgumentException("organizationId");
    }

    securityService.deleteOrganization(organizationId);
  }

  /**
   * Retrieve the filtered organizations using pagination.
   *
   * @param filter        the optional filter to apply to the organization name
   * @param sortDirection the optional sort direction to apply to the organization name
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the organizations
   */
  @ApiOperation(value = "Retrieve the filtered organizations using pagination",
      notes = "Retrieve the filtered organizations using pagination")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)

  // @PreAuthorize("hasAuthority('Application.OrganizationAdministration')")
  public ResponseEntity<List<Organization>> getOrganizations(@ApiParam(name = "filter",
      value = "The optional filter to apply to the organization name")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortDirection",
      value = "The optional sort direction to apply to the organization name")
  @RequestParam(value = "sortDirection", required = false) SortDirection sortDirection, @ApiParam(
      name = "pageIndex",
      value = "The optional page index")
  @RequestParam(value = "pageIndex", required = false) Integer pageIndex, @ApiParam(
      name = "pageSize",
      value = "The optional page size")
  @RequestParam(value = "pageSize", required = false) Integer pageSize)
    throws SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    var httpHeaders = new HttpHeaders();
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfOrganizations()));

    return new ResponseEntity<>(securityService.getFilteredOrganizations(filter, sortDirection,
        pageIndex, pageSize), httpHeaders, HttpStatus.OK);
  }
}

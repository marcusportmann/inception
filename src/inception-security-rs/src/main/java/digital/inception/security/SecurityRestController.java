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

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
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
@SuppressWarnings({ "unused", "WeakerAccess" })
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
   * Retrieve the organizations.
   *
   * @return the organizations
   */
  @ApiOperation(value = "Retrieve the organizations", notes = "Retrieve the organizations")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('Application.OrganizationAdministration')")
  public List<Organization> getOrganizations()
    throws SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return securityService.getOrganizations();
  }
}

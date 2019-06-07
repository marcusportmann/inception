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
import digital.inception.validation.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

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
   * Create the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  @ApiOperation(value = "Create the user", notes = "Create the user")
  @ApiResponses(value = { @ApiResponse(code = 204,
    message = "The user was created successfully") ,
    @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
    @ApiResponse(code = 404, message = "The user directory could not be found", response = RestControllerError.class) ,
    @ApiResponse(code = 409, message = "A user with the specified username already exists",
      response = RestControllerError.class) ,
    @ApiResponse(code = 500,
      message = "An error has occurred and the service is unable to process the request at this time",
      response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories/{userDirectoryId}/users", method = RequestMethod.POST,
    produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('Security.UserAdministration')")
  public void createUser(
    @ApiParam(name = "userDirectoryId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory", required = true)
    @PathVariable UUID userDirectoryId,

    @ApiParam(name = "user", value = "The user",
    required = true)
  @RequestBody User user,
    @ApiParam(
      name = "expiredPassword",
      value = "Create the user with its password expired")
    @RequestParam(value = "expiredPassword", required = false) Boolean expiredPassword,

    @ApiParam(
      name = "userLocked",
      value = "Create the user locked")
    @RequestParam(value = "userLocked", required = false) Boolean userLocked

    )
    throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateUserException, SecurityServiceException
  {
    if (userDirectoryId == null)
    {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (user == null)
    {
      throw new InvalidArgumentException("user");
    }

    checkAccessToUserDirectory(SecurityContextHolder.getContext().getAuthentication(), userDirectoryId);

    Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("user", ValidationError.toValidationErrors(
        constraintViolations));
    }

    securityService.createUser(userDirectoryId, user, (expiredPassword != null) && expiredPassword, (userLocked != null) && userLocked );
  }












  /**
   * Create the organization.
   *
   * @param organization the organization to create
   */
  @ApiOperation(value = "Create the organization", notes = "Create the organization")
  @ApiResponses(value = { @ApiResponse(code = 204,
    message = "The organization was created successfully") ,
    @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
    @ApiResponse(code = 409, message = "An organization with the specified ID or name already exists",
      response = RestControllerError.class) ,
    @ApiResponse(code = 500,
      message = "An error has occurred and the service is unable to process the request at this time",
      response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.POST,
    produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('Security.OrganizationAdministration')")
  public void createOrganization(@ApiParam(name = "organization", value = "The organization",
    required = true)
  @RequestBody Organization organization,
    @ApiParam(
      name = "createUserDirectory",
      value = "Should a new internal user directory be created for the organization")
    @RequestParam(value = "createUserDirectory", required = false) Boolean createUserDirectory)
    throws InvalidArgumentException, DuplicateOrganizationException, SecurityServiceException
  {
    if (organization == null)
    {
      throw new InvalidArgumentException("organization");
    }

    Set<ConstraintViolation<Organization>> constraintViolations = validator.validate(organization);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("organization", ValidationError.toValidationErrors(
        constraintViolations));
    }

    securityService.createOrganization(organization,
      (createUserDirectory != null) && createUserDirectory);
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
  @PreAuthorize("hasAuthority('Security.OrganizationAdministration')")
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
   * @param filter        the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the organizations
   */
  @ApiOperation(value = "Retrieve the organizations",
      notes = "Retrieve the organizations")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('Security.OrganizationAdministration')")
  public ResponseEntity<List<Organization>> getOrganizations(@ApiParam(name = "filter",
      value = "The optional filter to apply to the organizations")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortDirection",
      value = "The optional sort direction to apply to the organizations")
  @RequestParam(value = "sortDirection", required = false) SortDirection sortDirection, @ApiParam(
      name = "pageIndex",
      value = "The optional page index", example = "0")
  @RequestParam(value = "pageIndex", required = false) Integer pageIndex, @ApiParam(
      name = "pageSize",
      value = "The optional page size", example = "0")
  @RequestParam(value = "pageSize", required = false) Integer pageSize)
    throws SecurityServiceException
  {
    var httpHeaders = new HttpHeaders();
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfOrganizations()));

    return new ResponseEntity<>(securityService.getOrganizations(filter, sortDirection, pageIndex,
        pageSize), httpHeaders, HttpStatus.OK);
  }



  /**
   * Retrieve the user directories the organization is associated with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the user directories the organization is associated with
   */
  @ApiOperation(value = "Retrieve the user directories the organization is associated with",
    notes = "Retrieve the user directories the organization is associated with")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
    @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
    @ApiResponse(code = 404, message = "The organization could not be found",
      response = RestControllerError.class) ,


    @ApiResponse(code = 500,
      message = "An error has occurred and the service is unable to process the request at this time",
      response = RestControllerError.class) })
  @RequestMapping(value = "/organizations/{organizationId}/user-directories", method = RequestMethod.GET,
    produces = "application/json")
  @ResponseStatus(HttpStatus.OK)

  // TODO: HAS ROLE AdMINISTRATOR OR ORGANISATION ADMIN

  //@PreAuthorize("hasAuthority('Security.OrganizationAdministration') or hasAuthority('Security.UserAdministration')")
  public ResponseEntity<List<UserDirectory>> getUserDirectoriesForOrganization(@ApiParam(name = "organizationId",
    value = "The Universally Unique Identifier (UUID) used to uniquely identify the organization",
    required = true)
  @PathVariable UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException
  {
    if (organizationId == null)
    {
      throw new InvalidArgumentException("organizationId");
    }

    // TODO: CHECK ACCESS TO ORGANISATIONx

    List<UserDirectory> userDirectories = securityService.getUserDirectoriesForOrganization(organizationId, isAdministrator(SecurityContextHolder.getContext().getAuthentication()));

    return new ResponseEntity<>(userDirectories, HttpStatus.OK);
  }








  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the optional filter to apply to the users
   * @param sortDirection   the optional sort direction to apply to the users
   * @param pageIndex       the optional page index
   * @param pageSize        the optional page size
   *
   * @return the users
   */
  @ApiOperation(value = "Retrieve the users",
    notes = "Retrieve the users")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
    @ApiResponse(code = 404, message = "The user directory could not be found", response = RestControllerError.class) ,
    @ApiResponse(code = 500,
      message = "An error has occurred and the service is unable to process the request at this time",
      response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories/{userDirectoryId}/users", method = RequestMethod.GET,
    produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('Security.UserAdministration')")
  public ResponseEntity<List<User>> getUsers(

    @ApiParam(name = "userDirectoryId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory", required = true)
    @PathVariable UUID userDirectoryId,



    @ApiParam(name = "filter",
    value = "The optional filter to apply to the users")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortDirection",
    value = "The optional sort direction to apply to the users")
  @RequestParam(value = "sortDirection", required = false) SortDirection sortDirection, @ApiParam(
    name = "pageIndex",
    value = "The optional page index", example = "0")
  @RequestParam(value = "pageIndex", required = false) Integer pageIndex, @ApiParam(
    name = "pageSize",
    value = "The optional page size", example = "0")
  @RequestParam(value = "pageSize", required = false) Integer pageSize)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    checkAccessToUserDirectory(SecurityContextHolder.getContext().getAuthentication(), userDirectoryId);


    var httpHeaders = new HttpHeaders();
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfUsers(userDirectoryId)));

    return new ResponseEntity<>(securityService.getUsers(userDirectoryId, filter, sortDirection, pageIndex,
      pageSize), httpHeaders, HttpStatus.OK);
  }

  /**
   * Confirm that the user associated with the authenticated request is an administrator.
   *
   * @param authentication  the authenticated principal
   *
   * @return <code>true</code> if the user associated with the authenticated request is an
   *         administrator or <code>false</code> otherwise.
   */
  private boolean isAdministrator(Authentication authentication)
  {
    if (!authentication.isAuthenticated())
    {
      return false;


      // TODO: CHECK RETURN OF THIS METHOD
    }


    return false;
  }

  /**
   * Confirm that the user associated with the authenticated request has access to the user
   * directory.
   *
   * @param authentication  the authenticated principal
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  private void checkAccessToUserDirectory(Authentication authentication, UUID userDirectoryId)
  {
    if (!authentication.isAuthenticated())
    {
      throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId + ")");
    }

    for (GrantedAuthority authority : authentication.getAuthorities())
    {
      if (authority.getAuthority().startsWith("USER_DIRECTORY_ID_"))
      {
        UUID userDirectoryIdAuthority = UUID.fromString(authority.getAuthority().substring("USER_DIRECTORY_ID_".length()));

        if (!userDirectoryIdAuthority.equals(userDirectoryId))
        {
          throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId + ")");
        }
      }
    }
  }
}

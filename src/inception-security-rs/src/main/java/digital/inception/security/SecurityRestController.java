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
import digital.inception.rs.SecureRestController;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * The <code>SecurityRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api/security")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SecurityRestController extends SecureRestController
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
   * Create the new organization.
   *
   * @param organization        the organization
   * @param createUserDirectory should a new internal user directory be created for the organization
   */
  @ApiOperation(value = "Create the organization", notes = "Create the organization")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The organization was created successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 409,
          message = "An organization with the specified ID or name already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.OrganizationAdministration')")
  public void createOrganization(@ApiParam(name = "organization", value = "The organization",
      required = true)
  @RequestBody Organization organization, @ApiParam(name = "createUserDirectory",
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

    securityService.createOrganization(organization, (createUserDirectory != null)
        && createUserDirectory);
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
  @ApiResponses(value = { @ApiResponse(code = 204, message = "The user was created successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The user directory could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 409, message = "A user with the specified username already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories/{userDirectoryId}/users", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Security.UserAdministration')")
  public void createUser(@ApiParam(name = "userDirectoryId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory",
      required = true)
  @PathVariable UUID userDirectoryId, @ApiParam(name = "user", value = "The user", required = true)
  @RequestBody User user, @ApiParam(name = "expiredPassword",
      value = "Create the user with its password expired")
  @RequestParam(value = "expiredPassword", required = false) Boolean expiredPassword, @ApiParam(
      name = "userLocked",
      value = "Create the user locked")
  @RequestParam(value = "userLocked", required = false) Boolean userLocked)
    throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateUserException,
        SecurityServiceException
  {
    if (userDirectoryId == null)
    {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (user == null)
    {
      throw new InvalidArgumentException("user");
    }

    if (!hasAccessToUserDirectory(SecurityContextHolder.getContext().getAuthentication(),
        userDirectoryId))
    {
      throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId
          + ")");
    }

    Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("user", ValidationError.toValidationErrors(
          constraintViolations));
    }

    securityService.createUser(userDirectoryId, user, (expiredPassword != null)
        && expiredPassword, (userLocked != null) && userLocked);
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
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.OrganizationAdministration')")
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
  @ApiOperation(value = "Retrieve the organizations", notes = "Retrieve the organizations")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.OrganizationAdministration')")
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
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfOrganizations(
        filter)));

    return new ResponseEntity<>(securityService.getOrganizations(filter, sortDirection, pageIndex,
        pageSize), httpHeaders, HttpStatus.OK);
  }

  /**
   * Retrieve the organizations the user directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organizations the user directory is associated with
   */
  @ApiOperation(value = "Retrieve the organizations the user directory is associated with",
      notes = "Retrieve the organizations the user directory is associated with")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The user directory could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories/{userDirectoryId}/organizations",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<Organization>> getOrganizationsForUserDirectory(@ApiParam(
      name = "userDirectoryId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory",
      required = true)
  @PathVariable UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (userDirectoryId == null)
    {
      throw new InvalidArgumentException("organizationId");
    }

    if (!authentication.isAuthenticated())
    {
      throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId
          + ")");
    }

    if (!hasAccessToUserDirectory(authentication, userDirectoryId))
    {
      throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId
          + ")");
    }

    List<Organization> organizations = securityService.getOrganizationsForUserDirectory(
        userDirectoryId);

    return new ResponseEntity<>(organizations, HttpStatus.OK);
  }

  /**
   * Retrieve the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the user directories
   */
  @ApiOperation(value = "Retrieve the user directories", notes = "Retrieve the user directories")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.UserDirectoryAdministration')")
  public ResponseEntity<List<UserDirectory>> getUserDirectories(@ApiParam(name = "filter",
      value = "The optional filter to apply to the user directories")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortDirection",
      value = "The optional sort direction to apply to the user directories")
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
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfUserDirectories(
        filter)));

    return new ResponseEntity<>(securityService.getUserDirectories(filter, sortDirection,
        pageIndex, pageSize), httpHeaders, HttpStatus.OK);
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
  @RequestMapping(value = "/organizations/{organizationId}/user-directories",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.OrganizationAdministration') or hasAuthority('FUNCTION_Security.UserAdministration') or hasAuthority('FUNCTION_Security.GroupAdministration')")
  public ResponseEntity<List<UserDirectory>> getUserDirectoriesForOrganization(@ApiParam(
      name = "organizationId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the organization",
      required = true)
  @PathVariable UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (organizationId == null)
    {
      throw new InvalidArgumentException("organizationId");
    }

    List<UserDirectory> userDirectories = securityService.getUserDirectoriesForOrganization(
        organizationId);

    List<UserDirectory> filteredUserDirectories = new ArrayList<>();

    for (UserDirectory userDirectory : userDirectories)
    {
      if (hasAccessToUserDirectory(authentication, userDirectory.getId()))
      {
        filteredUserDirectories.add(userDirectory);
      }
    }

    return new ResponseEntity<>(filteredUserDirectories, HttpStatus.OK);
  }

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the summaries for the user directories
   */
  @ApiOperation(value = "Retrieve the summaries for the user directories",
      notes = "Retrieve the summaries for the user directories")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/user-directory-summaries", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.UserDirectoryAdministration')")
  public ResponseEntity<List<UserDirectorySummary>> getUserDirectorySummaries(@ApiParam(
      name = "filter",
      value = "The optional filter to apply to the user directories")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortDirection",
      value = "The optional sort direction to apply to the user directories")
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
    httpHeaders.add("x-total-count", String.valueOf(securityService.getNumberOfUserDirectories(
        filter)));

    return new ResponseEntity<>(securityService.getUserDirectorySummaries(filter, sortDirection,
        pageIndex, pageSize), httpHeaders, HttpStatus.OK);
  }

  /**
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the summaries for the user directories the organization is associated with
   */
  @ApiOperation(
      value = "Retrieve the summaries for the user directories the organization is associated with",
      notes = "Retrieve the summaries for the user directories the organization is associated with")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The organization could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/organizations/{organizationId}/user-directory-summaries",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Security.OrganizationAdministration') or hasAuthority('FUNCTION_Security.ResetUserPassword') or hasAuthority('FUNCTION_Security.UserAdministration') or hasAuthority('FUNCTION_Security.UserGroups')")
  public ResponseEntity<List<UserDirectorySummary>> getUserDirectorySummariesForOrganization(
      @ApiParam(name = "organizationId",
          value = "The Universally Unique Identifier (UUID) used to uniquely identify the organization",
          required = true)
  @PathVariable UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (organizationId == null)
    {
      throw new InvalidArgumentException("organizationId");
    }

    List<UserDirectorySummary> userDirectorySummaries =
        securityService.getUserDirectorySummariesForOrganization(organizationId);

    List<UserDirectorySummary> filteredUserDirectorySummaries = new ArrayList<>();

    for (UserDirectorySummary userDirectorySummary : userDirectorySummaries)
    {
      if (hasAccessToUserDirectory(authentication, userDirectorySummary.getId()))
      {
        filteredUserDirectorySummaries.add(userDirectorySummary);
      }
    }

    return new ResponseEntity<>(filteredUserDirectorySummaries, HttpStatus.OK);
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the optional filter to apply to the users
   * @param sortBy          The optional method used to sort the users e.g. by last name.
   * @param sortDirection   the optional sort direction to apply to the users
   * @param pageIndex       the optional page index
   * @param pageSize        the optional page size
   *
   * @return the users
   */
  @ApiOperation(value = "Retrieve the users", notes = "Retrieve the users")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The user directory could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/user-directories/{userDirectoryId}/users", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('Administrator') or hasAuthority('FUNCTION_Security.UserAdministration')")
  public ResponseEntity<List<User>> getUsers(@ApiParam(name = "userDirectoryId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory",
      required = true)
  @PathVariable UUID userDirectoryId, @ApiParam(name = "filter",
      value = "The optional filter to apply to the users")
  @RequestParam(value = "filter", required = false) String filter, @ApiParam(name = "sortBy",
      value = "The optional method used to sort the users e.g. by last name")
  @RequestParam(value = "sortBy", required = false) UserSortBy sortBy, @ApiParam(
      name = "sortDirection",
      value = "The optional sort direction to apply to the users")
  @RequestParam(value = "sortDirection", required = false) SortDirection sortDirection, @ApiParam(
      name = "pageIndex",
      value = "The optional page index", example = "0")
  @RequestParam(value = "pageIndex", required = false) Integer pageIndex, @ApiParam(
      name = "pageSize",
      value = "The optional page size", example = "0")
  @RequestParam(value = "pageSize", required = false) Integer pageSize)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (userDirectoryId == null)
    {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(authentication, userDirectoryId))
    {
      throw new AccessDeniedException("Access denied to the user directory (" + userDirectoryId
          + ")");
    }

    var users = securityService.getUsers(userDirectoryId, filter, sortBy, sortDirection, pageIndex,
        pageSize);

    var numberOfUsers = securityService.getNumberOfUsers(userDirectoryId, filter);

    var httpHeaders = new HttpHeaders();
    httpHeaders.add("x-total-count", String.valueOf(numberOfUsers));

    return new ResponseEntity<>(users, httpHeaders, HttpStatus.OK);
  }

  /**
   * Confirm that the user associated with the authenticated request has access to the user
   * directory.
   *
   * @param authentication  the authenticated principal
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user associated with the authenticated request has access to
   *         the user directory or <code>false</code> otherwise
   */
  protected boolean hasAccessToUserDirectory(Authentication authentication, UUID userDirectoryId)
    throws AccessDeniedException
  {
    // If the user is not authenticated then they cannot have access
    if (!authentication.isAuthenticated())
    {
      return false;
    }

    // If the user has the "Administrator" role they always have access
    if (hasRole(authentication, SecurityService.ADMINISTRATOR_ROLE_NAME))
    {
      return true;
    }

    // If the user is directly associated with the user directory then they have access
    String userDirectoryIdAuthorityValue = getValueForAuthorityWithPrefix(authentication,
        "USER_DIRECTORY_ID_");

    if (StringUtils.isEmpty(userDirectoryIdAuthorityValue))
    {
      return false;
    }
    else if (userDirectoryId.equals(UUID.fromString(userDirectoryIdAuthorityValue)))
    {
      return true;
    }

    /*
     * If the user is associated with a particular user directory, and that user directory is
     * associated with a particular organization, and that organization is associated with another
     * user directory, then the user has the same access to this other user directory.
     */
    try
    {
      var organizationIds = securityService.getOrganizationIdsForUserDirectory(UUID.fromString(
          userDirectoryIdAuthorityValue));

      for (UUID organizationId : organizationIds)
      {
        var userDirectoryIdsForOrganization = securityService.getUserDirectoryIdsForOrganization(
            organizationId);

        for (UUID userDirectoryIdForOrganization : userDirectoryIdsForOrganization)
        {
          if (userDirectoryIdForOrganization.equals(userDirectoryId))
          {
            return true;
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new AccessDeniedException("Failed to check whether the user ("
          + authentication.getPrincipal() + ") has access to the user directory ("
          + userDirectoryId + ")", e);
    }

    return false;
  }
}

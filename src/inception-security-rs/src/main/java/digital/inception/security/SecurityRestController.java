/*
 * Copyright 2018 Marcus Portmann
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SecurityRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SecurityRestController
{
  /* Scheduler Service */
  @Autowired
  private ISecurityService securityService;

  /* Validator */
  @Autowired
  private Validator validator;



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
  @RequestMapping(value = "/api/organizations", method = RequestMethod.GET,
    produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('Application.OrganizationAdministrationx')")
  public List<Organization> getOrganizations()
    throws SecurityServiceException
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return securityService.getOrganizations();
  }


}

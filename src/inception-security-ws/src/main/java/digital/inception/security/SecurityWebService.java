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

import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>SecurityWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "SecurityService", name = "ISecurityService",
    targetNamespace = "http://security.inception.digital")
@SOAPBinding
@SuppressWarnings({ "unused", "ValidExternallyBoundObject" })
public class SecurityWebService
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
   * Constructs a new <code>SecurityWebService</code>.
   *
   * @param securityService the Security Service
   * @param validator       the JSR-303 validator
   */
  public SecurityWebService(ISecurityService securityService, Validator validator)
  {
    this.securityService = securityService;
    this.validator = validator;
  }

  /**
   * Create the organization.
   *
   * @param organization        the organization to create
   * @param createUserDirectory should a new internal user directory be created for the organization
   */
  @WebMethod(operationName = "CreateOrganization")
  public void createOrganization(@WebParam(name = "Organization")
  @XmlElement(required = true) Organization organization, @WebParam(name = "CreateUserDirectory")
  @XmlElement(required = true) Boolean createUserDirectory)
    throws InvalidArgumentException, DuplicateOrganizationException, SecurityServiceException
  {
    validateOrganization(organization);

    securityService.createOrganization(organization,
      (createUserDirectory != null) && createUserDirectory);
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   */
  @WebMethod(operationName = "DeleteOrganization")
  public void deleteOrganization(@WebParam(name = "OrganizationId")
  @XmlElement(required = true) UUID organizationId)
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
   * @param filter        the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the organizations
   */
  @WebMethod(operationName = "GetOrganizations")
  @WebResult(name = "Organization")
  public List<Organization> getOrganizations(@WebParam(name = "Filter")
  @XmlElement(required = true) String filter, @WebParam(name = "SortDirection")
  @XmlElement(required = true) SortDirection sortDirection, @WebParam(name = "PageIndex")
  @XmlElement(required = true) Integer pageIndex, @WebParam(name = "PageSize")
  @XmlElement(required = true) Integer pageSize)
    throws SecurityServiceException
  {
    return securityService.getOrganizations(filter, sortDirection, pageIndex, pageSize);
  }

  private void validateOrganization(Organization organization)
    throws InvalidArgumentException
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
  }
}

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

package digital.inception.party;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.xml.LocalDateAdapter;
import java.time.LocalDate;
import java.util.UUID;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>PartyWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyService",
    name = "IPartyService",
    targetNamespace = "http://inception.digital/party")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyWebService {

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <b>PartyWebService</b>.
   *
   * @param partyService the Party Service
   */
  public PartyWebService(IPartyService partyService) {
    this.partyService = partyService;
  }

  /**
   * Create the new organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   */
  @WebMethod(operationName = "CreateOrganization")
  public void createOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Organization") @XmlElement(required = true) Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException {
    partyService.createOrganization(tenantId, organization);
  }

  /**
   * Create the new person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   */
  @WebMethod(operationName = "CreatePerson")
  public void createPerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Person") @XmlElement(required = true) Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    partyService.createPerson(tenantId, person);
  }

  /**
   * Delete the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  @WebMethod(operationName = "DeleteOrganization")
  public void deleteOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "OrganizationId") @XmlElement(required = true) UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    partyService.deleteOrganization(tenantId, organizationId);
  }

  /**
   * Delete the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  @WebMethod(operationName = "DeletePerson")
  public void deletePerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PersonId") @XmlElement(required = true) UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    partyService.deletePerson(tenantId, personId);
  }

  /**
   * Retrieve the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  @WebMethod(operationName = "GetOrganization")
  public Organization getOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "OrganizationId") @XmlElement(required = true) UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    return partyService.getOrganization(tenantId, organizationId);
  }

  /**
   * Retrieve the organizations.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the organizations
   * @param sortBy the optional method used to sort the organizations e.g. by name
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  @WebMethod(operationName = "GetOrganizations")
  public Organizations getOrganizations(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement OrganizationSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyService.getOrganizations(
        tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the parties.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  @WebMethod(operationName = "GetParties")
  public Parties getParties(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyService.getParties(tenantId, filter, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  @WebMethod(operationName = "GetParty")
  public Party getParty(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PartyId") @XmlElement(required = true) UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    return partyService.getParty(tenantId, partyId);
  }

  /**
   * Retrieve the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  @WebMethod(operationName = "GetPerson")
  public Person getPerson(
      @WebParam(name = "TenantId") @XmlElement UUID tenantId,
      @WebParam(name = "PersonId") @XmlElement UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    return partyService.getPerson(tenantId, personId);
  }

  /**
   * Retrieve the persons.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the persons
   * @param sortBy the optional method used to sort the persons e.g. by name
   * @param sortDirection the optional sort direction to apply to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the persons
   */
  @WebMethod(operationName = "GetPersons")
  public Persons getPersons(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement PersonSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyService.getPersons(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the snapshots for the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param entityType the type of entity
   * @param entityId the Universally Unique Identifier (UUID) for the entity
   * @param from the optional date to retrieve the snapshots from
   * @param to the optional date to retrieve the snapshots to
   * @param sortDirection the optional sort direction to apply to the snapshots
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the snapshots
   */
  @WebMethod(operationName = "GetSnapshots")
  public Snapshots getSnapshots(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "EntityType") @XmlElement(required = true) EntityType entityType,
      @WebParam(name = "EntityId") @XmlElement(required = true) UUID entityId,
      @WebParam(name = "From") @XmlElement @XmlJavaTypeAdapter(LocalDateAdapter.class)
          LocalDate from,
      @WebParam(name = "To") @XmlElement @XmlJavaTypeAdapter(LocalDateAdapter.class) LocalDate to,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    return partyService.getSnapshots(
        tenantId, entityType, entityId, from, to, sortDirection, pageIndex, pageSize);
  }

  /**
   * Update the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   */
  @WebMethod(operationName = "UpdateOrganization")
  public void updateOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Organization") @XmlElement(required = true) Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    partyService.updateOrganization(tenantId, organization);
  }

  /**
   * Update the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   */
  @WebMethod(operationName = "UpdatePerson")
  public void updatePerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Person") @XmlElement(required = true) Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    partyService.updatePerson(tenantId, person);
  }
}

/*
 * Copyright Marcus Portmann
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

package digital.inception.party.ws;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.party.model.*;
import digital.inception.party.service.PartyService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.UUID;

/**
 * The <b>PartyWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyService",
    name = "IPartyService",
    targetNamespace = "https://inception.digital/party")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyWebService {

  /** The Party Service. */
  private final PartyService partyService;

  /**
   * Constructs a new <b>PartyWebService</b>.
   *
   * @param partyService the Party Service
   */
  public PartyWebService(PartyService partyService) {
    this.partyService = partyService;
  }

  /**
   * Create the new association.
   *
   * @param tenantId the ID for the tenant
   * @param association the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateAssociationException if the association already exists
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be created
   */
  @WebMethod(operationName = "CreateAssociation")
  public void createAssociation(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Association") @XmlElement(required = true) Association association)
      throws InvalidArgumentException,
          DuplicateAssociationException,
          PartyNotFoundException,
          ServiceUnavailableException {
    partyService.createAssociation(tenantId, association);
  }

  /**
   * Create the new mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandate the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateMandateException if the mandate already exists
   * @throws PartyNotFoundException if one or more parties for the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be created
   */
  @WebMethod(operationName = "CreateMandate")
  public void createMandate(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Mandate") @XmlElement(required = true) Mandate mandate)
      throws InvalidArgumentException,
          DuplicateMandateException,
          PartyNotFoundException,
          ServiceUnavailableException {
    partyService.createMandate(tenantId, mandate);
  }

  /**
   * Create the new organization.
   *
   * @param tenantId the ID for the tenant
   * @param organization the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateOrganizationException if the organization already exists
   * @throws ServiceUnavailableException if the organization could not be created
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
   * @param tenantId the ID for the tenant
   * @param person the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicatePersonException if the person already exists
   * @throws ServiceUnavailableException if the person could not be created
   */
  @WebMethod(operationName = "CreatePerson")
  public void createPerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Person") @XmlElement(required = true) Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    partyService.createPerson(tenantId, person);
  }

  /**
   * Delete the association.
   *
   * @param tenantId the ID for the tenant
   * @param associationId the ID for the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be deleted
   */
  @WebMethod(operationName = "DeleteAssociation")
  public void deleteAssociation(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "AssociationId") @XmlElement(required = true) UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    partyService.deleteAssociation(tenantId, associationId);
  }

  /**
   * Delete the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandateId the ID for the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be deleted
   */
  @WebMethod(operationName = "DeleteMandate")
  public void deleteMandate(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "MandateId") @XmlElement(required = true) UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    partyService.deleteMandate(tenantId, mandateId);
  }

  /**
   * Delete the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be deleted
   */
  @WebMethod(operationName = "DeleteOrganization")
  public void deleteOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "OrganizationId") @XmlElement(required = true) UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    partyService.deleteOrganization(tenantId, organizationId);
  }

  /**
   * Delete the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be deleted
   */
  @WebMethod(operationName = "DeleteParty")
  public void deleteParty(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PartyId") @XmlElement(required = true) UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    partyService.deleteParty(tenantId, partyId);
  }

  /**
   * Delete the person.
   *
   * @param tenantId the ID for the tenant
   * @param personId the ID for the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be deleted
   */
  @WebMethod(operationName = "DeletePerson")
  public void deletePerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PersonId") @XmlElement(required = true) UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    partyService.deletePerson(tenantId, personId);
  }

  /**
   * Retrieve the association.
   *
   * @param tenantId the ID for the tenant
   * @param associationId the ID for the association
   * @return the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be retrieved
   */
  @WebMethod(operationName = "GetAssociation")
  @WebResult(name = "Association")
  public Association getAssociation(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "AssociationId") @XmlElement(required = true) UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    return partyService.getAssociation(tenantId, associationId);
  }

  /**
   * Retrieve the associations for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param sortBy the method used to sort the associations e.g. by type
   * @param sortDirection the sort direction to apply to the associations
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the associations for the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the associations could not be retrieved
   */
  @WebMethod(operationName = "GetAssociationsForParty")
  @WebResult(name = "AssociationsForParty")
  public AssociationsForParty getAssociationsForParty(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PartyId") @XmlElement(required = true) UUID partyId,
      @WebParam(name = "SortBy") @XmlElement AssociationSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    return partyService.getAssociationsForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandateId the ID for the mandate
   * @return the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be retrieved
   */
  @WebMethod(operationName = "GetMandate")
  @WebResult(name = "Mandate")
  public Mandate getMandate(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "MandateId") @XmlElement(required = true) UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    return partyService.getMandate(tenantId, mandateId);
  }

  /**
   * Retrieve the mandates for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param sortBy the method used to sort the mandates e.g. by type
   * @param sortDirection the sort direction to apply to the mandates
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the mandates for the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the mandates could not be retrieved
   */
  @WebMethod(operationName = "GetMandatesForParty")
  @WebResult(name = "MandatesForParty")
  public MandatesForParty getMandatesForParty(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PartyId") @XmlElement(required = true) UUID partyId,
      @WebParam(name = "SortBy") @XmlElement MandateSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    return partyService.getMandatesForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @return the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be retrieved
   */
  @WebMethod(operationName = "GetOrganization")
  @WebResult(name = "Organization")
  public Organization getOrganization(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "OrganizationId") @XmlElement(required = true) UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    Organization organization = partyService.getOrganization(tenantId, organizationId);

    return organization;
  }

  /**
   * Retrieve the organizations.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the organizations
   * @param sortBy the method used to sort the organizations e.g. by name
   * @param sortDirection the sort direction to apply to the organizations
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the organizations
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the organizations could not be retrieved
   */
  @WebMethod(operationName = "GetOrganizations")
  @WebResult(name = "Organizations")
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
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the parties
   * @param sortBy the method used to sort the parties e.g. by name
   * @param sortDirection the sort direction to apply to the parties
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the parties
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the parties could not be retrieved
   */
  @WebMethod(operationName = "GetParties")
  @WebResult(name = "Parties")
  public Parties getParties(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement PartySortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyService.getParties(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @return the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be retrieved
   */
  @WebMethod(operationName = "GetParty")
  @WebResult(name = "Party")
  public Party getParty(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "PartyId") @XmlElement(required = true) UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    return partyService.getParty(tenantId, partyId);
  }

  /**
   * Retrieve the person.
   *
   * @param tenantId the ID for the tenant
   * @param personId the ID for the person
   * @return the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be retrieved
   */
  @WebMethod(operationName = "GetPerson")
  @WebResult(name = "Person")
  public Person getPerson(
      @WebParam(name = "TenantId") @XmlElement UUID tenantId,
      @WebParam(name = "PersonId") @XmlElement UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    return partyService.getPerson(tenantId, personId);
  }

  /**
   * Retrieve the persons.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the persons
   * @param sortBy the method used to sort the persons e.g. by name
   * @param sortDirection the sort direction to apply to the persons
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the persons
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the persons could not be retrieved
   */
  @WebMethod(operationName = "GetPersons")
  @WebResult(name = "Persons")
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
   * Retrieve the snapshots for an entity.
   *
   * @param tenantId the ID for the tenant
   * @param entityType the type of entity
   * @param entityId the ID for the entity
   * @param from the date to retrieve the snapshots from
   * @param to the date to retrieve the snapshots to
   * @param sortDirection the sort direction to apply to the snapshots
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the snapshots
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the snapshots for the entity could not be retrieved
   */
  @WebMethod(operationName = "GetSnapshots")
  @WebResult(name = "Snapshots")
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
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyService.getSnapshots(
        tenantId, entityType, entityId, from, to, sortDirection, pageIndex, pageSize);
  }

  /**
   * Update the association.
   *
   * @param tenantId the ID for the tenant
   * @param association the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be updated
   */
  @WebMethod(operationName = "UpdateAssociation")
  public void updateAssociation(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Association") @XmlElement(required = true) Association association)
      throws InvalidArgumentException,
          AssociationNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    partyService.updateAssociation(tenantId, association);
  }

  /**
   * Update the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandate the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws PartyNotFoundException if one or more parties for the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be updated
   */
  @WebMethod(operationName = "UpdateMandate")
  public void updateMandate(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Mandate") @XmlElement(required = true) Mandate mandate)
      throws InvalidArgumentException,
          MandateNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    partyService.updateMandate(tenantId, mandate);
  }

  /**
   * Update the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organization the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be updated
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
   * @param tenantId the ID for the tenant
   * @param person the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be updated
   */
  @WebMethod(operationName = "UpdatePerson")
  public void updatePerson(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "Person") @XmlElement(required = true) Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    partyService.updatePerson(tenantId, person);
  }
}

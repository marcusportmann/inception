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

package digital.inception.party.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.TenantUtil;
import digital.inception.party.exception.AssociationNotFoundException;
import digital.inception.party.exception.DuplicateAssociationException;
import digital.inception.party.exception.DuplicateMandateException;
import digital.inception.party.exception.DuplicateOrganizationException;
import digital.inception.party.exception.DuplicatePersonException;
import digital.inception.party.exception.MandateNotFoundException;
import digital.inception.party.exception.OrganizationNotFoundException;
import digital.inception.party.exception.PartyNotFoundException;
import digital.inception.party.exception.PersonNotFoundException;
import digital.inception.party.model.*;
import digital.inception.party.service.PartyService;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code PartyApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
public class PartyApiControllerImpl extends SecureApiController implements PartyApiController {

  /** The Party Service. */
  private final PartyService partyService;

  /**
   * Constructs a new {@code PartyApiControllerImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param partyService the Party Service
   */
  public PartyApiControllerImpl(ApplicationContext applicationContext, PartyService partyService) {
    super(applicationContext);

    this.partyService = partyService;
  }

  @Override
  public void createAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException,
          DuplicateAssociationException,
          PartyNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(association.getTenantId())) {
      throw new AccessDeniedException(
          "Access denied to the tenant (" + association.getTenantId() + ")");
    }

    partyService.createAssociation(tenantId, association);
  }

  @Override
  public void createMandate(UUID tenantId, Mandate mandate)
      throws InvalidArgumentException,
          DuplicateMandateException,
          PartyNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(mandate.getTenantId())) {
      throw new AccessDeniedException(
          "Access denied to the tenant (" + mandate.getTenantId() + ")");
    }

    partyService.createMandate(tenantId, mandate);
  }

  @Override
  public void createOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(organization.getTenantId())) {
      throw new AccessDeniedException(
          "Access denied to the tenant (" + organization.getTenantId() + ")");
    }

    partyService.createOrganization(tenantId, organization);
  }

  @Override
  public void createPerson(UUID tenantId, Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(person.getTenantId())) {
      throw new AccessDeniedException("Access denied to the tenant (" + person.getTenantId() + ")");
    }

    partyService.createPerson(tenantId, person);
  }

  @Override
  public void deleteAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    partyService.deleteAssociation(tenantId, associationId);
  }

  @Override
  public void deleteMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    partyService.deleteMandate(tenantId, mandateId);
  }

  public void deleteOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    partyService.deleteOrganization(tenantId, organizationId);
  }

  @Override
  public void deletePerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    partyService.deletePerson(tenantId, personId);
  }

  @Override
  public Association getAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return partyService.getAssociation(tenantId, associationId);
  }

  @Override
  public AssociationsForParty getAssociationsForParty(
      UUID tenantId,
      UUID partyId,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {

    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    return partyService.getAssociationsForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Mandate getMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return partyService.getMandate(tenantId, mandateId);
  }

  @Override
  public MandatesForParty getMandatesForParty(
      UUID tenantId,
      UUID partyId,
      MandateSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {

    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    return partyService.getMandatesForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Organization getOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return partyService.getOrganization(tenantId, organizationId);
  }

  @Override
  public Organizations getOrganizations(
      UUID tenantId,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return partyService.getOrganizations(
        tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Parties getParties(
      UUID tenantId,
      String filter,
      PartySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return partyService.getParties(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Party getParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return partyService.getParty(tenantId, partyId);
  }

  @Override
  public Person getPerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return partyService.getPerson(tenantId, personId);
  }

  @Override
  public Persons getPersons(
      UUID tenantId,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return partyService.getPersons(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Snapshots getSnapshots(
      UUID tenantId,
      EntityType entityType,
      UUID entityId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return partyService.getSnapshots(
        tenantId, entityType, entityId, from, to, sortDirection, pageIndex, pageSize);
  }

  @Override
  public void updateAssociation(UUID tenantId, UUID associationId, Association association)
      throws InvalidArgumentException,
          AssociationNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (associationId == null) {
      throw new InvalidArgumentException("associationId");
    }

    if (association == null) {
      throw new InvalidArgumentException("association");
    }

    if (!associationId.equals(association.getId())) {
      throw new InvalidArgumentException("association");
    }

    partyService.updateAssociation(tenantId, association);
  }

  @Override
  public void updateMandate(UUID tenantId, UUID mandateId, Mandate mandate)
      throws InvalidArgumentException,
          MandateNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (mandateId == null) {
      throw new InvalidArgumentException("mandateId");
    }

    if (mandate == null) {
      throw new InvalidArgumentException("mandate");
    }

    if (!mandateId.equals(mandate.getId())) {
      throw new InvalidArgumentException("mandate");
    }

    partyService.updateMandate(tenantId, mandate);
  }

  @Override
  public void updateOrganization(UUID tenantId, UUID organizationId, Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

    if (organization == null) {
      throw new InvalidArgumentException("organization");
    }

    if (!organizationId.equals(organization.getId())) {
      throw new InvalidArgumentException("organization");
    }

    partyService.updateOrganization(tenantId, organization);
  }

  @Override
  public void updatePerson(UUID tenantId, UUID personId, Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if (!hasAccessToTenant(tenantId)) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

    if (person == null) {
      throw new InvalidArgumentException("person");
    }

    if (!personId.equals(person.getId())) {
      throw new InvalidArgumentException("person");
    }

    partyService.updatePerson(tenantId, person);
  }
}

/*
 * Copyright 2022 Marcus Portmann
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

import {HttpClient, HttpErrorResponse, HttpParams, HttpResponse} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ServiceUnavailableError, SortDirection
} from 'ngx-inception/core';
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Association} from "./association";
import {AssociationSortBy} from "./association-sorty-by";
import {AssociationsForParty} from "./associations-for-party";
import {EntityType} from "./entity-type";
import {Mandate} from './mandate';
import {MandateSortBy} from './mandate-sorty-by';
import {MandatesForParty} from './mandates-for-party';
import {Organization} from "./organization";
import {OrganizationSortBy} from "./organization-sort-by";
import {Organizations} from "./organizations";
import {Parties} from "./parties";
import {Party} from "./party";
import {PartySortBy} from "./party-sorty-by";
import {
  AssociationNotFoundError,
  DuplicateAssociationError, DuplicateMandateError, DuplicateOrganizationError,
  DuplicatePersonError, MandateNotFoundError,
  OrganizationNotFoundError, PartyNotFoundError, PersonNotFoundError
} from "./party.service.errors";
import {Person} from "./person";
import {PersonSortBy} from "./person-sorty-by";
import {Persons} from "./persons";
import {Snapshots} from "./snapshots";

/**
 * The Party Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class PartyService {

  /**
   * Constructs a new PartyService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient) {
    console.log('Initializing the Party Service');
  }

  /**
   * Create the new association.
   *
   * @param association The association to create.
   *
   * @return True if the association was created successfully or false otherwise.
   */
  createAssociation(association: Association): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.partyApiUrlPrefix + '/associations', association, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateAssociationError.TYPE)) {
        return throwError(() => new DuplicateAssociationError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to create the association.', httpErrorResponse));
    }));
  }

  /**
   * Create the new mandate.
   *
   * @param mandate The mandate to create.
   *
   * @return True if the mandate was created successfully or false otherwise.
   */
  createMandate(mandate: Mandate): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.partyApiUrlPrefix + '/mandates', mandate, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateMandateError.TYPE)) {
        return throwError(() => new DuplicateMandateError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to create the mandate.', httpErrorResponse));
    }));
  }

  /**
   * Create the new organization.
   *
   * @param organization The organization to create.
   *
   * @return True if the organization was created successfully or false otherwise.
   */
  createOrganization(organization: Organization): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.partyApiUrlPrefix + '/organizations', organization, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateOrganizationError.TYPE)) {
        return throwError(() => new DuplicateOrganizationError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to create the organization.', httpErrorResponse));
    }));
  }

  /**
   * Create the new person.
   *
   * @param person The person to create.
   *
   * @return True if the person was created successfully or false otherwise.
   */
  createPerson(person: Person): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.partyApiUrlPrefix + '/persons', person, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicatePersonError.TYPE)) {
        return throwError(() => new DuplicatePersonError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to create the person.', httpErrorResponse));
    }));
  }

  /**
   * Delete the association.
   *
   * @param associationId The ID for the association.
   *
   * @return True if the association was deleted or false otherwise.
   */
  deleteAssociation(associationId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.partyApiUrlPrefix + '/associations/' + associationId, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, AssociationNotFoundError.TYPE)) {
        return throwError(() => new AssociationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to delete the association.', httpErrorResponse));
    }));
  }

  /**
   * Delete the mandate.
   *
   * @param mandateId The ID for the mandate.
   *
   * @return True if the mandate was deleted or false otherwise.
   */
  deleteMandate(mandateId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.partyApiUrlPrefix + '/mandates/' + mandateId, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, MandateNotFoundError.TYPE)) {
        return throwError(() => new MandateNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to delete the mandate.', httpErrorResponse));
    }));
  }

  /**
   * Delete the organization.
   *
   * @param organizationId The ID for the organization.
   *
   * @return True if the organization was deleted or false otherwise.
   */
  deleteOrganization(organizationId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.partyApiUrlPrefix + '/organizations/' + organizationId, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, OrganizationNotFoundError.TYPE)) {
        return throwError(() => new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to delete the organization.', httpErrorResponse));
    }));
  }

  /**
   * Delete the person.
   *
   * @param personId The ID for the person.
   *
   * @return True if the person was deleted or false otherwise.
   */
  deletePerson(personId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.partyApiUrlPrefix + '/persons/' + personId, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, PersonNotFoundError.TYPE)) {
        return throwError(() => new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to delete the person.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the association.
   *
   * @param associationId The ID for the association.
   *
   * @return The association.
   */
  getAssociation(associationId: string): Observable<Association> {
    return this.httpClient.get<Association>(this.config.partyApiUrlPrefix + '/associations/' + associationId,
      {reportProgress: true})
    .pipe(map((association: Association) => {
      return association;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, AssociationNotFoundError.TYPE)) {
        return throwError(() => new AssociationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the association.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the associations for the party.
   *
   * @param partyId       The ID for the party.
   * @param sortBy        The optional method used to sort the associations e.g. by type.
   * @param sortDirection The optional sort direction to apply to the associations.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The associations for the party.
   */
  getAssociationsForParty(partyId: string, sortBy?: AssociationSortBy, sortDirection?: SortDirection,
             pageIndex?: number, pageSize?: number): Observable<AssociationsForParty> {

    let params = new HttpParams();

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<AssociationsForParty>(
      this.config.partyApiUrlPrefix + '/parties/' + partyId + '/associations', {
        params,
        reportProgress: true,
      }).pipe(map((associations: AssociationsForParty) => {
      return associations;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {

      if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the associations for the party.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the mandate.
   *
   * @param mandateId The ID for the mandate.
   *
   * @return The mandate.
   */
  getMandate(mandateId: string): Observable<Mandate> {
    return this.httpClient.get<Mandate>(this.config.partyApiUrlPrefix + '/mandates/' + mandateId,
      {reportProgress: true})
    .pipe(map((mandate: Mandate) => {
      return mandate;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, MandateNotFoundError.TYPE)) {
        return throwError(() => new MandateNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandate.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the mandates for the party.
   *
   * @param partyId       The ID for the party.
   * @param sortBy        The optional method used to sort the mandates e.g. by type.
   * @param sortDirection The optional sort direction to apply to the mandates.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The mandates for the party.
   */
  getMandatesForParty(partyId: string, sortBy?: MandateSortBy, sortDirection?: SortDirection,
                          pageIndex?: number, pageSize?: number): Observable<MandatesForParty> {

    let params = new HttpParams();

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<MandatesForParty>(
      this.config.partyApiUrlPrefix + '/parties/' + partyId + '/mandates', {
        params,
        reportProgress: true,
      }).pipe(map((mandates: MandatesForParty) => {
      return mandates;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {

      if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandates for the party.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId The ID for the organization.
   *
   * @return The organization.
   */
  getOrganization(organizationId: string): Observable<Organization> {
    return this.httpClient.get<Organization>(this.config.partyApiUrlPrefix + '/organizations/' + organizationId,
      {reportProgress: true})
    .pipe(map((organization: Organization) => {
      return organization;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, OrganizationNotFoundError.TYPE)) {
        return throwError(() => new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the organization.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter        The optional filter to apply to the organizations.
   * @param sortBy        The optional method used to sort the organizations e.g. by name.
   * @param sortDirection The optional sort direction to apply to the organizations.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The users.
   */
  getOrganizations(filter?: string, sortBy?: OrganizationSortBy, sortDirection?: SortDirection,
           pageIndex?: number, pageSize?: number): Observable<Organizations> {

    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Organizations>(
      this.config.partyApiUrlPrefix + '/organizations', {
        params,
        reportProgress: true,
      }).pipe(map((organizations: Organizations) => {
      return organizations;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the organizations.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the parties.
   *
   * @param filter        The optional filter to apply to the parties.
   * @param sortBy        The optional method used to sort the parties e.g. by name.
   * @param sortDirection The optional sort direction to apply to the parties.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The parties.
   */
  getParties(filter?: string, sortBy?: PartySortBy, sortDirection?: SortDirection,
             pageIndex?: number, pageSize?: number): Observable<Parties> {

    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Parties>(
      this.config.partyApiUrlPrefix + '/parties', {
        params,
        reportProgress: true,
      }).pipe(map((parties: Parties) => {
      return parties;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the parties.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the party.
   *
   * @param partyId The ID for the party.
   *
   * @return The party.
   */
  getParty(partyId: string): Observable<Party> {
    return this.httpClient.get<Party>(this.config.partyApiUrlPrefix + '/parties/' + partyId,
      {reportProgress: true})
    .pipe(map((party: Party) => {
      return party;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the party.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the person.
   *
   * @param personId The ID for the person.
   *
   * @return The person.
   */
  getPerson(personId: string): Observable<Person> {
    return this.httpClient.get<Person>(this.config.partyApiUrlPrefix + '/persons/' + personId,
      {reportProgress: true})
    .pipe(map((person: Person) => {
      return person;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, PersonNotFoundError.TYPE)) {
        return throwError(() => new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the person.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the persons.
   *
   * @param filter        The optional filter to apply to the persons.
   * @param sortBy        The optional method used to sort the persons e.g. by name.
   * @param sortDirection The optional sort direction to apply to the persons.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The persons.
   */
  getPersons(filter?: string, sortBy?: PersonSortBy, sortDirection?: SortDirection,
                   pageIndex?: number, pageSize?: number): Observable<Persons> {

    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Persons>(
      this.config.partyApiUrlPrefix + '/persons', {
        params,
        reportProgress: true,
      }).pipe(map((persons: Persons) => {
      return persons;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the persons.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the snapshots for an entity.
   *
   * @param entityType    The type of entity
   * @param entityId      The ID for the entity
   * @param from          The optional date to retrieve the snapshots from
   * @param to            The optional date to retrieve the snapshots to
   * @param sortDirection The optional sort direction to apply to the snapshots.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The snapshots.
   */
  getSnapshots(entityType: EntityType, entityId: string, from?: Date, to?: Date,
               sortDirection?: SortDirection, pageIndex?: number,
               pageSize?: number): Observable<Snapshots> {

    let params = new HttpParams();

    params = params.append('entityType', entityType);
    params = params.append('entityId', entityId);

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Snapshots>(
      this.config.partyApiUrlPrefix + '/snapshots', {
        params,
        reportProgress: true,
      }).pipe(map((snapshots: Snapshots) => {
      return snapshots;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the snapshots.', httpErrorResponse));
    }));
  }

  /**
   * Update the association.
   *
   * @param association The association to update.
   *
   * @return True if the association was updated successfully or false otherwise.
   */
  updateAssociation(association: Association): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.partyApiUrlPrefix + '/associations/' + association.id,
      association, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, AssociationNotFoundError.TYPE)) {
        return throwError(() => new AssociationNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to update the association.', httpErrorResponse));
    }));
  }

  /**
   * Update the mandate.
   *
   * @param mandate The mandate to update.
   *
   * @return True if the mandate was updated successfully or false otherwise.
   */
  updateMandate(mandate: Mandate): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.partyApiUrlPrefix + '/mandates/' + mandate.id,
      mandate, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, MandateNotFoundError.TYPE)) {
        return throwError(() => new MandateNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(() => new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to update the mandate.', httpErrorResponse));
    }));
  }

  /**
   * Update the organization.
   *
   * @param organization The organization to update.
   *
   * @return True if the organization was updated successfully or false otherwise.
   */
  updateOrganization(organization: Organization): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.partyApiUrlPrefix + '/organizations/' + organization.id,
      organization, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, OrganizationNotFoundError.TYPE)) {
        return throwError(() => new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to update the organization.', httpErrorResponse));
    }));
  }

  /**
   * Update the person.
   *
   * @param person The person to update.
   *
   * @return True if the person was updated successfully or false otherwise.
   */
  updatePerson(person: Person): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.partyApiUrlPrefix + '/persons/' + person.id,
      person, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, PersonNotFoundError.TYPE)) {
        return throwError(() => new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to update the person.', httpErrorResponse));
    }));
  }
}

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

import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Association} from "./association";
import {Organization} from "./organization";
import {
  AssociationNotFoundError,
  DuplicateAssociationError, DuplicateOrganizationError, DuplicatePersonError,
  OrganizationNotFoundError, PartyNotFoundError, PersonNotFoundError
} from "./party.service.errors";
import {Person} from "./person";

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
        return throwError(new DuplicateAssociationError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to create the association.', httpErrorResponse));
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
        return throwError(new DuplicateOrganizationError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to create the organization.', httpErrorResponse));
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
        return throwError(new DuplicatePersonError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to create the person.', httpErrorResponse));
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
        return throwError(new AssociationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to delete the association.', httpErrorResponse));
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
        return throwError(new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to delete the organization.', httpErrorResponse));
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
        return throwError(new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to delete the person.', httpErrorResponse));
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
        return throwError(new AssociationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the association.', httpErrorResponse));
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
        return throwError(new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the organization.', httpErrorResponse));
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
        return throwError(new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the person.', httpErrorResponse));
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
        return throwError(new AssociationNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PartyNotFoundError.TYPE)) {
        return throwError(new PartyNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to update the association.', httpErrorResponse));
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
        return throwError(new OrganizationNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to update the organization.', httpErrorResponse));
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
        return throwError(new PersonNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to update the person.', httpErrorResponse));
    }));
  }
}

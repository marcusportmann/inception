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

import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Inject, Injectable, LOCALE_ID} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {ContactMechanismRole} from "./contact-mechanism-role";
import {ContactMechanismType} from "./contact-mechanism-type";
import {EmploymentStatus} from "./employment-status";
import {EmploymentType} from "./employment-type";
import {Gender} from "./gender";
import {IdentityDocumentType} from "./identity-document-type";
import {MaritalStatus} from "./marital-status";
import {MarriageType} from "./marriage-type";
import {NextOfKinType} from "./next-of-kin-type";
import {Occupation} from "./occupation";
import {Race} from "./race";
import {ResidencePermitType} from "./residence-permit-type";
import {ResidencyStatus} from "./residency-status";
import {ResidentialType} from "./residential-type";
import {SourceOfFundsType} from "./source-of-funds-type";
import {TaxNumberType} from "./tax-number-type";
import {Title} from "./title";

/**
 * The Party Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class PartyReferenceService {

  /**
   * Constructs a new ReferenceService.
   *
   * @param config     The Inception configuration.
   * @param localeId   The Unicode locale identifier for the locale for the application.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              @Inject(LOCALE_ID) private localeId: string, private httpClient: HttpClient) {
    console.log('Initializing the Party Reference Service (' + localeId + ')');
  }

  /**
   * Retrieve the contact mechanism roles.
   *
   * @return The contact mechanism roles.
   */
  getContactMechanismRoles(): Observable<ContactMechanismRole[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ContactMechanismRole[]>(this.config.partyReferenceApiUrlPrefix + '/contact-mechanism-roles',
      {params, reportProgress: true})
    .pipe(map((contactMechanismRoles: ContactMechanismRole[]) => {
      return contactMechanismRoles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the contact mechanism roles.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @return The contact mechanism types.
   */
  getContactMechanismTypes(): Observable<ContactMechanismType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ContactMechanismType[]>(this.config.partyReferenceApiUrlPrefix + '/contact-mechanism-types',
      {params, reportProgress: true})
    .pipe(map((contactMechanismTypes: ContactMechanismType[]) => {
      return contactMechanismTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the contact mechanism types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the employment statuses.
   *
   * @return The employment statuses.
   */
  getEmploymentStatuses(): Observable<EmploymentStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<EmploymentStatus[]>(this.config.partyReferenceApiUrlPrefix + '/employment-statuses',
      {params, reportProgress: true})
    .pipe(map((employmentStatuses: EmploymentStatus[]) => {
      return employmentStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the employment statuses.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the employment types.
   *
   * @return The employment types.
   */
  getEmploymentTypes(): Observable<EmploymentType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<EmploymentType[]>(this.config.partyReferenceApiUrlPrefix + '/employment-types',
      {params, reportProgress: true})
    .pipe(map((employmentTypes: EmploymentType[]) => {
      return employmentTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the employment types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the genders.
   *
   * @return The genders.
   */
  getGenders(): Observable<Gender[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Gender[]>(this.config.partyReferenceApiUrlPrefix + '/genders',
      {params, reportProgress: true})
    .pipe(map((genders: Gender[]) => {
      return genders;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the genders.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the identity document types.
   *
   * @return The identity document types.
   */
  getIdentityDocumentTypes(): Observable<IdentityDocumentType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<IdentityDocumentType[]>(this.config.partyReferenceApiUrlPrefix + '/identity-document-types',
      {params, reportProgress: true})
    .pipe(map((identityDocumentTypes: IdentityDocumentType[]) => {
      return identityDocumentTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the identity document types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the marital statuses.
   *
   * @return The marital statuses.
   */
  getMaritalStatuses(): Observable<MaritalStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MaritalStatus[]>(this.config.partyReferenceApiUrlPrefix + '/marital-statuses',
      {params, reportProgress: true})
    .pipe(map((maritalStatuses: MaritalStatus[]) => {
      return maritalStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the marital statuses.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the marriage types.
   *
   * @return The marriage types.
   */
  getMarriageTypes(): Observable<MarriageType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MarriageType[]>(this.config.partyReferenceApiUrlPrefix + '/marriage-types',
      {params, reportProgress: true})
    .pipe(map((marriageTypes: MarriageType[]) => {
      return marriageTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the marriage types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the next of kin types.
   *
   * @return The next of kin types.
   */
  getNextOfKinTypes(): Observable<NextOfKinType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<NextOfKinType[]>(this.config.partyReferenceApiUrlPrefix + '/next-of-kin-types',
      {params, reportProgress: true})
    .pipe(map((nextOfKinTypes: NextOfKinType[]) => {
      return nextOfKinTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the next of kin types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the occupations.
   *
   * @return The occupations.
   */
  getOccupations(): Observable<Occupation[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Occupation[]>(this.config.partyReferenceApiUrlPrefix + '/occupations',
      {params, reportProgress: true})
    .pipe(map((occupations: Occupation[]) => {
      return occupations;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the occupations.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the races.
   *
   * @return The races.
   */
  getRaces(): Observable<Race[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Race[]>(this.config.partyReferenceApiUrlPrefix + '/races',
      {params, reportProgress: true})
    .pipe(map((races: Race[]) => {
      return races;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the races.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residence permit types.
   *
   * @return The residence permit types.
   */
  getResidencePermitTypes(): Observable<ResidencePermitType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidencePermitType[]>(this.config.partyReferenceApiUrlPrefix + '/residence-permit-types',
      {params, reportProgress: true})
    .pipe(map((residencePermitTypes: ResidencePermitType[]) => {
      return residencePermitTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the residence permit types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residency statuses.
   *
   * @return The residency statuses.
   */
  getResidencyStatuses(): Observable<ResidencyStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidencyStatus[]>(this.config.partyReferenceApiUrlPrefix + '/residency-statuses',
      {params, reportProgress: true})
    .pipe(map((residencyStatuses: ResidencyStatus[]) => {
      return residencyStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the residency statuses.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residential types.
   *
   * @return The residential types.
   */
  getResidentialTypes(): Observable<ResidentialType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidentialType[]>(this.config.partyReferenceApiUrlPrefix + '/residential-types',
      {params, reportProgress: true})
    .pipe(map((residentialTypes: ResidentialType[]) => {
      return residentialTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the residential types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the source of funds types.
   *
   * @return The source of funds types.
   */
  getSourceOfFundsTypes(): Observable<SourceOfFundsType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SourceOfFundsType[]>(this.config.partyReferenceApiUrlPrefix + '/source-of-funds-types',
      {params, reportProgress: true})
    .pipe(map((sourcesOfFunds: SourceOfFundsType[]) => {
      return sourcesOfFunds;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the source of funds types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the tax number types.
   *
   * @return The tax number types.
   */
  getTaxNumberTypes(): Observable<TaxNumberType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<TaxNumberType[]>(this.config.partyReferenceApiUrlPrefix + '/tax-number-types',
      {params, reportProgress: true})
    .pipe(map((taxNumberTypes: TaxNumberType[]) => {
      return taxNumberTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the tax number types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the titles.
   *
   * @return The titles.
   */
  getTitles(): Observable<Title[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Title[]>(this.config.partyReferenceApiUrlPrefix + '/titles',
      {params, reportProgress: true})
    .pipe(map((titles: Title[]) => {
      return titles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      }

      return throwError(new ServiceUnavailableError('Failed to retrieve the titles.', httpErrorResponse));
    }));
  }
}

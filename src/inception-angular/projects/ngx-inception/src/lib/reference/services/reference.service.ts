/*
 * Copyright 2020 Marcus Portmann
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

import {Inject, Injectable, LOCALE_ID} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {ApiError} from '../../core/errors/api-error';
import {ReferenceServiceError} from './reference.service.errors';
import {CommunicationError} from '../../core/errors/communication-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';
import {PhysicalAddressType} from "./physical-address-type";
import {CommunicationMethod} from "./communication-method";
import {Country} from "./country";
import {EmploymentStatus} from "./employment-status";
import {EmploymentType} from "./employment-type";
import {Gender} from "./gender";
import {IdentityDocumentType} from "./identity-document-type";
import {Language} from "./language";
import {MaritalStatus} from "./marital-status";
import {MarriageType} from "./marriage-type";
import {MinorType} from "./minor-type";
import {NextOfKinType} from "./next-of-kin-type";
import {Occupation} from "./occupation";
import {ResidencePermitType} from "./residence-permit-type";
import {Race} from "./race";
import {Region} from "./region";
import {ResidencyStatus} from "./residency-status";
import {ResidentialType} from "./residential-type";
import {SourceOfFunds} from "./source-of-funds";
import {SuitableTimeToContact} from "./suitable-time-to-contact";
import {TaxNumberType} from "./tax-number-type";
import {Title} from "./title";
import {VerificationStatus} from "./verification-status";
import {VerificationMethod} from "./verification-method";

/**
 * The Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ReferenceService {

  /**
   * Constructs a new ReferenceService.
   *
   * @param config     The Inception configuration.
   * @param localeId   The Unicode locale identifier identifying the locale for the application.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              @Inject(LOCALE_ID) private localeId: string, private httpClient: HttpClient) {
    console.log('Initializing the Reference Service (' + localeId + ')');
  }

  /**
   * Retrieve the communication methods.
   *
   * @return The communication methods.
   */
  getCommunicationMethods(): Observable<CommunicationMethod[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<CommunicationMethod[]>(this.config.referenceApiUrlPrefix + '/communication-methods',
      {params, reportProgress: true})
    .pipe(map((communicationMethods: CommunicationMethod[]) => {
      return communicationMethods;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the communication methods.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the countries.
   *
   * @return The countries.
   */
  getCountries(): Observable<Country[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Country[]>(this.config.referenceApiUrlPrefix + '/countries',
      {params, reportProgress: true})
    .pipe(map((countries: Country[]) => {
      return countries;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the countries.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<EmploymentStatus[]>(this.config.referenceApiUrlPrefix + '/employment-statuses',
      {params, reportProgress: true})
    .pipe(map((employmentStatuses: EmploymentStatus[]) => {
      return employmentStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the employment statuses.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<EmploymentType[]>(this.config.referenceApiUrlPrefix + '/employment-types',
      {params, reportProgress: true})
    .pipe(map((employmentTypes: EmploymentType[]) => {
      return employmentTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the employment types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<Gender[]>(this.config.referenceApiUrlPrefix + '/genders',
      {params, reportProgress: true})
    .pipe(map((genders: Gender[]) => {
      return genders;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the genders.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<IdentityDocumentType[]>(this.config.referenceApiUrlPrefix + '/identity-document-types',
      {params, reportProgress: true})
    .pipe(map((identityDocumentTypes: IdentityDocumentType[]) => {
      return identityDocumentTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the identity document types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the languages.
   *
   * @return The languages.
   */
  getLanguages(): Observable<Language[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Language[]>(this.config.referenceApiUrlPrefix + '/languages',
      {params, reportProgress: true})
    .pipe(map((languages: Language[]) => {
      return languages;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the languages.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<MaritalStatus[]>(this.config.referenceApiUrlPrefix + '/marital-statuses',
      {params, reportProgress: true})
    .pipe(map((maritalStatuses: MaritalStatus[]) => {
      return maritalStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the marital statuses.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<MarriageType[]>(this.config.referenceApiUrlPrefix + '/marriage-types',
      {params, reportProgress: true})
    .pipe(map((marriageTypes: MarriageType[]) => {
      return marriageTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the marriage types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the minor types.
   *
   * @return The minor types.
   */
  getMinorTypes(): Observable<MinorType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MinorType[]>(this.config.referenceApiUrlPrefix + '/minor-types',
      {params, reportProgress: true})
    .pipe(map((minorTypes: MinorType[]) => {
      return minorTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the minor types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<NextOfKinType[]>(this.config.referenceApiUrlPrefix + '/next-of-kin-types',
      {params, reportProgress: true})
    .pipe(map((nextOfKinTypes: NextOfKinType[]) => {
      return nextOfKinTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the next of kin types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<Occupation[]>(this.config.referenceApiUrlPrefix + '/occupations',
      {params, reportProgress: true})
    .pipe(map((occupations: Occupation[]) => {
      return occupations;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the occupations.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the physical address types.
   *
   * @return The physical address types.
   */
  getPhysicalAddressTypes(): Observable<PhysicalAddressType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PhysicalAddressType[]>(this.config.referenceApiUrlPrefix + '/physical-address-types',
      {params, reportProgress: true})
    .pipe(map((addressTypes: PhysicalAddressType[]) => {
      return addressTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the physical address types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<Race[]>(this.config.referenceApiUrlPrefix + '/races',
      {params, reportProgress: true})
    .pipe(map((races: Race[]) => {
      return races;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the races.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the regions.
   *
   * @return The regions.
   */
  getRegions(): Observable<Region[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Region[]>(this.config.referenceApiUrlPrefix + '/regions',
      {params, reportProgress: true})
    .pipe(map((regions: Region[]) => {
      return regions;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the regions.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<ResidencePermitType[]>(this.config.referenceApiUrlPrefix + '/residence-permit-types',
      {params, reportProgress: true})
    .pipe(map((residencePermitTypes: ResidencePermitType[]) => {
      return residencePermitTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the residence permit types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<ResidencyStatus[]>(this.config.referenceApiUrlPrefix + '/residency-statuses',
      {params, reportProgress: true})
    .pipe(map((residencyStatuses: ResidencyStatus[]) => {
      return residencyStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the residency statuses.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<ResidentialType[]>(this.config.referenceApiUrlPrefix + '/residential-types',
      {params, reportProgress: true})
    .pipe(map((residentialTypes: ResidentialType[]) => {
      return residentialTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the residential types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the sources of funds.
   *
   * @return The sources of funds.
   */
  getSourcesOfFunds(): Observable<SourceOfFunds[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SourceOfFunds[]>(this.config.referenceApiUrlPrefix + '/sources-of-funds',
      {params, reportProgress: true})
    .pipe(map((sourcesOfFunds: SourceOfFunds[]) => {
      return sourcesOfFunds;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the sources of funds.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the suitable times to contact.
   *
   * @return The suitable times to contact.
   */
  getSuitableTimesToContact(): Observable<SuitableTimeToContact[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SuitableTimeToContact[]>(this.config.referenceApiUrlPrefix + '/suitable-times-to-contact',
      {params, reportProgress: true})
    .pipe(map((suitableTimesToContact: SuitableTimeToContact[]) => {
      return suitableTimesToContact;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the suitable times to contact.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<TaxNumberType[]>(this.config.referenceApiUrlPrefix + '/tax-number-types',
      {params, reportProgress: true})
    .pipe(map((taxNumberTypes: TaxNumberType[]) => {
      return taxNumberTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the tax number types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
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

    return this.httpClient.get<Title[]>(this.config.referenceApiUrlPrefix + '/titles',
      {params, reportProgress: true})
    .pipe(map((titles: Title[]) => {
      return titles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the titles.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the verification methods.
   *
   * @return The verification methods.
   */
  getVerificationMethods(): Observable<VerificationMethod[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<VerificationMethod[]>(this.config.referenceApiUrlPrefix + '/verification-methods',
      {params, reportProgress: true})
    .pipe(map((verificationMethods: VerificationMethod[]) => {
      return verificationMethods;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the verification methods.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the verification statuses.
   *
   * @return The verification statuses.
   */
  getVerificationStatuses(): Observable<VerificationStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<VerificationStatus[]>(this.config.referenceApiUrlPrefix + '/verification-statuses',
      {params, reportProgress: true})
    .pipe(map((verificationStatuses: VerificationStatus[]) => {
      return verificationStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ReferenceServiceError('Failed to retrieve the verification statuses.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }
}

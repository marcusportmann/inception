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

import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Inject, Injectable, LOCALE_ID} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, of, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Country} from './country';
import {Language} from './language';
import {Region} from './region';

/**
 * The Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ReferenceService {

  private static cachedCountries: Map<string, Country> | null = null;

  private static cachedLanguages: Map<string, Language> | null = null;

  private static cachedRegions: Map<string, Map<string, Region>> = new Map<string, Map<string, Region>>();

  /**
   * Constructs a new ReferenceService.
   *
   * @param config     The Inception configuration.
   * @param localeId   The Unicode locale identifier for the locale for the application.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              @Inject(LOCALE_ID) private localeId: string, private httpClient: HttpClient) {
    console.log('Initializing the Reference Service (' + localeId + ')');
  }

  /**
   * Retrieve the countries.
   *
   * @return The countries.
   */
  getCountries(): Observable<Map<string, Country>> {
    if (!!ReferenceService.cachedCountries) {
      return of(ReferenceService.cachedCountries);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Country[]>(this.config.referenceApiUrlPrefix + '/countries',
        {params, reportProgress: true})
      .pipe(map((countries: Country[]) => {
        let cachedCountries = new Map<string, Country>();

        for (const country of countries) {
          cachedCountries.set(country.code, country);
        }

        ReferenceService.cachedCountries = cachedCountries;

        return ReferenceService.cachedCountries;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }

        return throwError(new ServiceUnavailableError('Failed to retrieve the countries.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the languages.
   *
   * @return The languages.
   */
  getLanguages(): Observable<Map<string, Language>> {
    if (!!ReferenceService.cachedLanguages) {
      return of(ReferenceService.cachedLanguages);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Language[]>(this.config.referenceApiUrlPrefix + '/languages',
        {params, reportProgress: true})
      .pipe(map((languages: Language[]) => {
        let cachedLanguages = new Map<string, Language>();

        for (const language of languages) {
          cachedLanguages.set(language.code, language);
        }

        ReferenceService.cachedLanguages = cachedLanguages;

        return ReferenceService.cachedLanguages;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }

        return throwError(new ServiceUnavailableError('Failed to retrieve the languages.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the regions.
   *
   * @param country The ISO 3166-1 alpha-2 code for the country to retrieve the regions for.
   *
   * @return The regions.
   */
  getRegions(country: string): Observable<Map<string, Region>> {
    let cachedRegionsForCountry: Map<string, Region> | undefined = ReferenceService.cachedRegions.get(country);

    if (!!cachedRegionsForCountry) {
      return of(cachedRegionsForCountry);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);
      params = params.append('country', country);

      return this.httpClient.get<Region[]>(this.config.referenceApiUrlPrefix + '/regions',
        {params, reportProgress: true})
      .pipe(map((regions: Region[]) => {
        let cachedRegionsForCountry = new Map<string, Region>();

        for (const region of regions) {
          cachedRegionsForCountry.set(region.code, region);
        }

        ReferenceService.cachedRegions.set(country, cachedRegionsForCountry);

        return cachedRegionsForCountry;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }

        return throwError(new ServiceUnavailableError('Failed to retrieve the regions.', httpErrorResponse));
      }));
    }
  }
}

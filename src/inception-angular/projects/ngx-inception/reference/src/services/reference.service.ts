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

import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Inject, Injectable, LOCALE_ID} from '@angular/core';
import {
  AccessDeniedError, CacheService, CommunicationError, INCEPTION_CONFIG, InceptionConfig,
  ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, of, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Country} from './country';
import {Language} from './language';
import {Region} from './region';
import {TimeZone} from './time-zone';

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
   * @param localeId   The Unicode locale identifier for the locale for the application.
   * @param httpClient The HTTP client.
   * @param cacheService The cache service for caching reference data.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              @Inject(LOCALE_ID) private localeId: string, private httpClient: HttpClient,
              private cacheService: CacheService) {
    console.log(`Initializing the Reference Service (${localeId})`);
  }

  /**
   * Retrieve the countries.
   *
   * @return The countries.
   */
  getCountries(): Observable<Map<string, Country>> {
    return this.getData('countries', '/reference/countries', (country: Country) => country.code);
  }

  /**
   * Retrieve the languages.
   *
   * @return The languages.
   */
  getLanguages(): Observable<Map<string, Language>> {
    return this.getData('languages', '/reference/languages', (language: Language) => language.code);
  }

  /**
   * Retrieve the regions.
   *
   * @return The regions.
   */
  getRegions(): Observable<Map<string, Region>> {
    return this.getData('regions', '/reference/regions', (region: Region) => region.code);
  }

  /**
   * Retrieve the time zones.
   *
   * @return The time zones.
   */
  getTimeZones(): Observable<Map<string, TimeZone>> {
    return this.getData('timeZones', '/reference/time-zones', (timeZone: TimeZone) => timeZone.id);
  }

  /**
   * Generic method to retrieve reference data, cache it, and return it as a Map.
   *
   * @param cacheKey  The key used to cache the data.
   * @param endpoint  The API endpoint for retrieving the data.
   * @param keyGetter A function to extract the key for each entity (e.g., code or id).
   *
   * @return An observable with the cached or retrieved data.
   */
  private getData<T>(cacheKey: string, endpoint: string,
                     keyGetter: (item: T) => string): Observable<Map<string, T>> {
    let cachedData: Map<string, T> = this.cacheService.get(cacheKey);

    if (cachedData !== undefined) {
      return of(cachedData);
    }

    const params = new HttpParams().set('localeId', this.localeId);

    return this.httpClient.get<T[]>(this.config.apiUrlPrefix + endpoint, {
      params,
      reportProgress: true
    })
    .pipe(map((items: T[]) => {
      cachedData = new Map<string, T>();

      for (const item of items) {
        cachedData.set(keyGetter(item), item);
      }

      this.cacheService.set(cacheKey, cachedData);

      return cachedData;
    }), catchError(this.handleApiError(`Failed to retrieve the ${cacheKey}.`)));
  }

  private handleApiError(defaultMessage: string) {
    return (httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }
      return throwError(() => new ServiceUnavailableError(defaultMessage, httpErrorResponse));
    };
  }
}


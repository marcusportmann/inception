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

import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Code} from './code';
import {CodeCategory} from './code-category';
import {
  CodeCategoryNotFoundError,
  CodeNotFoundError,
  CodesServiceError,
  DuplicateCodeCategoryError,
  DuplicateCodeError
} from './codes.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {ApiError} from '../../errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {environment} from '../../../../environments/environment';
import {CodeCategorySummary} from './code-category-summary';
import {AccessDeniedError} from '../../errors/access-denied-error';

/**
 * The Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class CodesService {

  /**
   * Constructs a new CodesService.
   *
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Codes Service');
  }

  /**
   * Create a code.
   *
   * @param code The code code to create.
   *
   * @return True if the code was created successfully or false otherwise.
   */
  createCode(code: Code): Observable<boolean> {
    return this.httpClient.post<boolean>(
      environment.codesServiceUrlPrefix + '/code-categories/' + code.codeCategoryId + '/codes',
      code, {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeCategoryNotFoundError(this.i18n({
            id: '@@codes_service_the_code_category_could_not_be_found',
            value: 'The code category could not be found.'
          }), apiError));
        } else if (apiError.status === 409) {
          return throwError(new DuplicateCodeError(this.i18n({
            id: '@@codes_service_the_code_already_exists',
            value: 'The code already exists.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_create_the_code',
            value: 'Failed to create the code.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Create a code category.
   *
   * @param codeCategory The code category to create.
   *
   * @return True if the code category was created successfully or false otherwise.
   */
  createCodeCategory(codeCategory: CodeCategory): Observable<boolean> {
    return this.httpClient.post<boolean>(environment.codesServiceUrlPrefix + '/code-categories',
      codeCategory, {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 409) {
          return throwError(new DuplicateCodeCategoryError(this.i18n({
            id: '@@codes_service_the_code_category_already_exists',
            value: 'The code category already exists.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_create_the_code_category',
            value: 'Failed to create the code category.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId The ID used to uniquely identify the code category the code is
   *                       associated with.
   * @param codeId         The ID used to uniquely identify the code.
   *
   * @return True if the code was deleted or false otherwise.
   */
  deleteCode(codeCategoryId: string, codeId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategoryId + '/codes/' + codeId,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeNotFoundError(this.i18n({
            id: '@@codes_service_the_code_could_not_be_found',
            value: 'The code could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_delete_the_code',
            value: 'Failed to delete the code.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId The ID used to uniquely identify the code category.
   *
   * @return True if the code category was deleted or false otherwise.
   */
  deleteCodeCategory(codeCategoryId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategoryId,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeCategoryNotFoundError(this.i18n({
            id: '@@codes_service_the_code_category_could_not_be_found',
            value: 'The code category could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_delete_the_code_category',
            value: 'Failed to delete the code category.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId The ID used to uniquely identify the code category the code is
   *                       associated with.
   * @param codeId         The ID uniquely identifying the code.
   *
   * @return The code.
   */
  getCode(codeCategoryId: string, codeId: string): Observable<Code> {
    return this.httpClient.get<Code>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategoryId + '/codes/' + codeId,
      {reportProgress: true}).pipe(map((code: Code) => {
      return code;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeNotFoundError(this.i18n({
            id: '@@codes_service_the_code_could_not_be_found',
            value: 'The code could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code',
            value: 'Failed to retrieve the code.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve all the code categories.
   *
   * @return The code categories.
   */
  getCodeCategories(): Observable<CodeCategory[]> {
    return this.httpClient.get<CodeCategory[]>(
      environment.codesServiceUrlPrefix + '/code-categories', {reportProgress: true})
      .pipe(map((codeCategories: CodeCategory[]) => {
        return codeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code_categories',
            value: 'Failed to retrieve the code categories.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId The ID used to uniquely identify the code category.
   *
   * @return The code category.
   */
  getCodeCategory(codeCategoryId: string): Observable<CodeCategory> {
    return this.httpClient.get<CodeCategory>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategoryId,
      {reportProgress: true}).pipe(map((codeCategory: CodeCategory) => {
      return codeCategory;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeCategoryNotFoundError(this.i18n({
            id: '@@codes_service_the_code_category_could_not_be_found',
            value: 'The code category could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code_category',
            value: 'Failed to retrieve the code category.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the codes for the code category.
   *
   * @param codeCategoryId The ID used to uniquely identify the code category.
   *
   * @return the codes for the code category
   */
  getCodes(codeCategoryId: string): Observable<Code[]> {
    return this.httpClient.get<Code[]>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategoryId + '/codes',
      {reportProgress: true}).pipe(map((codes: Code[]) => {
      return codes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeCategoryNotFoundError(this.i18n({
            id: '@@codes_service_the_code_category_could_not_be_found',
            value: 'The code category could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_codes',
            value: 'Failed to retrieve the codes.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the summaries for all code categories.
   *
   * @return The code category summaries.
   */
  getCodeCategorySummaries(): Observable<CodeCategorySummary[]> {
    return this.httpClient.get<CodeCategory[]>(
      environment.codesServiceUrlPrefix + '/code-category-summaries', {reportProgress: true})
      .pipe(map((codeCategorySummaries: CodeCategorySummary[]) => {
        return codeCategorySummaries;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(new AccessDeniedError(httpErrorResponse, this.i18n));
        } else if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code_categories',
            value: 'Failed to retrieve the code categories.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Update the code.
   *
   * @param code The code to update.
   *
   * @return True if the code was updated successfully or false otherwise.
   */
  updateCode(code: Code): Observable<boolean> {
    return this.httpClient.put<boolean>(
      environment.codesServiceUrlPrefix + '/code-categories/' + code.codeCategoryId + '/codes/' +
      code.id, code, {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeNotFoundError(this.i18n({
            id: '@@codes_service_the_code_could_not_be_found',
            value: 'The code could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_update_the_code',
            value: 'Failed to update the code.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Update the code category.
   *
   * @param codeCategory The code category to update.
   *
   * @return True if the code category was updated successfully or false otherwise.
   */
  updateCodeCategory(codeCategory: CodeCategory): Observable<boolean> {
    return this.httpClient.put<boolean>(
      environment.codesServiceUrlPrefix + '/code-categories/' + codeCategory.id, codeCategory,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
          return throwError(new CodeCategoryNotFoundError(this.i18n({
            id: '@@codes_service_the_code_category_could_not_be_found',
            value: 'The code category could not be found.'
          }), apiError));
        } else {
          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_update_the_code_category',
            value: 'Failed to update the code category.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }
}

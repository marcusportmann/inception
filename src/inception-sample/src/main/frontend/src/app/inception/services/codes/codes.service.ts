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
import {Code} from "./code";
import {CodeCategory} from "./code-category";
import {
  CodeCategoryNotFoundError,
  CodesServiceError,
  DuplicateCodeCategoryError
} from "./codes.service.errors";
import {CommunicationError} from "../../errors/communication-error";
import {ApiError} from "../../errors/api-error";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {environment} from "../../../../environments/environment";

/**
 * The CodesService class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class CodesService {

  /**
   * Constructs a new CodesService.
   *
   * @param {HttpClient} httpClient The HTTP client.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Codes Service');
  }

  /**
   * Create a code.
   *
   * @param {Code} code The code code to create.
   *
   * @return True if the code was created successfully or false otherwise.
   */
  createCode(code: Code): Observable<boolean> {

    return this.httpClient.post<boolean>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + code.codeCategoryId,
      code, {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
            return throwError(new CodeCategoryNotFoundError(this.i18n({
              id: '@@codes_service_the_code_category_could_not_be_found',
              value: 'The code category could not be found.'
            }), apiError));
          } else if (apiError.status == 409) {
            return throwError(new DuplicateCodeCategoryError(this.i18n({
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
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Create a code category.
   *
   * @param {CodeCategory} codeCategory The code category to create.
   *
   * @return True if the code category was created successfully or false otherwise.
   */
  createCodeCategory(codeCategory: CodeCategory): Observable<boolean> {

    return this.httpClient.post<boolean>(environment.codesServiceUrlPrefix + '/codeCategories',
      codeCategory, {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 409) {
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
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Delete the code.
   *
   * @param {string} codeCategoryId The ID used to uniquely identify the code category the code is
   *                                associated with.
   * @param {string} codeId         The ID used to uniquely identify the code.
   *
   * @return True if the code was deleted or false otherwise.
   */
  deleteCode(codeCategoryId: string, codeId: string): Observable<boolean> {

    return this.httpClient.delete<boolean>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + codeCategoryId + '/' + codeId,
      {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
            return throwError(new CodeCategoryNotFoundError(this.i18n({
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
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Delete the code category.
   *
   * @param {string} codeCategoryId The ID used to uniquely identify the code category.
   *
   * @return True if the code category was deleted or false otherwise.
   */
  deleteCodeCategory(codeCategoryId: string): Observable<boolean> {

    return this.httpClient.delete<boolean>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + codeCategoryId,
      {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
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
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Retrieve the code categories.
   *
   * @return {Observable<CodeCategory[]>} The list of code categories.
   */
  getCodeCategories(): Observable<CodeCategory[]> {

    return this.httpClient.get<CodeCategory[]>(
      environment.codesServiceUrlPrefix + '/codeCategories', {reportProgress: true}).pipe(
      map((codeCategories: CodeCategory[]) => {

        return codeCategories;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code_categories',
            value: 'Failed to retrieve the code categories.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Retrieve the codes for the code category.
   *
   * @param {string} codeCategoryId The ID used to uniquely identify the code category.
   *
   * @return the codes for the code category
   */
  getCodeCategoryCodes(codeCategoryId: string): Observable<Code[]> {

    return this.httpClient.get<Code[]>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + codeCategoryId + '/codes',
      {reportProgress: true}).pipe(
      map((codes: Code[]) => {

        return codes;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
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
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Update the code.
   *
   * @param {Code} code The code to update.
   *
   * @return True if the code was updated successfully or false otherwise.
   */
  updateCode(code: Code): Observable<boolean> {

    return this.httpClient.post<boolean>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + code.codeCategoryId + '/' + code.id,
      code, {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
            return throwError(new CodeCategoryNotFoundError(this.i18n({
              id: '@@codes_service_the_code_could_not_be_found',
              value: 'The code could not be found.'
            }), apiError));
          } else {
            return throwError(new CodesServiceError(this.i18n({
              id: '@@codes_service_failed_to_create_the_code',
              value: 'Failed to update the code.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }

  /**
   * Update the code category.
   *
   * @param {CodeCategory} codeCategory The code category to update.
   *
   * @return True if the code category was updated successfully or false otherwise.
   */
  updateCodeCategory(codeCategory: CodeCategory): Observable<boolean> {

    return this.httpClient.post<boolean>(
      environment.codesServiceUrlPrefix + '/codeCategories/' + codeCategory.id,
      codeCategory, {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {

        return true;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status == 404) {
            return throwError(new CodeCategoryNotFoundError(this.i18n({
              id: '@@codes_service_the_code_category_could_not_be_found',
              value: 'The code category could not be found.'
            }), apiError));
          } else {
            return throwError(new CodesServiceError(this.i18n({
              id: '@@codes_service_failed_to_create_the_code_category',
              value: 'Failed to update the code category.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }
}

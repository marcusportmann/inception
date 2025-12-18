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

import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ResponseConverter, ServiceUnavailableError
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Code } from './code';
import { CodeCategory } from './code-category';
import { CodeCategorySummary } from './code-category-summary';
import {
  CodeCategoryNotFoundError, CodeNotFoundError, DuplicateCodeCategoryError, DuplicateCodeError
} from './codes.service.errors';

/**
 * The Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class CodesService {
  private config = inject<InceptionConfig>(INCEPTION_CONFIG);

  private httpClient = inject(HttpClient);

  /**
   * Constructs a new CodesService.
   */
  constructor() {
    console.log('Initializing the Codes Service');
  }

  /**
   * Create a code.
   *
   * @param code The code to create.
   *
   * @return True if the code was created successfully or false otherwise.
   */
  createCode(code: Code): Observable<boolean> {
    return this.httpClient
      .post<boolean>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          code.codeCategoryId
        )}/codes`,
        code,
        { observe: 'response' }
      )
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) => CodesService.handleApiError(error, 'Failed to create the code.'))
      );
  }

  /**
   * Create a code category.
   *
   * @param codeCategory The code category to create.
   *
   * @return True if the code category was created successfully or false otherwise.
   */
  createCodeCategory(codeCategory: CodeCategory): Observable<boolean> {
    return this.httpClient
      .post<boolean>(`${this.config.apiUrlPrefix}/codes/code-categories`, codeCategory, {
        observe: 'response'
      })
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to create the code category.')
        )
      );
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId The ID for the code category the code is associated with.
   * @param codeId         The ID for the code.
   *
   * @return True if the code was deleted or false otherwise.
   */
  deleteCode(codeCategoryId: string, codeId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          codeCategoryId
        )}/codes/${encodeURIComponent(codeId)}`,
        { observe: 'response' }
      )
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) => CodesService.handleApiError(error, 'Failed to delete the code.'))
      );
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId The ID for the code category.
   *
   * @return True if the code category was deleted or false otherwise.
   */
  deleteCodeCategory(codeCategoryId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(codeCategoryId)}`,
        { observe: 'response' }
      )
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to delete the code category.')
        )
      );
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId The ID for the code category the code is associated with.
   * @param codeId         The ID for the code.
   *
   * @return The code.
   */
  getCode(codeCategoryId: string, codeId: string): Observable<Code> {
    return this.httpClient
      .get<Code>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          codeCategoryId
        )}/codes/${encodeURIComponent(codeId)}`,
        { reportProgress: true }
      )
      .pipe(
        catchError((error) => CodesService.handleApiError(error, 'Failed to retrieve the code.'))
      );
  }

  /**
   * Retrieve all the code categories.
   *
   * @return The code categories.
   */
  @ResponseConverter getCodeCategories(): Observable<CodeCategory[]> {
    return this.httpClient
      .get<
        CodeCategory[]
      >(`${this.config.apiUrlPrefix}/codes/code-categories`, { reportProgress: true })
      .pipe(
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to retrieve the code categories.')
        )
      );
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId The ID for the code category.
   *
   * @return The code category.
   */
  @ResponseConverter getCodeCategory(codeCategoryId: string): Observable<CodeCategory> {
    return this.httpClient
      .get<CodeCategory>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(codeCategoryId)}`,
        { reportProgress: true }
      )
      .pipe(
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to retrieve the code category.')
        )
      );
  }

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId The ID for the code category.
   *
   * @return The name of the code category.
   */
  getCodeCategoryName(codeCategoryId: string): Observable<string> {
    return this.httpClient
      .get<string>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          codeCategoryId
        )}/name`,
        { reportProgress: true }
      )
      .pipe(
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to retrieve the code category name.')
        )
      );
  }

  /**
   * Retrieve the summaries for all code categories.
   *
   * @return The code category summaries.
   */
  @ResponseConverter getCodeCategorySummaries(): Observable<CodeCategorySummary[]> {
    return this.httpClient
      .get<
        CodeCategorySummary[]
      >(`${this.config.apiUrlPrefix}/codes/code-category-summaries`, { reportProgress: true })
      .pipe(
        catchError((error) =>
          CodesService.handleApiError(
            error,
            'Failed to retrieve the summaries for the code categories.'
          )
        )
      );
  }

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId The ID for the code category the code is associated with.
   * @param codeId         The ID for the code.
   *
   * @return The name of the code.
   */
  getCodeName(codeCategoryId: string, codeId: string): Observable<string> {
    return this.httpClient
      .get<string>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          codeCategoryId
        )}/codes/${encodeURIComponent(codeId)}/name`,
        { reportProgress: true }
      )
      .pipe(
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to retrieve the code name.')
        )
      );
  }

  /**
   * Retrieve the codes for the code category.
   *
   * @param codeCategoryId The ID for the code category.
   *
   * @return The codes for the code category.
   */
  getCodesForCodeCategory(codeCategoryId: string): Observable<Code[]> {
    return this.httpClient
      .get<
        Code[]
      >(`${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(codeCategoryId)}/codes`, { reportProgress: true })
      .pipe(
        catchError((error) => CodesService.handleApiError(error, 'Failed to retrieve the codes.'))
      );
  }

  /**
   * Update the code.
   *
   * @param code The code to update.
   *
   * @return True if the code was updated successfully or false otherwise.
   */
  updateCode(code: Code): Observable<boolean> {
    return this.httpClient
      .put<boolean>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(
          code.codeCategoryId
        )}/codes/${encodeURIComponent(code.id)}`,
        code,
        { observe: 'response' }
      )
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) => CodesService.handleApiError(error, 'Failed to update the code.'))
      );
  }

  /**
   * Update the code category.
   *
   * @param codeCategory The code category to update.
   *
   * @return True if the code category was updated successfully or false otherwise.
   */
  updateCodeCategory(codeCategory: CodeCategory): Observable<boolean> {
    return this.httpClient
      .put<boolean>(
        `${this.config.apiUrlPrefix}/codes/code-categories/${encodeURIComponent(codeCategory.id)}`,
        codeCategory,
        { observe: 'response' }
      )
      .pipe(
        map(CodesService.isResponse204),
        catchError((error) =>
          CodesService.handleApiError(error, 'Failed to update the code category.')
        )
      );
  }

  private static handleApiError(
    httpErrorResponse: HttpErrorResponse,
    defaultMessage: string
  ): Observable<never> {
    if (ProblemDetails.isProblemDetails(httpErrorResponse, CodeCategoryNotFoundError.TYPE)) {
      return throwError(() => new CodeCategoryNotFoundError(httpErrorResponse));
    } else if (ProblemDetails.isProblemDetails(httpErrorResponse, CodeNotFoundError.TYPE)) {
      return throwError(() => new CodeNotFoundError(httpErrorResponse));
    } else if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateCodeError.TYPE)) {
      return throwError(() => new DuplicateCodeError(httpErrorResponse));
    } else if (
      ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateCodeCategoryError.TYPE)
    ) {
      return throwError(() => new DuplicateCodeCategoryError(httpErrorResponse));
    } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
      return throwError(() => new AccessDeniedError(httpErrorResponse));
    } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
      return throwError(() => new CommunicationError(httpErrorResponse));
    } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
      return throwError(() => new InvalidArgumentError(httpErrorResponse));
    }

    return throwError(() => new ServiceUnavailableError(defaultMessage, httpErrorResponse));
  }

  private static isResponse204(httpResponse: HttpResponse<boolean>): boolean {
    return httpResponse.status === 204;
  }
}

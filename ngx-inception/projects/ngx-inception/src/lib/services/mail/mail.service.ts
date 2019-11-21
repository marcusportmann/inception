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

import {Inject, Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ApiError} from '../../errors/api-error';
import {
  DuplicateMailTemplateError, MailServiceError, MailTemplateNotFoundError
} from './mail.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {MailTemplate} from './mail-template';
import {MailTemplateSummary} from './mail-template-summary';
import {INCEPTION_CONFIG} from '../../inception.module';
import {InceptionConfig} from '../../inception-config';

/**
 * The Mail Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class MailService {

  /**
   * Constructs a new MailService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Mail Service');
  }

  /**
   * Create a mail template.
   *
   * @param mailTemplate The mail template to create.
   *
   * @return True if the mail template was created successfully or false otherwise.
   */
  createMailTemplate(mailTemplate: MailTemplate): Observable<boolean> {
    return this.httpClient.post<boolean>(this.config.mailApiUrlPrefix + '/mail-templates',
      mailTemplate, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'MailTemplateNotFoundError') {
            return throwError(new MailTemplateNotFoundError(this.i18n, apiError));
          } else if (apiError.code === 'DuplicateMailTemplateError') {
            return throwError(new DuplicateMailTemplateError(this.i18n, apiError));
          } else {
            return throwError(new MailServiceError(this.i18n({
              id: '@@mail_create_mail_template_error',
              value: 'Failed to create the mail template.'
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
   * Delete the mail template.
   *
   * @param mailTemplateId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template.
   *
   * @return True if the mail template was deleted or false otherwise.
   */
  deleteMailTemplate(mailTemplateId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.mailApiUrlPrefix + '/mail-templates/' + encodeURIComponent(mailTemplateId),
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'MailTemplateNotFoundError') {
            return throwError(new MailTemplateNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new MailServiceError(this.i18n({
              id: '@@mail_delete_mail_template_error',
              value: 'Failed to delete the mail template.'
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
   * Retrieve the mail template.
   *
   * @param mailTemplateId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       template.
   *
   * @return The mail template.
   */
  getMailTemplate(mailTemplateId: string): Observable<MailTemplate> {
    return this.httpClient.get<MailTemplate>(
      this.config.mailApiUrlPrefix + '/mail-templates/' + encodeURIComponent(mailTemplateId),
      {reportProgress: true})
      .pipe(map((mailTemplate: MailTemplate) => {
        return mailTemplate;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'MailTemplateNotFoundError') {
            return throwError(new MailTemplateNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new MailServiceError(this.i18n({
              id: '@@mail_get_mail_template_error',
              value: 'Failed to retrieve the mail template.'
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
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template.
   *
   * @return The name of the mail template.
   */
  getMailTemplateName(mailTemplateId: string): Observable<string> {
    return this.httpClient.get<string>(
      this.config.mailApiUrlPrefix + '/mail-templates/' + encodeURIComponent(mailTemplateId) +
      '/name', {
        reportProgress: true,
      }).pipe(map((mailTemplateName: string) => {
      return mailTemplateName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'MailTemplateNotFoundError') {
          return throwError(new MailTemplateNotFoundError(this.i18n, apiError));
        } else {
          return throwError(new MailServiceError(this.i18n({
            id: '@@mail_get_mail_template_name_error',
            value: 'Failed to retrieve the mail template name.'
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
   * Retrieve the summaries for all the mail templates.
   *
   * @return The summaries for all the mail templates.
   */
  getMailTemplateSummaries(): Observable<MailTemplateSummary[]> {
    return this.httpClient.get<MailTemplateSummary[]>(
      this.config.mailApiUrlPrefix + '/mail-template-summaries', {reportProgress: true})
      .pipe(map((mailTemplateSummaries: MailTemplateSummary[]) => {
        return mailTemplateSummaries;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new MailServiceError(this.i18n({
            id: '@@mail_get_mail_template_summaries_error',
            value: 'Failed to retrieve the summaries for the mail templates.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve all the mail templates.
   *
   * @return The mail templates.
   */
  getMailTemplates(): Observable<MailTemplate[]> {
    return this.httpClient.get<MailTemplate[]>(this.config.mailApiUrlPrefix + '/mail-templates',
      {reportProgress: true})
      .pipe(map((mailTemplates: MailTemplate[]) => {
        return mailTemplates;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new MailServiceError(this.i18n({
            id: '@@mail_get_mail_templates_error',
            value: 'Failed to retrieve the mail templates.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplate The mail template
   *
   * @return True if the mail template was updated successfully or false otherwise.
   */
  updateMailTemplate(mailTemplate: MailTemplate): Observable<boolean> {
    return this.httpClient.put<boolean>(
      this.config.mailApiUrlPrefix + '/mail-templates/' + encodeURIComponent(mailTemplate.id),
      mailTemplate, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'MailTemplateNotFoundError') {
            return throwError(new MailTemplateNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new MailServiceError(this.i18n({
              id: '@@mail_update_mail_template_error',
              value: 'Failed to update the mail template.'
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

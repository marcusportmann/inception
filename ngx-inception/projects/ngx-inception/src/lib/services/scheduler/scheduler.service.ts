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
import {Job} from './job';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ApiError} from '../../errors/api-error';
import {DuplicateJobError, JobNotFoundError, SchedulerServiceError} from './scheduler.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {INCEPTION_CONFIG} from '../../inception-config'
import {InceptionConfig} from '../../inception-config';

/**
 * The Scheduler Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class SchedulerService {

  /**
   * Constructs a new SchedulerService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Scheduler Service');
  }

  /**
   * Create a job.
   *
   * @param job The job to create.
   *
   * @return True if the job was created successfully or false otherwise.
   */
  createJob(job: Job): Observable<boolean> {
    return this.httpClient.post<boolean>(this.config.schedulerApiUrlPrefix + '/jobs', job, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'JobNotFoundError') {
            return throwError(new JobNotFoundError(this.i18n, apiError));
          } else if (apiError.code === 'DuplicateJobError') {
            return throwError(new DuplicateJobError(this.i18n, apiError));
          } else {
            return throwError(new SchedulerServiceError(this.i18n({
              id: '@@scheduler_create_job_error',
              value: 'Failed to create the job.'
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
   * Delete the job.
   *
   * @param jobId The Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @return True if the job was deleted or false otherwise.
   */
  deleteJob(jobId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.config.schedulerApiUrlPrefix + '/jobs/' + encodeURIComponent(jobId), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'JobNotFoundError') {
            return throwError(new JobNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new SchedulerServiceError(this.i18n({
              id: '@@scheduler_delete_job_error',
              value: 'Failed to delete the job.'
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
   * Retrieve the job.
   *
   * @param jobId The Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @return The job.
   */
  getJob(jobId: string): Observable<Job> {
    return this.httpClient.get<Job>(this.config.schedulerApiUrlPrefix + '/jobs/' + encodeURIComponent(jobId), {reportProgress: true})
      .pipe(map((job: Job) => {
        return job;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'JobNotFoundError') {
            return throwError(new JobNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new SchedulerServiceError(this.i18n({
              id: '@@scheduler_get_job_error',
              value: 'Failed to retrieve the job.'
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
   * Retrieve the name of the job.
   *
   * @param jobId The Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @return The name of the job.
   */
  getJobName(jobId: string): Observable<string> {
    return this.httpClient.get<string>(this.config.schedulerApiUrlPrefix + '/jobs/' + encodeURIComponent(jobId) + '/name', {
      reportProgress: true,
    }).pipe(map((jobName: string) => {
      return jobName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'JobNotFoundError') {
          return throwError(new JobNotFoundError(this.i18n, apiError));
        } else {
          return throwError(new SchedulerServiceError(this.i18n({
            id: '@@scheduler_get_job_name_error',
            value: 'Failed to retrieve the job name.'
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
   * Retrieve all the jobs.
   *
   * @return The jobs.
   */
  getJobs(): Observable<Job[]> {
    return this.httpClient.get<Job[]>(this.config.schedulerApiUrlPrefix + '/jobs', {reportProgress: true})
      .pipe(map((jobs: Job[]) => {
        return jobs;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new SchedulerServiceError(this.i18n({
            id: '@@scheduler_get_jobs_error',
            value: 'Failed to retrieve the jobs.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Update the job.
   *
   * @param job The job.
   *
   * @return True if the job was updated successfully or false otherwise.
   */
  updateJob(job: Job): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.schedulerApiUrlPrefix + '/jobs/' + encodeURIComponent(job.id), job,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'JobNotFoundError') {
          return throwError(new JobNotFoundError(this.i18n, apiError));
        } else {
          return throwError(new SchedulerServiceError(this.i18n({
            id: '@@scheduler_update_job_error',
            value: 'Failed to update the job.'
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

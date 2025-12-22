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

import {HttpErrorResponse} from '@angular/common/http';
import {HttpError} from './http-error';
import {ProblemDetails} from './problem-details';

/**
 * The base class that all framework error classes should be derived from.
 *
 * @author Marcus Portmann
 */
export class FrameworkError extends globalThis.Error {
  /**
   * The cause of the error.
   */
  override cause?: unknown;

  /**
   * The date and time the error occurred.
   */
  timestamp: Date;

  /**
   * Constructs a new FrameworkError.
   *
   * @param message The error message.
   * @param cause   The cause of the error.
   */
  constructor(message: string, cause?: unknown) {
    // Passing ErrorOptions is safe in modern runtimes; older runtimes simply ignore extra args.
    super(
      message,
      cause !== undefined ? { cause: FrameworkError.normalizeCause(cause) } : undefined
    );

    this.name = new.target.name; // e.g. "AccessDeniedError"

    this.timestamp = new Date();

    // If runtime didn't set cause (older engines), set it manually.
    if (cause !== undefined && this.cause === undefined) {
      this.cause = FrameworkError.normalizeCause(cause);
    }

    // Maintain a useful timestamp if cause is ProblemDetails
    if (this.cause instanceof ProblemDetails) {
      this.timestamp = this.cause.timestamp;
    }
  }

  private static normalizeCause(cause: unknown): unknown {
    if (cause instanceof HttpErrorResponse) {
      if (ProblemDetails.isProblemDetails(cause)) {
        return new ProblemDetails(cause);
      }

      // HttpError mapping behavior:
      const e: unknown = cause.error;

      const obj: Record<string, unknown> =
        typeof e === 'object' && e !== null ? (e as Record<string, unknown>) : {};

      const error = typeof obj['error'] === 'string' ? obj['error'] : '';

      const errorDescription =
        typeof obj['error_description'] === 'string' ? obj['error_description'] : '';

      const statusText = FrameworkError.statusTextFromCode(cause.status);

      return new HttpError(
        error,
        errorDescription,
        cause.message,
        cause.status,
        statusText,
        cause.url ?? ''
      );
    }

    return cause;
  }

  private static statusTextFromCode(status: number): string {
    switch (status) {
      case 400:
        return 'Bad Request';
      case 401:
        return 'Unauthorized';
      case 403:
        return 'Forbidden';
      case 404:
        return 'Not Found';
      case 409:
        return 'Conflict';
      case 500:
        return 'Internal Server Error';
      case 503:
        return 'Service Unavailable';
      default:
        return '';
    }
  }
}

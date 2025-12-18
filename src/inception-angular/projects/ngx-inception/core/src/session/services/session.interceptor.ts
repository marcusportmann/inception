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

import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap, take } from 'rxjs/operators';
import { SessionService } from './session.service';

/**
 * The SessionInterceptor class implements an Angular HTTP interceptor, which injects the OAuth2
 * access token associated with the current active user session into HTTP requests as an
 * Authorization header.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SessionInterceptor implements HttpInterceptor {
  private static readonly DEFAULT_TENANT_ID = '00000000-0000-0000-0000-000000000000';

  private static readonly TOKEN_ENDPOINT_SUFFIX = '/oauth/token';

  private readonly sessionService = inject(SessionService);

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Skip attaching headers for the token endpoint
    if (this.shouldBypass(req)) {
      return next.handle(req);
    }

    return this.sessionService.session$.pipe(
      take(1),
      switchMap((session) => {
        // If there is no active session, just pass the request through
        if (!session) {
          return next.handle(req);
        }

        const tenantId = session.tenantId ?? SessionInterceptor.DEFAULT_TENANT_ID;

        const authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${session.accessToken}`,
            'Tenant-ID': tenantId
          }
        });

        return next.handle(authReq);
      })
    );
  }

  private shouldBypass(req: HttpRequest<unknown>): boolean {
    return req.url.endsWith(SessionInterceptor.TOKEN_ENDPOINT_SUFFIX);
  }
}

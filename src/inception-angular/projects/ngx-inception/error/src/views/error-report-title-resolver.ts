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

import {Inject, Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable, of} from 'rxjs';
import {ErrorService} from '../services/error.service';

/**
 * The ErrorReportTitleResolver class provides the route data resolver that resolves the
 * title for the "Error Report" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ErrorReportTitleResolver {

  /**
   * Constructs a new ErrorReportTitleResolver.
   *
   * @param errorService The error service.
   */
  constructor(@Inject(ErrorService) private errorService: ErrorService) {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activated route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
          routerStateSnapshot: RouterStateSnapshot): Observable<string> {
    let errorReportId = activatedRouteSnapshot.paramMap.get('errorReportId');

    if (!errorReportId) {
      throw (new Error('No errorReportId route parameter found'));
    }

    errorReportId = decodeURIComponent(errorReportId);

    return of(errorReportId);
    //return this.codesService.getErrorReportName(codeCategoryId);
  }
}

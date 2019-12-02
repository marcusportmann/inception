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

import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {ReportingService} from '../services/reporting.service';
import {Inject} from '@angular/core';

/**
 * The ReportDefinitionTitleResolver class provides the route data resolver that resolves the
 * title for the "Report Definition" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
export class ReportDefinitionTitleResolver implements Resolve<string> {

  /**
   * Constructs a new ReportDefinitionTitleResolver.
   *
   * @param reportingService The reporting service.
   */
  constructor(@Inject(ReportingService) private reportingService: ReportingService) {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activate route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
          routerStateSnapshot: RouterStateSnapshot): Observable<string> {
    let reportDefinitionId = activatedRouteSnapshot.paramMap.get('reportDefinitionId');

    if (!reportDefinitionId) {
      throw(new Error('No reportDefinitionId route parameter found'));
    }

    reportDefinitionId = decodeURIComponent(reportDefinitionId);

    return this.reportingService.getReportDefinitionName(reportDefinitionId);
  }
}

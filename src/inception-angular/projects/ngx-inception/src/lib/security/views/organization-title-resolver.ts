/*
 * Copyright 2020 Marcus Portmann
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
import { Inject, Injectable } from '@angular/core';
import {SecurityService} from '../services/security.service';

/**
 * The OrganizationTitleResolver class provides the route data resolver that resolves the
 * title for the "Organization" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class OrganizationTitleResolver implements Resolve<string> {

  /**
   * Constructs a new OrganizationTitleResolver.
   *
   * @param securityService The security service.
   */
  constructor(@Inject(SecurityService) private securityService: SecurityService) {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activate route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
          routerStateSnapshot: RouterStateSnapshot): Observable<string> {
    let organizationId = activatedRouteSnapshot.paramMap.get('organizationId');

    if (!organizationId) {
      throw(new Error('No organizationId route parameter found'));
    }

    organizationId = decodeURIComponent(organizationId);

    return this.securityService.getOrganizationName(organizationId);
  }
}

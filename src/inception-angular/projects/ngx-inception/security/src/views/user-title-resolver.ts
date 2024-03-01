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
import {Observable} from 'rxjs';
import {SecurityService} from '../services/security.service';

/**
 * The UserTitleResolver class provides the route data resolver that resolves the
 * title for the "User" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class UserTitleResolver {

  /**
   * Constructs a new UserTitleResolver.
   *
   * @param securityService The security service.
   */
  constructor(@Inject(SecurityService) private securityService: SecurityService) {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activated route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
          routerStateSnapshot: RouterStateSnapshot): Observable<string> {
    let userDirectoryId = activatedRouteSnapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw (new Error('No userDirectoryId route parameter found'));
    }

    userDirectoryId = decodeURIComponent(userDirectoryId);

    let username = activatedRouteSnapshot.paramMap.get('username');

    if (!username) {
      throw (new Error('No username route parameter found'));
    }

    username = decodeURIComponent(username);

    return this.securityService.getUserName(userDirectoryId, username);
  }
}

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

import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable, of} from 'rxjs';

/**
 * The ConfigTitleResolver class provides the route data resolver that resolves the title for the
 * "Config" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ConfigTitleResolver {

  /**
   * Constructs a new ConfigTitleResolver.
   */
  constructor() {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activated route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot,
          routerStateSnapshot: RouterStateSnapshot): Observable<string> {
    let id = activatedRouteSnapshot.paramMap.get('id');

    if (!id) {
      throw (new Error('No id route parameter found'));
    }

    id = decodeURIComponent(id);

    return of(id);
  }
}

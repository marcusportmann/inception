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

import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { CodesService } from '../services/codes.service';

/**
 * The CodeTitleResolver class provides the route data resolver that resolves the
 * title for the "Code" route in the navigation hierarchy.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class CodeTitleResolver {
  private readonly codesService = inject<CodesService>(CodesService);

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activated route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  // noinspection JSUnusedGlobalSymbols
  resolve(
    activatedRouteSnapshot: ActivatedRouteSnapshot,
    routerStateSnapshot: RouterStateSnapshot
  ): Observable<string> {
    // Mark parameters as used so TS doesn't complain
    void activatedRouteSnapshot;
    void routerStateSnapshot;

    const codeCategoryId = activatedRouteSnapshot.paramMap.get('codeCategoryId');

    if (!codeCategoryId) {
      throw new globalThis.Error('No codeCategoryId route parameter found');
    }

    const codeId = activatedRouteSnapshot.paramMap.get('codeId');

    if (!codeId) {
      throw new globalThis.Error('No codeId route parameter found');
    }

    return this.codesService.getCodeName(codeCategoryId, codeId);
  }
}

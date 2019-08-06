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

import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Observable, of} from "rxjs";

/**
 * The CodesTitleResolver class provides the route data resolver that resolves the
 * title for the "Codes" route in the navigation hierarchy.
 *
 * This is required since it is not possible to override a resolver provided title in a parent
 * route with a static title specified as part of a child route's data.
 *
 * @author Marcus Portmann
 */
export class CodesTitleResolver implements Resolve<string> {

  /**
   * Constructs a new CodeCategoryTitleResolver.
   *
   * @param i18n The internationalization service.
   */
  constructor() {
  }

  /**
   * Resolve the title.
   *
   * @param activatedRouteSnapshot The activate route snapshot.
   * @param routerStateSnapshot    The router state snapshot.
   */
  resolve(activatedRouteSnapshot: ActivatedRouteSnapshot, routerStateSnapshot: RouterStateSnapshot):
    Observable<string> {
    return of('Codes');
  }
}

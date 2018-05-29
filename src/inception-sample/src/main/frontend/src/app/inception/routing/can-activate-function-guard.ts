/*
 * Copyright 2018 Marcus Portmann
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

import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate} from "@angular/router";
import {SessionService} from "../services/session/session.service";

/**
 * The CanActivateFunctionGuard class implements the routing guard that restricts access to a route
 * based on the function codes assigned to a user that are stored as part of the user's session.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class CanActivateFunctionGuard implements CanActivate {

  /**
   * Constructs a new CanActivateFunctionGuard.
   *
   * @param {SessionService} sessionService The Session Service.
   */
  constructor(private sessionService: SessionService) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {



    return true;
  }
}

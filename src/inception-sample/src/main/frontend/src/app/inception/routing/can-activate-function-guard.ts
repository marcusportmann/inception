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
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {SessionService} from "../services/session/session.service";
import {Session} from "../services/session/session";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

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
   * @param {Router}         router         The router.
   */
  constructor(private sessionService: SessionService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {

    return this.sessionService.getSession().pipe(map((session: Session) => {

      if (route) {
        if (route.data) {
          if (route.data.functionCodes) {

            // TODO: Confirm that route.data.functionCodes is an array of strings -- MARCUS

            if (session) {
              for (var i = 0; i < route.data.functionCodes.length; i++) {
                for (var j = 0; j < session.functionCodes.length; j++) {
                  if (route.data.functionCodes[i] == session.functionCodes[j]) {
                    return true;
                  }
                }
              }

              this.router.navigate(['/login']);

              return false;
            }
            else {
              this.router.navigate(['/login']);

              return false;
            }
          }
          else {
            return true;
          }
        }
        else {
          return true;
        }
      }
      else {
        this.router.navigate(['/login']);

        return false;
      }
    }, error => {

      // TODO: Handle or log error -- MARCUS

      this.router.navigate(['/login']);

      return false;
    }));

  }
}

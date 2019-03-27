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

import {Injectable} from '@angular/core';
import {NavigationItem} from "./navigation-item";
import {BehaviorSubject} from "rxjs";
import {SessionService} from "../session/session.service";
import {map} from "rxjs/operators";
import {Session} from "../session/session";

/**
 * The NavigationService class provides the Navigation Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class NavigationService {

  userNavigation: BehaviorSubject<NavigationItem[]>  = new BehaviorSubject<Object[]>([]);

  private navigation: NavigationItem[];

  /**
   * Constructs a new NavigationService.
   *
   * @param {SessionService} sessionService The session service.
   */
  constructor(private sessionService: SessionService) {
    console.log('Initializing the Navigation Service');
    
    this.sessionService.session.pipe(
      map((session: Session) => {
        this.userNavigation.next(Object.assign([],
          this.filterNavigationItems(this.navigation, session)));
      })
    ).subscribe();
  }

  /**
   * Initialize the navigation.
   *
   * @param {NavigationItem[]} navigation The navigation.
   */
  initNavigation(navigation: NavigationItem[]) {
    this.navigation = navigation;
    this.userNavigation.next(this.filterNavigationItems(navigation, null));
  }

  private filterNavigationItems(navigationItems: NavigationItem[], session: Session): NavigationItem[] {
    if (!navigationItems) {
      return navigationItems;
    }

    let filteredNavigationItems: NavigationItem[] = [];

    for (let i = 0; i < navigationItems.length; i++) {
      let navigationItem: NavigationItem = navigationItems[i];

      let functionCodes = (navigationItem.functionCodes == null) ? [] : navigationItem.functionCodes;

      if (functionCodes.length > 0) {
        if (session) {
          for (let j = 0; j < functionCodes.length; j++) {
            for (let k = 0; k < session.functionCodes.length; k++) {
              if (functionCodes[j] == session.functionCodes[k]) {
                let filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

                filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
                  navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
                  navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
              }
            }
          }
        }
      }
      else {
        let filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

        filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
          navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
          navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
      }
    }

    return filteredNavigationItems;
  }
}

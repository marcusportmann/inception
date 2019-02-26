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

import {
  Component,
  ElementRef,
  HostBinding,
  Input, 
  OnDestroy,
  OnInit,
} from '@angular/core';

import {Router} from '@angular/router';
import {NavigationService} from "../../services/navigation/navigation.service";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";
import {Subscription} from "rxjs";
import {map} from "rxjs/operators";

/**
 * The SidebarNavComponent class implements the sidebar nav component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'sidebar-nav',
  template: `
    <ul class="nav">
      <sidebar-nav-item *ngFor="let navItem of navItems" [navItem]="navItem"></sidebar-nav-item>
    </ul>`
})
export class SidebarNavComponent implements  OnInit, OnDestroy {

  navItems: NavigationItem[];

  private _sessionSubscription: Subscription;

  @HostBinding('class.sidebar-nav') true;

  @HostBinding('attr.role')
  role = 'nav';

  constructor(private navigationService: NavigationService, private sessionService: SessionService) {
    this.navItems = new Array<NavigationItem>();
  }

  ngOnDestroy(): void {
    if (this._sessionSubscription) {
      this._sessionSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this._sessionSubscription = this.sessionService.session.pipe(
      map((session: Session) => {
        this.navItems = this.filterNavigationItems(this.navigationService.getNavigation(), session);
      })
    ).subscribe();
  }

  private filterNavigationItems(navigationItems: NavigationItem[], session: Session): NavigationItem[] {

    if (!navigationItems) {
      return navigationItems;
    }

    var filteredNavigationItems: NavigationItem[] = [];

    for (var i = 0; i < navigationItems.length; i++) {

      var navigationItem: NavigationItem = navigationItems[i];

      var functionCodes = (navigationItem.functionCodes == null) ? [] : navigationItem.functionCodes;

      if (functionCodes.length > 0) {

        if (session) {

          for (var j = 0; j < functionCodes.length; j++) {
            for (var k = 0; k < session.functionCodes.length; k++) {
              if (functionCodes[j] == session.functionCodes[k]) {

                var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

                filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
                  navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
                  navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
              }
            }
          }
        }
      }
      else {

        var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

        filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
          navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
          navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
      }
    }

    return filteredNavigationItems;
  }
}



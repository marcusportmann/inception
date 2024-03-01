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

import {Component, HostBinding, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {NavigationItem} from '../services/navigation-item';
import {NavigationService} from '../services/navigation.service';

/**
 * The SidebarNavComponent class implements the sidebar nav component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-nav',
  template: `
      <ul class="nav">
          <sidebar-nav-item *ngFor="let navItem of navItems" [navItem]="navItem"></sidebar-nav-item>
      </ul>`
})
export class SidebarNavComponent implements OnInit, OnDestroy {

  navItems: NavigationItem[];

  @HostBinding('attr.role') role = 'nav';

  private userNavigationSubscription?: Subscription;

  /**
   * Constructs a new SidebarNavComponent.
   *
   * @param navigationService The navigation service.
   */
  constructor(private navigationService: NavigationService) {
    this.navItems = new Array<NavigationItem>();
  }

  ngOnDestroy(): void {
    if (this.userNavigationSubscription) {
      this.userNavigationSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.userNavigationSubscription =
      this.navigationService.userNavigation$.subscribe((navigation: NavigationItem[]) => {
        this.navItems = navigation;
      });
  }

  @HostBinding('class.sidebar-nav') sidebarNav() {
    return true;
  }
}



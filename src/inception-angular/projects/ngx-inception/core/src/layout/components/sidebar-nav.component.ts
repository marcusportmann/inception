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

import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  HostBinding,
  inject,
  OnDestroy,
  OnInit
} from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NavigationItem } from '../services/navigation-item';
import { NavigationService } from '../services/navigation.service';
import { SidebarNavItemComponent } from './sidebar-nav-item.component';

/**
 * The SidebarNavComponent class implements the sidebar nav component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-nav',
  standalone: true,
  template: ` <ul class="nav">
    @for (navItem of navItems; track navItem) {
      <sidebar-nav-item [navItem]="navItem"></sidebar-nav-item>
    }
  </ul>`,
  imports: [SidebarNavItemComponent],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarNavComponent implements OnInit, OnDestroy {
  navItems: NavigationItem[];

  @HostBinding('attr.role') role = 'nav';

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private readonly navigationService = inject(NavigationService);

  private readonly router = inject(Router);

  private routerEventSubscription?: Subscription;

  private userNavigationSubscription?: Subscription;

  /**
   * Constructs a new SidebarNavComponent.
   */
  constructor() {
    this.navItems = new Array<NavigationItem>();
  }

  ngOnDestroy(): void {
    if (this.userNavigationSubscription) {
      this.userNavigationSubscription.unsubscribe();
    }

    if (this.routerEventSubscription) {
      this.routerEventSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.userNavigationSubscription = this.navigationService.userNavigation$.subscribe(
      (navigation: NavigationItem[]) => {
        this.navItems = navigation;
        this.changeDetectorRef.detectChanges();
      }
    );

    this.routerEventSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  @HostBinding('class.sidebar-nav') sidebarNav() {
    return true;
  }
}

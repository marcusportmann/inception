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

import {AsyncPipe} from '@angular/common';
import {ChangeDetectionStrategy, Component, HostBinding, inject} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {combineLatest} from 'rxjs';
import {filter, map, startWith} from 'rxjs/operators';
import {NavigationItem} from '../services/navigation-item';
import {NavigationService} from '../services/navigation.service';
import {SidebarNavItemComponent} from './sidebar-nav-item.component';

/**
 * The SidebarNavComponent class implements the sidebar nav component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-nav',
  standalone: true,
  imports: [SidebarNavItemComponent, AsyncPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="nav">
      @for (navItem of navItems$ | async; track navItem) {
        <sidebar-nav-item [navItem]="navItem"></sidebar-nav-item>
      }
    </ul>
  `
})
export class SidebarNavComponent {
  @HostBinding('attr.role') role = 'nav';

  @HostBinding('class.sidebar-nav') sidebarNav = true;

  private readonly _navigationService = inject(NavigationService);

  private readonly _router = inject(Router);

  // Emits on nav tree changes OR navigation end so dropdown "open" states refresh
  readonly navItems$ = combineLatest([
    this._navigationService.userNavigation$,
    this._router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      startWith(null)
    )
  ]).pipe(map(([items]) => items as NavigationItem[]));
}

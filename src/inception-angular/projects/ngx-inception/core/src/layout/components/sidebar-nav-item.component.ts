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

import {NgClass, NgTemplateOutlet} from '@angular/common';
import {ChangeDetectionStrategy, Component, inject, Input} from '@angular/core';
import {IsActiveMatchOptions, Router, RouterLink, RouterLinkActive} from '@angular/router';
import {
  SidebarNavDropdownTogglerDirective
} from '../directives/sidebar-nav-dropdown-toggler.directive';
import {SidebarNavDropdownDirective} from '../directives/sidebar-nav-dropdown.directive';
import {NavigationItem} from '../services/navigation-item';
import {SidebarService} from '../services/sidebar.service';

/**
 * The SidebarNavItemComponent class implements the sidebar nav item component.
 *
 * @author Marcus Portmann
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective,
    NgClass,
    RouterLink,
    RouterLinkActive,
    NgTemplateOutlet
  ],
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-nav-item',
  standalone: true,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }'],
  template: `
    @if (navItem; as item) {
      @if (item.divider) {
        <li class="nav-divider"></li>
      }

      @if (!item.divider && item.title) {
        <li class="nav-title">{{ item.name }}</li>
      }

      @if (!item.divider && !item.title && item.children.length > 0) {
        <li
          [ngClass]="['nav-item', 'nav-dropdown', item.cssClass || '']"
          [class.open]="isActive(item)"
          sidebarNavDropdown>
          <div class="sidebar-nav-dropdown">
            <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
              <ng-container
                *ngTemplateOutlet="navContent; context: { $implicit: item }"></ng-container>
            </a>
            <ul class="nav-dropdown-items">
              @for (child of item.children; track child) {
                <sidebar-nav-item [navItem]="child"></sidebar-nav-item>
              }
            </ul>
          </div>
        </li>
      } @else if (!item.divider && !item.title) {
        <li [ngClass]="['nav-item', item.cssClass || '']">
          @if (!isExternalLink(item)) {
            <a
              [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
              [routerLink]="[item.url]"
              [state]="{ resetState: true }"
              routerLinkActive="active"
              (click)="hideMobile()">
              <ng-container
                *ngTemplateOutlet="navContent; context: { $implicit: item }"></ng-container>
            </a>
          } @else {
            <a
              [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
              [href]="item.url">
              <ng-container
                *ngTemplateOutlet="navContent; context: { $implicit: item }"></ng-container>
            </a>
          }
        </li>
      }

      <ng-template #navContent let-item>
        @if (item.icon) {
          <i class="nav-icon {{ item.icon }}"></i>
        }
        <span class="nav-item-name">{{ item.name }}</span>
        @if (item.badge) {
          <span class="badge" [ngClass]="'badge-' + item.badge.variant">
            {{ item.badge.text }}
          </span>
        }
      </ng-template>
    }
  `
})
export class SidebarNavItemComponent {
  @Input() navItem?: NavigationItem;

  private readonly _matchOptions: IsActiveMatchOptions = {
    paths: 'subset',
    queryParams: 'subset',
    fragment: 'ignored',
    matrixParams: 'ignored'
  };

  private readonly _router = inject(Router);

  private readonly _sidebarService = inject(SidebarService);

  hideMobile(): void {
    this._sidebarService.hideMobile();
  }

  /** Dropdown open state: active if its own url is active OR any child is active. */
  isActive(item: NavigationItem): boolean {
    const own = this.isUrlActive(item.url);
    if (own) return true;

    if (item.children?.length) {
      return item.children.some((c) => this.isActive(c));
    }

    return false;
  }

  isExternalLink(item: NavigationItem): boolean {
    const url = item.url ?? '';
    return /^https?:\/\//i.test(url);
  }

  private isUrlActive(url?: string): boolean {
    if (!url) return false;
    if (/^https?:\/\//i.test(url)) return false; // external doesn't participate

    return this._router.isActive(this._router.parseUrl(url), this._matchOptions);
  }
}

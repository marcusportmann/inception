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

import { NgClass, NgForOf, NgIf, NgTemplateOutlet } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input
} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import {
  SidebarNavDropdownTogglerDirective
} from '../directives/sidebar-nav-dropdown-toggler.directive';
import { SidebarNavDropdownDirective } from '../directives/sidebar-nav-dropdown.directive';
import { NavigationItem } from '../services/navigation-item';

/**
 * The SidebarNavItemComponent class implements the sidebar nav item component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-nav-item',
  standalone: true,
  template: `
    <ng-container *ngIf="navItem as item">
      <!-- Divider -->
      <li *ngIf="item.divider" class="nav-divider"></li>

      <!-- Title -->
      <li *ngIf="!item.divider && item.title" class="nav-title">
        {{ item.name }}
      </li>

      <!-- Dropdown -->
      <li
        *ngIf="!item.divider && !item.title && item.children?.length; else sidebarNavLink"
        [ngClass]="['nav-item', 'nav-dropdown', item.cssClass || '']"
        [class.open]="isActive()"
        sidebarNavDropdown>
        <div class="sidebar-nav-dropdown">
          <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
            <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
            </ng-container>
          </a>
          <ul class="nav-dropdown-items">
            <sidebar-nav-item *ngFor="let child of item.children" [navItem]="child">
            </sidebar-nav-item>
          </ul>
        </div>
      </li>

      <!-- Simple link (internal / external) -->
      <ng-template #sidebarNavLink>
        <li [ngClass]="['nav-item', item.cssClass || '']">
          <!-- Internal route -->
          <a
            *ngIf="!isExternalLink; else externalLink"
            [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
            [routerLink]="[item.url]"
            [state]="{ resetState: true }"
            routerLinkActive="active"
            (click)="hideMobile()">
            <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
            </ng-container>
          </a>

          <!-- External link -->
          <ng-template #externalLink>
            <a
              [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
              [href]="item.url">
              <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
              </ng-container>
            </a>
          </ng-template>
        </li>
      </ng-template>

      <!-- Shared icon / text / badge template -->
      <ng-template #navContent let-item>
        <i *ngIf="item.icon" class="nav-icon {{ item.icon }}"></i>
        <span class="nav-item-name">{{ item.name }}</span>
        <span *ngIf="item.badge" class="badge" [ngClass]="'badge-' + item.badge.variant">
          {{ item.badge.text }}
        </span>
      </ng-template>
    </ng-container>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }'],
  imports: [
    SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective,
    NgClass,
    RouterLink,
    RouterLinkActive,
    NgIf,
    NgTemplateOutlet,
    NgForOf
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarNavItemComponent {
  @Input() navItem?: NavigationItem;

  constructor(private router: Router) {}

  hideMobile(): void {
    document.body.classList.remove('sidebar-show');
  }

  isActive(): boolean {
    const url = this.navItem?.url;
    return !!url && this.router.isActive(url, false);
  }

  get isExternalLink(): boolean {
    const url = this.navItem?.url ?? '';
    // Handles http and https, case-insensitive
    return /^https?:\/\//i.test(url);
  }
}

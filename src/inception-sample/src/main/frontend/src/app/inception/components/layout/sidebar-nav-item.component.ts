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

import {Component, ElementRef, Input} from '@angular/core';
import {NavigationItem} from '../../services/navigation/navigation-item';
import {Router} from '@angular/router';

/**
 * The SidebarNavItemComponent class implements the sidebar nav item component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'sidebar-nav-item',
  template: `
    <ng-container *ngIf="isDivider(); else checkForTitle">
      <li class="nav-divider"></li>
    </ng-container>
    <ng-template #checkForTitle>
      <ng-container *ngIf="isTitle(); else checkForDropdown">
        <li class="nav-title">{{navItem.name}}</li>
      </ng-container>
    </ng-template>
    <ng-template #checkForDropdown>
      <ng-container *ngIf="isDropdown(); else sidebarNavLink">
        <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + navItem.cssClass : 'nav-item nav-dropdown'"
            [class.open]="isActive()"
            routerLinkActive="open"
            sidebarNavDropdown>
          <sidebar-nav-dropdown [navItem]='navItem'></sidebar-nav-dropdown>
        </li>
      </ng-container>
    </ng-template>
    <ng-template #sidebarNavLink>
      <li [ngClass]="hasClass() ? 'nav-item ' + navItem.cssClass : 'nav-item'">
        <a *ngIf="!isExternalLink(); else externalLink"
           [ngClass]="hasVariant() ? 'nav-link nav-link-' + navItem.variant : 'nav-link'"
           routerLinkActive="active"
           [routerLink]="[navItem.url]"
           (click)="hideMobile()">
          <i *ngIf="hasIcon()" class="nav-icon {{ navItem.icon }}"></i>
          {{ navItem.name }}
          <span *ngIf="hasBadge()" [ngClass]="'badge badge-' + navItem.badge.variant">{{ navItem.badge.text }}</span>
        </a>
        <ng-template #externalLink>
          <a [ngClass]="hasVariant() ? 'nav-link nav-link-' + navItem.variant : 'nav-link'" href="{{navItem.url}}">
            <i *ngIf="hasIcon()" class="nav-icon {{ navItem.icon }}"></i>
            {{ navItem.name }}
            <span *ngIf="hasBadge()" [ngClass]="'badge badge-' + navItem.badge.variant">{{ navItem.badge.text }}</span>
          </a>
        </ng-template>
      </li>
    </ng-template>
  `
})
export class SidebarNavItemComponent {

  @Input() navItem: NavigationItem;

  /**
   * Constructs a new SidebarNavItemComponent.
   *
   * @param elementRef The element reference.
   * @param router     The router.
   */
  constructor(private elementRef: ElementRef, private router: Router) {
  }

  hasBadge(): boolean {
    return !!this.navItem.badge;
  }

  hasClass(): boolean {
    return !!this.navItem.cssClass;
  }

  hasIcon(): boolean {
    return !!this.navItem.icon;
  }

  hasVariant(): boolean {
    return !!this.navItem.variant;
  }

  hideMobile() {
    if (document.body.classList.contains('sidebar-show')) {
      document.body.classList.toggle('sidebar-show');
    }
  }

  isActive(): boolean {
    if (this.navItem.url) {
      return this.router.isActive(this.thisUrl(), false);
    } else {
      return false;
    }
  }

  isDivider(): boolean {
    return this.navItem.divider;
  }

  isDropdown(): boolean {
    return !!this.navItem.children;
  }

  isExternalLink(): boolean {
    if (this.navItem) {
      if (this.navItem.url) {
        return this.navItem.url.substring(0, 4) === 'http';
      }
    }

    return false;
  }

  isTitle(): boolean {
    return this.navItem.title;
  }

  thisUrl(): string {
    return this.navItem.url;
  }
}

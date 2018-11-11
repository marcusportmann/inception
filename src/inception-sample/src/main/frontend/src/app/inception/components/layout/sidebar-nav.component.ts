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

  @HostBinding('attr.role') role = 'nav';

  constructor(private navigationService: NavigationService, private sessionService: SessionService) {
    this.navItems = new Array<NavigationItem>();
  }

  ngOnDestroy() {
    if (this._sessionSubscription) {
      this._sessionSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this._sessionSubscription = this.sessionService.session.pipe(
      map((session: Session) => {
        this.navItems = this._filterNavigationItems(this.navigationService.getNavigation(), session);
      })
    ).subscribe();
  }

  private _filterNavigationItems(navigationItems: NavigationItem[], session: Session): NavigationItem[] {

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

                var filteredChildNavigationItems: NavigationItem[] =  this._filterNavigationItems(navigationItem.children, session);

                filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
                  navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
                  navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
              }
            }
          }
        }
      }
      else {

        var filteredChildNavigationItems: NavigationItem[] =  this._filterNavigationItems(navigationItem.children, session);

        filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
          navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
          navigationItem.variant, navigationItem.badge, navigationItem.divider, navigationItem.title));
      }
    }

    return filteredNavigationItems;
  }
}

@Component({
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
          <i *ngIf="hasIcon()" class="nav-icon material-icons">{{ navItem.icon }}</i>
          {{ navItem.name }}
          <span *ngIf="hasBadge()" [ngClass]="'badge badge-' + navItem.badge.variant">{{ navItem.badge.text }}</span>
        </a>
        <ng-template #externalLink>
          <a [ngClass]="hasVariant() ? 'nav-link nav-link-' + navItem.variant : 'nav-link'" href="{{navItem.url}}">
            <i *ngIf="hasIcon()" class="nav-icon material-icons">{{ navItem.icon }}</i>
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

  constructor(private router: Router, private el: ElementRef) {
  }

  hasBadge(): boolean {
    return this.navItem.badge ? true : false;
  }

  hasClass(): boolean {
    return this.navItem.cssClass ? true : false;
  }

  hasIcon(): boolean {
    return this.navItem.icon ? true : false;
  }

  hasVariant(): boolean {
    return this.navItem.variant ? true : false;
  }

  hideMobile() {
    if (document.body.classList.contains('sidebar-show')) {
      document.body.classList.toggle('sidebar-show');
    }
  }

  isActive(): boolean {
    if (this.navItem.url) {
      return this.router.isActive(this.thisUrl(), false);
    }
    else {
      return false;
    }
  }

  isDivider(): boolean {
    return this.navItem.divider ? true : false;
  }

  isDropdown(): boolean {
    return this.navItem.children ? true : false;
  }

  isExternalLink(): boolean {
    if (this.navItem) {
      if (this.navItem.url) {
        return this.navItem.url.substring(0, 4) === 'http' ? true : false;
      }
    }

    return false;
  }

  isTitle(): boolean {
    return this.navItem.title ? true : false;
  }

  thisUrl(): string {
    return this.navItem.url;
  }
}

@Component({
  selector: 'sidebar-nav-dropdown',
  template: `
    <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
      <i *ngIf="hasIcon()" class="nav-icon material-icons">{{ navItem.icon }}</i>
      {{ navItem.name }}
      <span *ngIf="hasBadge()" [ngClass]="'badge badge-' + navItem.badge.variant">{{ navItem.badge.text }}</span>
    </a>
    <ul class="nav-dropdown-items">
      <sidebar-nav-item *ngFor="let child of navItem.children" [navItem]='child'></sidebar-nav-item>
    </ul>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }']
})
export class SidebarNavDropdownComponent {
  @Input() navItem: NavigationItem;

  public hasBadge() {
    return this.navItem.badge ? true : false;
  }

  public hasIcon() {
    return this.navItem.icon ? true : false;
  }

  constructor(private router: Router) {
  }
}


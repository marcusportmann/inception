/*
 * Copyright 2020 Marcus Portmann
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

import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Replace} from '../../core/util/replace';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {Router} from '@angular/router';
import {Session} from '../../security/services/session';
import {SecurityService} from '../../security/services/security.service';

/**
 * The AdminHeaderComponent class implements the admin header component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'admin-header',
  template: `
    <header class="admin-header">
      <button class="toggler d-lg-none" type="button" sidebarToggler>
        <span class="toggler-icon"></span>
      </button>
      <a class="brand" href="#">
        <div class="brand-full"></div>
        <div class="brand-minimized"></div>
      </a>

      <button class="toggler d-md-down-none" type="button" [sidebarToggler]="sidebarToggler">
        <span class="toggler-icon"></span>
      </button>

      <ul class="nav ml-auto">
        <!--
        <li class="nav-item d-md-down-none">
          <a href="#" class="nav-link"><i class="icon-bell"></i><span class="badge badge-danger">5</span></a>
        </li>
        -->
        <li *ngIf="isLoggedIn() | async; else login_link" class="nav-item"
            [matMenuTriggerFor]="userMenu">
          <a href="#" class="nav-link" (click)="false">
            <span class="user-icon"></span>
            <span class="user-name d-md-down-none">{{ userName() | async }}</span>
          </a>
        </li>

        <mat-menu #userMenu="matMenu" yPosition="below" overlapTrigger="false" class="user-menu">
          <a mat-menu-item href="#">
            <i class="fas fa-user-circle"></i>
            <span i18n="@@admin_header_component_menu_item_profile">Profile</span>
          </a>
          <a mat-menu-item href="#">
            <i class="fas fa-cogs"></i>
            <span i18n="@@admin_header_component_menu_item_settings">Settings</span>
          </a>
          <a mat-menu-item href="#" (click)="logout()">
            <i class="fas fa-sign-out-alt"></i>
            <span i18n="@@admin_header_component_menu_item_logout">Logout</span>
          </a>
        </mat-menu>

        <ng-template #login_link>
          <li class="nav-item">
            <a class="nav-link" (click)="login()">
              <span class="login-icon"></span>
              <span class="login d-md-down-none"
                    i18n="@@admin_header_component_link_login">Login</span>
            </a>
          </li>
        </ng-template>
      </ul>
    </header>
  `
})
export class AdminHeaderComponent implements OnInit {

  @Input() fixed = false;

  // tslint:disable-next-line
  @Input() sidebarToggler!: any;

  /**
   * Constructs a new AdminHeaderComponent.
   *
   * @param elementRef      The element reference.
   * @param router          The router.
   * @param securityService The security service.
   */
  constructor(private elementRef: ElementRef, private router: Router, private securityService: SecurityService) {
  }

  // // tslint:disable-next-line
  // static breakpoint(breakpoint: any): any {
  //   console.log(breakpoint);
  //   return breakpoint ? breakpoint : '';
  // }

  ngOnInit(): void {
    Replace(this.elementRef);

    if (this.fixed) {
      const bodySelector = document.querySelector('body');

      if (bodySelector) {
        bodySelector.classList.add('admin-header-fixed');
      }
    }
  }

  isLoggedIn(): Observable<boolean> {
    return this.securityService.session$.pipe(map((session: Session | null) => {

      //console.log('isLoggedIn session = ', session);

      return (!!session);
    }));
  }

  login(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.securityService.logout();
  }

  userName(): Observable<string> {
    return this.securityService.session$.pipe(map((session: Session | null) => {
      if (!!session) {
        return session.name;
      } else {
        return '';
      }
    }));
  }
}

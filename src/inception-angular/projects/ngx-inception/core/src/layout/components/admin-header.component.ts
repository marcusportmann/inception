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

import {isPlatformBrowser} from '@angular/common';
import {
  booleanAttribute, ChangeDetectionStrategy, Component, computed, ElementRef, inject, Input,
  OnDestroy, OnInit, PLATFORM_ID, Renderer2
} from '@angular/core';
import {toSignal} from '@angular/core/rxjs-interop';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {Router, RouterLink} from '@angular/router';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';
import {SessionService} from '../../session/services/session.service';
import {Replace} from '../../util/replace';
import {SidebarTogglerDirective} from '../directives/sidebar-toggler.directive';

/**
 * The AdminHeaderComponent class implements the admin header component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'admin-header',
  standalone: true,
  imports: [SidebarTogglerDirective, MatMenuTrigger, MatMenu, MatMenuItem, RouterLink],
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.admin-header-fixed]': 'fixed'
  },
  template: `
    <header class="admin-header">
      <a
        class="toggler d-lg-none"
        href="#"
        role="button"
        sidebarToggler
        aria-label="Toggle sidebar"
        (click)="_preventDefault($event)"
      >
        <span class="toggler-icon"></span>
      </a>

      <a class="brand" [routerLink]="['/']" aria-label="Home">
        <div class="brand-full"></div>
        <div class="brand-minimized"></div>
      </a>

      <a
        class="toggler d-md-down-none"
        href="#"
        role="button"
        [sidebarToggler]="sidebarToggler"
        aria-label="Toggle sidebar"
        (click)="_preventDefault($event)"
      >
        <span class="toggler-icon"></span>
      </a>

      <ul class="nav ml-auto">
        @if (isLoggedIn()) {
          <li class="nav-item" [matMenuTriggerFor]="userMenu">
            <a
              href="#"
              class="nav-link"
              role="button"
              aria-haspopup="menu"
              (click)="_preventDefault($event)"
            >
              <span class="user-icon"></span>
              <span class="user-name d-md-down-none">{{ userName() }}</span>
            </a>
          </li>
        } @else {
          <li class="nav-item">
            <a class="nav-link" href="#" role="button" (click)="_onLoginClick($event)">
              <span class="login-icon"></span>
              <span
                class="login d-md-down-none"
                i18n="@@admin_header_component_link_login"
              >Login</span>
            </a>
          </li>
        }

        <mat-menu #userMenu="matMenu" yPosition="below" [overlapTrigger]="false" class="user-menu">
          @if (isUserProfileEnabled) {
            <a mat-menu-item href="#" role="menuitem" (click)="_onProfileClick($event)">
              <i class="fas fa-user-circle" aria-hidden="true"></i>
              <span i18n="@@admin_header_component_menu_item_profile">Profile</span>
            </a>
          }
          <a mat-menu-item href="#" role="menuitem" (click)="_onLogoutClick($event)">
            <i class="fas fa-sign-out-alt" aria-hidden="true"></i>
            <span i18n="@@admin_header_component_menu_item_logout">Logout</span>
          </a>
        </mat-menu>
      </ul>
    </header>
  `
})
export class AdminHeaderComponent implements OnInit, OnDestroy {
  readonly config = inject<InceptionConfig>(INCEPTION_CONFIG);

  @Input({transform: booleanAttribute}) fixed = false;

  /** Expose config flag as a stable boolean for the template. */
  readonly isUserProfileEnabled: boolean = this.config.userProfileEnabled;

  @Input() sidebarToggler?: string;

  /** Optional: used for aria-expanded if you want it. */
    // Keep if you later wire it via @ViewChild(MatMenuTrigger) userMenuTrigger!: MatMenuTrigger;
  readonly userMenuTrigger?: MatMenuTrigger;

  readonly userName = computed(() => this.session()?.name ?? '');

  private _bodyClassApplied = false;

  private readonly elementRef = inject(ElementRef<HTMLElement>);

  private readonly platformId = inject(PLATFORM_ID);

  private readonly renderer = inject(Renderer2);

  private readonly router = inject(Router);

  private readonly sessionService = inject(SessionService);

  /** Session as a signal. */
  readonly session = toSignal(this.sessionService.session$, {initialValue: null});

  /** Derived signals. */
  readonly isLoggedIn = computed(() => !!this.session());

  @Input()
  set isFixed(value: boolean) {
    // Optional: keep backward compatibility if any consumers use a different input name.
    this.fixed = value;
  }

  _onLoginClick(event: MouseEvent): void {
    event.preventDefault();
    void this.router.navigate(['/login']);
  }

  _onLogoutClick(event: MouseEvent): void {
    event.preventDefault();
    this.sessionService.logout();
  }

  _onProfileClick(event: MouseEvent): void {
    event.preventDefault();
    void this.router.navigate(['/profile']);
  }

  /** Template helper to keep anchors but prevent '#'/navigation side effects. */
  _preventDefault(event: MouseEvent): void {
    event.preventDefault();
  }

  ngOnDestroy(): void {
    if (isPlatformBrowser(this.platformId) && this._bodyClassApplied) {
      this.renderer.removeClass(document.body, 'admin-header-fixed');
      this._bodyClassApplied = false;
    }
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      // Legacy DOM manipulation (only run in the browser).
      Replace(this.elementRef);

      // If you *must* add a class to <body>, do it via Renderer2 and clean up in ngOnDestroy.
      if (this.fixed) {
        this.renderer.addClass(document.body, 'admin-header-fixed');
        this._bodyClassApplied = true;
      }
    }
  }
}

// <a mat-menu-item>
// <i class="fas fa-cogs"></i>
//   <span i18n="@@admin_header_component_menu_item_settings">Settings</span>
//   </a>

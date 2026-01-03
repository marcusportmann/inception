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
  ChangeDetectionStrategy, Component, DestroyRef, DOCUMENT, inject, NgZone, OnDestroy, OnInit,
  PLATFORM_ID
} from '@angular/core';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ActivatedRoute, ActivatedRouteSnapshot, Router, RouterOutlet} from '@angular/router';
import {NgScrollbarModule} from 'ngx-scrollbar';
import {distinctUntilChanged, filter, map, take} from 'rxjs/operators';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';
import {SessionService} from '../../session/services/session.service';
import {SidebarMinimizerDirective} from '../directives/sidebar-minimizer.directive';
import {BreadcrumbsService} from '../services/breadcrumbs.service';
import {SidebarService} from '../services/sidebar.service';
import {SpinnerService} from '../services/spinner.service';
import {TitleBarService} from '../services/title-bar.service';

import {AdminContainerView} from './admin-container-view';
import {AdminFooterComponent} from './admin-footer.component';
import {AdminHeaderComponent} from './admin-header.component';
import {BreadcrumbsComponent} from './breadcrumbs.component';
import {SidebarNavComponent} from './sidebar-nav.component';
import {SidebarComponent} from './sidebar.component';
import {TitleBarComponent} from './title-bar.component';

/**
 * The AdminContainerComponent class implements the admin container component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'admin-container',
  standalone: true,
  imports: [
    AdminHeaderComponent,
    RouterOutlet,
    AdminFooterComponent,
    SidebarComponent,
    SidebarNavComponent,
    NgScrollbarModule,
    TitleBarComponent,
    BreadcrumbsComponent,
    SidebarMinimizerDirective
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <admin-header fixed sidebarToggler="lg"></admin-header>

    <div class="admin-body">
      <sidebar class="sidebar" fixed display="lg">
        <ng-scrollbar appearance="compact" orientation="vertical" visibility="hover">
          <sidebar-nav></sidebar-nav>
        </ng-scrollbar>
        <button class="sidebar-minimizer" type="button" sidebarMinimizer></button>
      </sidebar>

      <main class="main">
        <title-bar fixed></title-bar>
        <breadcrumbs fixed></breadcrumbs>

        <div class="container-fluid">
          <router-outlet (activate)="routerOutletActivate($event)"
                         (deactivate)="routerOutletDeactivate($event)">
          </router-outlet>
        </div>
      </main>
    </div>

    <admin-footer [fixed]="false">
      <span>2026 &copy; <span class="copyright-name"></span></span>
    </admin-footer>
  `
})
export class AdminContainerComponent implements OnInit, OnDestroy {
  sidebarMinimized = true;

  private readonly _activatedRoute = inject(ActivatedRoute);

  private readonly _breadcrumbsService = inject(BreadcrumbsService);

  private readonly _config = inject<InceptionConfig>(INCEPTION_CONFIG);

  private readonly _destroyRef = inject(DestroyRef);

  private readonly _document = inject<Document>(DOCUMENT);

  private _mutationObserver?: MutationObserver;

  private readonly _ngZone = inject(NgZone);

  private readonly _platformId = inject(PLATFORM_ID);

  private readonly _router = inject(Router);

  private readonly _sessionService = inject(SessionService);

  private readonly _sidebarService = inject(SidebarService);

  private readonly _spinnerService = inject(SpinnerService);

  private readonly _titleBarService = inject(TitleBarService);

  ngOnDestroy(): void {
    this._mutationObserver?.disconnect();
    this._mutationObserver = undefined;
  }

  ngOnInit(): void {
    this._initSidebarMinimizedTracking();
    this._initSessionLifecycleHandling();
  }

  /**
   * Process the router outlet activate event.
   *
   * @param component The activated child component.
   */
  routerOutletActivate(component: unknown): void {
    // Stop any previous "title observable" subscription chain by resetting title/back nav first.
    // (We also cancel via takeUntilDestroyed using a per-activation observable below.)
    let usedViewTitle = false;

    if (component instanceof AdminContainerView) {
      // Sidebar minimized preference
      const sidebarMinimized = component.sidebarMinimized;
      if (sidebarMinimized != null) {
        this._sidebarService.setSidebarMinimized(sidebarMinimized);
      }

      // Breadcrumb visibility
      this._breadcrumbsService.setBreadcrumbsVisible(component.breadcrumbsVisible);

      // Back navigation
      this._titleBarService.setBackNavigation(component.backNavigation);

      // Title: string or Observable<string>
      const title = component.title;
      if (title) {
        usedViewTitle = true;

        if (typeof title === 'string') {
          this._titleBarService.setTitle(title);
        } else {
          // Subscribe safely; auto-cleaned when component is destroyed OR when a new route
          // activates (because routerOutletDeactivate clears title/back nav).
          title
          .pipe(takeUntilDestroyed(this._destroyRef))
          .subscribe((newTitle: string) => this._titleBarService.setTitle(newTitle));
        }
      }
    } else {
      // Default for non AdminContainerView children
      this._breadcrumbsService.setBreadcrumbsVisible(true);
      this._titleBarService.setBackNavigation(null);
    }

    if (!usedViewTitle) {
      this._updateTitleFromRoute();
    }
  }

  /**
   * Process the router outlet deactivate event.
   *
   * @param _component The deactivated child component.
   */
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  routerOutletDeactivate(_component: unknown): void {
    // Clear the back navigation and title
    this._titleBarService.setBackNavigation(null);
    this._titleBarService.setTitle(null);
  }

  private _initSessionLifecycleHandling(): void {
    // Navigate away exactly once when the session becomes null.
    this._sessionService.session$
    .pipe(
      map((session) => !!session),
      distinctUntilChanged(),
      filter((loggedIn) => !loggedIn),
      take(1),
      takeUntilDestroyed(this._destroyRef)
    )
    .subscribe(() => {
      this._spinnerService.hideSpinner();
      void this._router.navigate([this._config.logoutRedirectUri ?? '/']);
    });
  }

  private _initSidebarMinimizedTracking(): void {
    if (!isPlatformBrowser(this._platformId)) {
      return;
    }

    // Initial sidebar state
    this.sidebarMinimized = this._document.body.classList.contains('sidebar-minimized');

    // Observe body class changes for sidebar minimization (legacy integration).
    // Run outside Angular and only re-enter when the value changes.
    this._ngZone.runOutsideAngular(() => {
      let lastValue = this.sidebarMinimized;

      this._mutationObserver = new MutationObserver(() => {
        const nextValue = this._document.body.classList.contains('sidebar-minimized');
        if (nextValue === lastValue) return;

        lastValue = nextValue;
        this._ngZone.run(() => {
          this.sidebarMinimized = nextValue;
        });
      });

      this._mutationObserver.observe(this._document.body, {
        attributes: true,
        attributeFilter: ['class']
      });
    });
  }

  /**
   * Update the title from the deepest activated route snapshot, if provided.
   */
  private _updateTitleFromRoute(): void {
    let route: ActivatedRouteSnapshot | null = this._activatedRoute.snapshot.firstChild ?? null;

    while (route?.firstChild) {
      route = route.firstChild;
    }

    const titleFromRoute = route?.data?.['title'] as string | undefined;
    if (titleFromRoute) {
      this._titleBarService.setTitle(titleFromRoute);
    }
  }
}

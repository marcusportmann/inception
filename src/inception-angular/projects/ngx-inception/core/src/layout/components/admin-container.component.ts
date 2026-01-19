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
  ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, DOCUMENT, inject, NgZone,
  OnDestroy, OnInit, PLATFORM_ID
} from '@angular/core';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ActivatedRoute, ActivatedRouteSnapshot, Router, RouterOutlet} from '@angular/router';
import {NgScrollbarModule} from 'ngx-scrollbar';
import {Subject} from 'rxjs';
import {distinctUntilChanged, filter, map, take, takeUntil} from 'rxjs/operators';
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
  changeDetection: ChangeDetectionStrategy.OnPush,
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
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'admin-container',
  standalone: true,
  template: `
    <admin-header fixed sidebarToggler="lg"></admin-header>

    <div class="admin-body">
      <sidebar class="sidebar" fixed display="lg">
        <ng-scrollbar appearance="compact" orientation="vertical" visibility="hover">
          <sidebar-nav></sidebar-nav>
        </ng-scrollbar>
        <button type="button" class="sidebar-minimizer" sidebarMinimizer>&nbsp;</button>
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
      <span>{{ currentYear }} &copy; <span class="copyright-name"></span></span>
    </admin-footer>
  `
})
export class AdminContainerComponent implements OnInit, OnDestroy {
  readonly currentYear = new Date().getFullYear();

  sidebarMinimized = true;

  private readonly _activatedRoute = inject(ActivatedRoute);

  private readonly _breadcrumbsService = inject(BreadcrumbsService);

  private readonly _changeDetectorRef = inject(ChangeDetectorRef);

  private readonly _config = inject<InceptionConfig>(INCEPTION_CONFIG);

  private readonly destroyRef = inject(DestroyRef);

  private readonly document = inject<Document>(DOCUMENT);

  private mutationObserver?: MutationObserver;

  private readonly ngZone = inject(NgZone);

  private readonly platformId = inject(PLATFORM_ID);

  /**
   * Cancels any per-activation streams (e.g. title observables) when the routed component changes.
   */
  private readonly routeActivationStop$ = new Subject<void>();

  private readonly router = inject(Router);

  private readonly sessionService = inject(SessionService);

  private readonly sidebarService = inject(SidebarService);

  private readonly spinnerService = inject(SpinnerService);

  private readonly titleBarService = inject(TitleBarService);

  ngOnDestroy(): void {
    this.routeActivationStop$.next();
    this.routeActivationStop$.complete();

    this.mutationObserver?.disconnect();
    this.mutationObserver = undefined;
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
    // Stop any previous per-activation subscription chain immediately.
    this.routeActivationStop$.next();

    let usedViewTitle = false;

    if (component instanceof AdminContainerView) {
      // Sidebar minimized preference
      const sidebarMinimized = component.sidebarMinimized;
      if (sidebarMinimized != null) {
        this.sidebarService.setSidebarMinimized(sidebarMinimized);
      }

      // Breadcrumb visibility
      this._breadcrumbsService.setBreadcrumbsVisible(component.breadcrumbsVisible);

      // Back navigation
      this.titleBarService.setBackNavigation(component.backNavigation);

      // Title: string or Observable<string>
      const title = component.title;
      if (title) {
        usedViewTitle = true;

        if (typeof title === 'string') {
          this.titleBarService.setTitle(title);
        } else {
          title
          .pipe(
            // Stop when this routed component deactivates OR container destroys.
            takeUntil(this.routeActivationStop$),
            takeUntilDestroyed(this.destroyRef)
          )
          .subscribe((newTitle: string) => this.titleBarService.setTitle(newTitle));
        }
      }
    } else {
      // Defaults for non AdminContainerView children
      this._breadcrumbsService.setBreadcrumbsVisible(true);
      this.titleBarService.setBackNavigation(null);
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
    // Cancel any per-activation streams for the deactivated route.
    this.routeActivationStop$.next();

    // Clear the back navigation and title
    this.titleBarService.setBackNavigation(null);
    this.titleBarService.setTitle(null);
  }

  private _initSessionLifecycleHandling(): void {
    // Navigate away exactly once when the session becomes null.
    this.sessionService.session$
    .pipe(
      map((session) => !!session),
      distinctUntilChanged(),
      filter((loggedIn) => !loggedIn),
      take(1)
    )
    .subscribe(() => {
      this.spinnerService.hideSpinner();
      void this.router.navigate([this._config.logoutRedirectUri ?? '/']);
    });
  }

  private _initSidebarMinimizedTracking(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    // Initial sidebar state
    this.sidebarMinimized = this.document.body.classList.contains('sidebar-minimized');

    // Observe body class changes for sidebar minimization (legacy integration).
    // Run outside Angular and only re-enter when the value changes.
    this.ngZone.runOutsideAngular(() => {
      let lastValue = this.sidebarMinimized;

      this.mutationObserver = new MutationObserver(() => {
        const nextValue = this.document.body.classList.contains('sidebar-minimized');
        if (nextValue === lastValue) return;

        lastValue = nextValue;
        this.ngZone.run(() => {
          this.sidebarMinimized = nextValue;

          // OnPush safety: ensure template updates reliably.
          this._changeDetectorRef.markForCheck();
        });
      });

      this.mutationObserver.observe(this.document.body, {
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
      this.titleBarService.setTitle(titleFromRoute);
    }
  }
}

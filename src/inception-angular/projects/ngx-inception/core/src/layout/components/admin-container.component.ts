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



import { Component, Inject, OnDestroy, OnInit, DOCUMENT } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, RouterOutlet } from '@angular/router';
import { PerfectScrollbarModule } from 'ngx-om-perfect-scrollbar';

import { Observable, Subscription } from 'rxjs';
import { INCEPTION_CONFIG, InceptionConfig } from '../../inception-config';
import { Session } from '../../session/services/session';
import { SessionService } from '../../session/services/session.service';
import { BreadcrumbsService } from '../services/breadcrumbs.service';
import { SidebarService } from '../services/sidebar.service';
import { SpinnerService } from '../services/spinner.service';
import { TitleBarService } from '../services/title-bar.service';

import { AdminContainerView } from './admin-container-view';
import { AdminFooterComponent } from './admin-footer.component';
import { AdminHeaderComponent } from './admin-header.component';
import { BreadcrumbsComponent } from './breadcrumbs.component';
import { SidebarMinimizerComponent } from './sidebar-minimizer.component';
import { SidebarNavComponent } from './sidebar-nav.component';
import { SidebarComponent } from './sidebar.component';
import { TitleBarComponent } from './title-bar.component';

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
    PerfectScrollbarModule,
    SidebarMinimizerComponent,
    TitleBarComponent,
    BreadcrumbsComponent
  ],
  template: `
    <admin-header
      [fixed]="true"
      [sidebarToggler]="'lg'">
    </admin-header>

    <div class="admin-body">
      <sidebar class="sidebar" [fixed]="true" [display]="'lg'">
        <sidebar-nav
          [perfectScrollbar]
          [disabled]="sidebarMinimized">
        </sidebar-nav>
        <sidebar-minimizer></sidebar-minimizer>
      </sidebar>

      <main class="main">
        <title-bar [fixed]="true"></title-bar>
        <breadcrumbs [fixed]="true"></breadcrumbs>

        <div class="container-fluid">
          <router-outlet
            (activate)="routerOutletActive($event)"
            (deactivate)="routerOutletDeactive($event)">
          </router-outlet>
        </div>
      </main>
    </div>

    <admin-footer [fixed]="false">
      <span>2025 &copy; <span class="copyright-name"></span></span>
    </admin-footer>
  `
})
export class AdminContainerComponent implements OnInit, OnDestroy {
  sidebarMinimized = true;

  private adminContainerViewTitleSubscription?: Subscription;

  private readonly mutationObserver: MutationObserver;

  private readonly subscriptions = new Subscription();

  /**
   * Constructs a new AdminContainerComponent.
   */
  constructor(
    @Inject(INCEPTION_CONFIG) private readonly config: InceptionConfig,
    @Inject(DOCUMENT) private readonly document: Document,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly breadcrumbsService: BreadcrumbsService,
    private readonly sessionService: SessionService,
    private readonly sidebarService: SidebarService,
    private readonly spinnerService: SpinnerService,
    private readonly titleBarService: TitleBarService
  ) {
    this.mutationObserver = new MutationObserver(() => {
      this.sidebarMinimized = this.document.body.classList.contains('sidebar-minimized');
    });
  }

  ngOnDestroy(): void {
    this.mutationObserver.disconnect();
    this.subscriptions.unsubscribe();

    if (this.adminContainerViewTitleSubscription) {
      this.adminContainerViewTitleSubscription.unsubscribe();
      this.adminContainerViewTitleSubscription = undefined;
    }
  }

  ngOnInit(): void {
    // Initial sidebar state
    this.sidebarMinimized = this.document.body.classList.contains('sidebar-minimized');

    // Observe body class changes for sidebar minimization
    this.mutationObserver.observe(this.document.body, {
      attributes: true,
      attributeFilter: ['class']
    });

    // Handle session lifecycle
    this.subscriptions.add(
      this.sessionService.session$.subscribe((session: Session | null) => {
        if (!session) {
          this.spinnerService.hideSpinner();

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate([
            this.config.logoutRedirectUri ?? '/'
          ]);
        }
      })
    );
  }

  /**
   * Process the router outlet activate event.
   *
   * @param component The activated child component.
   */
  routerOutletActive(component: unknown): void {
    let usingAdminContainerViewTitle = false;

    // Try and retrieve the back navigation and title from the admin container view if present
    if (component instanceof AdminContainerView) {
      if (component.sidebarMinimized != null) {
        this.sidebarService.setSidebarMinimized(component.sidebarMinimized);
      }

      this.breadcrumbsService.setBreadcrumbsVisible(component.breadcrumbsVisible);

      if (component.backNavigation) {
        this.titleBarService.setBackNavigation(component.backNavigation);
      }

      const title: string | Observable<string> | null = component.title;

      if (title) {
        usingAdminContainerViewTitle = true;

        if (typeof title === 'string') {
          this.titleBarService.setTitle(title);
        } else {
          this.adminContainerViewTitleSubscription = title.subscribe((newTitle: string) => {
            this.titleBarService.setTitle(newTitle);
          });
        }
      }
    } else {
      this.breadcrumbsService.setBreadcrumbsVisible(true);
    }

    // Fallback: title from the deepest activated route
    if (!usingAdminContainerViewTitle) {
      this.updateTitleFromRoute();
    }
  }

  /**
   * Process the router outlet deactivate event.
   *
   * @param _component The deactivated child component.
   */
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  routerOutletDeactive(_component: unknown): void {
    // Unsubscribe from the title for the admin container view if required
    if (this.adminContainerViewTitleSubscription) {
      this.adminContainerViewTitleSubscription.unsubscribe();
      this.adminContainerViewTitleSubscription = undefined;
    }

    // Clear the back navigation and title
    this.titleBarService.setBackNavigation(null);
    this.titleBarService.setTitle(null);
  }

  /**
   * Update the title from the deepest activated route snapshot, if provided.
   */
  private updateTitleFromRoute(): void {
    let route: ActivatedRouteSnapshot | null = this.activatedRoute.snapshot.firstChild ?? null;

    while (route?.firstChild) {
      route = route.firstChild;
    }

    const titleFromRoute = route?.data?.['title'] as string | undefined;

    if (titleFromRoute) {
      this.titleBarService.setTitle(titleFromRoute);
    }
  }
}



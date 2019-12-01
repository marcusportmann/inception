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

import {Component} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot} from '@angular/router';

import {Observable, Subscription} from 'rxjs';

import {AdminContainerView} from './admin-container-view';
import {TitleBarService} from '../services/title-bar.service';

/**
 * The AdminContainerComponent class implements the admin container component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line:component-selector
  selector: 'admin-container',
  template: `
    <admin-header [fixed]="true" [sidebarToggler]="'lg'"></admin-header>
    <div class="admin-body">
      <sidebar [fixed]="true" [display]="'lg'">
        <sidebar-nav [perfectScrollbar] [disabled]="sidebarMinimized"></sidebar-nav>
        <sidebar-minimizer></sidebar-minimizer>
      </sidebar>
      <main class="main">
        <title-bar [fixed]="true"></title-bar>
        <breadcrumbs [fixed]="true"></breadcrumbs>
        <div class="container-fluid">
          <router-outlet (activate)="routerOutletActive($event)" (deactivate)="routerOutletDeactive($event)">
          </router-outlet>
        </div>
      </main>
    </div>
    <admin-footer [fixed]="false">
      <span>2019 &copy; <span class="copyright-name"></span></span>
    </admin-footer>
  `
})
export class AdminContainerComponent {

  element: HTMLElement = document.body;

  sidebarMinimized = true;

  private adminContainerViewTitleSubscription?: Subscription;

  private changes: MutationObserver;

  /**
   * Constructs a new AdminContainerComponent.
   *
   * @param activatedRoute  The activated route.
   * @param titleBarService The title bar service.
   */
  constructor(private activatedRoute: ActivatedRoute, private titleBarService: TitleBarService) {
    this.changes = new MutationObserver(() => {
      this.sidebarMinimized = document.body.classList.contains('sidebar-minimized');
    });

    this.changes.observe(this.element, {
      attributes: true
    });
  }

  /**
   * Process the router outlet activate event.
   *
   * @param childComponent The child component.
   */
  // tslint:disable-next-line
  routerOutletActive(childComponent: any) {

    let usingAdminContainerViewTitle = false;

    // Try and retrieve the back navigation and title from the admin container view if present
    if (childComponent instanceof AdminContainerView) {

      if (childComponent.backNavigation) {
        this.titleBarService.setBackNavigation(childComponent.backNavigation);
      }

      const title: string | Observable<string> | null = childComponent.title;

      if (title) {
        usingAdminContainerViewTitle = true;

        if (typeof (title) === 'string') {
          this.titleBarService.setTitle(title);
        } else {
          this.adminContainerViewTitleSubscription = title.subscribe((newTitle: string) => {
            this.titleBarService.setTitle(newTitle);
          });
        }
      }
    }

    /*
     * If we are not using a title provided by the admin container view, attempt to retrieve the
     * title from the activated route.
     */
    if (!usingAdminContainerViewTitle) {
      let activateRouteSnapshot: ActivatedRouteSnapshot | null = this.activatedRoute.snapshot.firstChild;

      if (activateRouteSnapshot) {

        while (activateRouteSnapshot.firstChild) {
          activateRouteSnapshot = activateRouteSnapshot.firstChild;
        }

        if (activateRouteSnapshot.data) {
          if (!!activateRouteSnapshot.data.title) {
            this.titleBarService.setTitle(activateRouteSnapshot.data.title);
          }
        }
      }
    }
  }

  /**
   * Process the router outlet deactivate event.
   *
   * @param childComponent The child component.
   */
  // tslint:disable-next-line
  routerOutletDeactive(childComponent: any) {
    // Unsubscribe from the title for the admin container view if required
    if (this.adminContainerViewTitleSubscription) {
      this.adminContainerViewTitleSubscription.unsubscribe();
      this.adminContainerViewTitleSubscription = undefined;
    }

    // Clear the back navigation and title
    this.titleBarService.setBackNavigation(null);
    this.titleBarService.setTitle(null);
  }
}

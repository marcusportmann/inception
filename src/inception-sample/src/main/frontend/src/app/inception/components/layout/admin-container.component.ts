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
import {AdminContainerView} from "./admin-container-view";
import {BackNavigation} from "./back-navigation";
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
} from "@angular/router";
import {TitleBarService} from "../../services/layout/title-bar.service";

/**
 * The AdminContainerComponent class implements the admin container component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'admin-container',
  template: `
    <admin-header
      [fixed]="true"
      [brandFull]="{src: 'assets/images/logo.png', width: 100,  alt: 'Logo'}"
      [brandMinimized]="{src: 'assets/images/logo-symbol.png', width: 30, height: 30, alt: 'Logo'}"
      [sidebarToggler]="'lg'">
    </admin-header>
    <div class="admin-body">
      <sidebar [fixed]="true" [display]="'lg'">
        <sidebar-nav [perfectScrollbar] [disabled]="sidebarMinimized"></sidebar-nav>
        <sidebar-minimizer></sidebar-minimizer>
      </sidebar>
      <main class="main">
        <title-bar [fixed]="true"></title-bar>
        <breadcrumbs [fixed]="true"></breadcrumbs>
        <div class="container-fluid">
          <router-outlet (activate)="onRouterOutletActive($event)" (deactivate)="onRouterOutletDeactive($event)"></router-outlet>
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

    this.changes.observe(<Element>this.element, {
      attributes: true
    });
  }

  /**
   * Process the router outlet activate event.
   *
   * @param childComponent The child component.
   */
  onRouterOutletActive(childComponent: any) {

    let backNavigation: BackNavigation = null;
    let title: string = null;

    // Try and retrieve the title from the data for activated route
    let activateRouteSnapshot: ActivatedRouteSnapshot = this.activatedRoute.snapshot.firstChild;

    while (activateRouteSnapshot.firstChild) activateRouteSnapshot = activateRouteSnapshot.firstChild;

    if (activateRouteSnapshot.data) {
      if (!!activateRouteSnapshot.data.title) {
        title = activateRouteSnapshot.data.title;
      }
    }

    // Try and retrieve the back navigation and title from the admin container view if present
    if (childComponent instanceof AdminContainerView) {

      if (childComponent.hasBackNavigation) {
        backNavigation = childComponent.backNavigation;
      }

      if (childComponent.hasTitle) {
        title = childComponent.title;
      }
    }

    // Set the back navigation
    this.titleBarService.setBackNavigation(backNavigation);

    // Set the title
    this.titleBarService.setTitle(title);
  }

  /**
   * Process the router outlet deactivate event.
   *
   * @param childComponent The child component.
   */
  onRouterOutletDeactive(childComponent: any) {
  }
}

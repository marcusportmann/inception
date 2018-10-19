import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";
import {NavigationService} from "../../services/navigation/navigation.service";
import {PerfectScrollbarConfigInterface} from "ngx-perfect-scrollbar";

import {Observable} from "rxjs";

import {map} from "rxjs/operators";


const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  suppressScrollY: true
};


@Component({
  selector: 'admin-container',
  template: `
    <div class="spinner"></div>
    <admin-header
      [fixed]="true"
      [brandFull]="{src: 'assets/images/logo.png', width: 100,  alt: 'Logo'}"
      [brandMinimized]="{src: 'assets/images/logo-symbol.png', width: 30, height: 30, alt: 'Logo'}"
      [sidebarToggler]="'lg'">
    </admin-header>
    <div class="admin-body">
      <sidebar [fixed]="true" [display]="'lg'">
        <sidebar-nav [navItems]="navItems" [perfectScrollbar] [disabled]="sidebarMinimized"></sidebar-nav>
        <sidebar-minimizer></sidebar-minimizer>
      </sidebar>
      <!-- Main content -->
      <main class="main">
        <breadcrumbs></breadcrumbs>
        <div class="container-fluid">
          <router-outlet></router-outlet>
        </div><!-- /.container-fluid -->
      </main>
    </div>
    <admin-footer>
      <span>2018 &copy; <span class="copyright-name"></span></span>
    </admin-footer>
  `
})
export class AdminContainerComponent {

  navItems: NavigationItem[] = [];

  sidebarMinimized = true;

  private changes: MutationObserver;

  public element: HTMLElement = document.body;

  constructor(private router: Router, private navigationService: NavigationService, private sessionService: SessionService) {

    this.changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = document.body.classList.contains('sidebar-minimized')
    });

    this.changes.observe(<Element>this.element, {
      attributes: true
    });
  }

  filterNavigationItems(navigationItems: NavigationItem[], session: Session): NavigationItem[] {

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

                var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

                filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
                  navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
                  navigationItem.badge, navigationItem.divider, navigationItem.title));
              }
            }
          }
        }
      }
      else {

        var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

        filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
          navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems, navigationItem.cssClass,
          navigationItem.badge, navigationItem.divider, navigationItem.title));
      }
    }

    return filteredNavigationItems;
  }

  ngOnInit() {

    // Retrieve and filter the navigation items
    this.sessionService.getSession().pipe(map((session: Session) => {
      this.navItems = this.filterNavigationItems(this.navigationService.getNavigation(), session);
    })).subscribe();
  }
}

import { Component, OnInit } from '@angular/core';
import {ContainerComponent} from "./container/index";
import {Router} from "@angular/router";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";
import {NavigationService} from "../../services/navigation/navigation.service";

@Component({
  selector: 'app-container',
  template: `
    <app-header
      [fixed]="true"
      [navbarBrandFull]="{src: 'assets/images/logo.png', width: 100,  alt: 'Logo'}"
      [navbarBrandMinimized]="{src: 'assets/images/logo-symbol.png', width: 30, height: 30, alt: 'Logo'}"
      [sidebarToggler]="'lg'">




    </app-header>
    <div class="app-body">
      <app-sidebar [fixed]="true" [display]="'lg'">
        <app-sidebar-nav [navItems]="navItems" [perfectScrollbar] [disabled]="sidebarMinimized"></app-sidebar-nav>
        <app-sidebar-minimizer></app-sidebar-minimizer>
      </app-sidebar>
      <!-- Main content -->
      <main class="app-main">
        <!-- Breadcrumb -->
        <ol class="breadcrumb">
          <inception-layout-breadcrumbs></inception-layout-breadcrumbs>
        </ol>
        <div class="container-fluid">
          <router-outlet></router-outlet>
        </div><!-- /.container-fluid -->
      </main>
    </div>
    <app-footer>
      <span>2018 &copy; <span class="copyright-name"></span></span>
    </app-footer>
  `
})
export class AppContainerComponent extends ContainerComponent {

  navItems: NavigationItem[] = [];

  sidebarMinimized = true;

  private changes: MutationObserver;

  public element: HTMLElement = document.body;

  constructor(private router: Router, private navigationService: NavigationService, private sessionService: SessionService) {
    super();

    this.changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = document.body.classList.contains('app-sidebar-minimized')
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
                  navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems,
                  navigationItem.badge));
              }
            }
          }
        }
      }
      else {

        var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);

        filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
          navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems,
          navigationItem.badge));
      }
    }

    return filteredNavigationItems;
  }

  ngOnInit() {
    super.ngOnInit();

    // Retrieve and filter the navigation items
    this.sessionService.getSession().map((session: Session) => {
      this.navItems = this.filterNavigationItems(this.navigationService.getNavigation(), session);
    }).subscribe();
  }
}

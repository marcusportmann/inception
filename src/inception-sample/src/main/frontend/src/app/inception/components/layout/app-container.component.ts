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
      [sidebarToggler]="'lg'"
      [appAsideToggler]="'lg'">
    
      <!--
      <ul class="nav navbar-nav d-md-down-none">
        <li class="nav-item px-3">
          <a class="nav-link" href="#">Dashboard</a>
        </li>
        <li class="nav-item px-3">
          <a class="nav-link" href="#">Users</a>
        </li>
        <li class="nav-item px-3">
          <a class="nav-link" href="#">Settings</a>
        </li>
      </ul>
      -->
    
      <ul class="nav navbar-nav ml-auto">
        <!--
        <li class="nav-item d-md-down-none">
          <a class="nav-link" href="#"><i class="icon-bell"></i><span class="badge badge-pill badge-danger">5</span></a>
        </li>
        <li class="nav-item d-md-down-none">
          <a class="nav-link" href="#"><i class="icon-list"></i></a>
        </li>
        <li class="nav-item d-md-down-none">
          <a class="nav-link" href="#"><i class="icon-location-pin"></i></a>
        </li>
        <li class="nav-item dropdown" dropdown placement="bottom right">
          <a class="nav-link" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false" dropdownToggle (click)="false">
            <img src="assets/img/avatars/6.jpg" class="img-avatar" alt="admin@bootstrapmaster.com"/>
          </a>
          <div class="dropdown-menu dropdown-menu-right" *dropdownMenu aria-labelledby="simple-dropdown">
            <div class="dropdown-header text-center"><strong>Account</strong></div>
            <a class="dropdown-item" href="#"><i class="fa fa-bell-o"></i> Updates<span class="badge badge-info">42</span></a>
            <a class="dropdown-item" href="#"><i class="fa fa-envelope-o"></i> Messages<span class="badge badge-success">42</span></a>
            <a class="dropdown-item" href="#"><i class="fa fa-tasks"></i> Tasks<span class="badge badge-danger">42</span></a>
            <a class="dropdown-item" href="#"><i class="fa fa-comments"></i> Comment<span class="badge badge-warning">42</span></a>
            <div class="dropdown-header text-center"><strong>Settings</strong></div>
            <a class="dropdown-item" href="#"><i class="fa fa-user"></i> Profile</a>
            <a class="dropdown-item" href="#"><i class="fa fa-wrench"></i> Setting</a>
            <a class="dropdown-item" href="#"><i class="fa fa-usd"></i> Payments<span class="badge badge-dark">42</span></a>
            <a class="dropdown-item" href="#"><i class="fa fa-file"></i> Projects<span class="badge badge-primary">42</span></a>
            <div class="divider"></div>
            <a class="dropdown-item" href="#"><i class="fa fa-shield"></i> Lock account</a>
            <a class="dropdown-item" href="#"><i class="fa fa-lock"></i> Logout</a>
          </div>
        </li>
        -->
      </ul>
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
      <app-aside [fixed]="true" [display]="false">
        

        
      </app-aside>
    </div>
    <app-footer>
      <span><a href="https://coreui.io">Sample</a> &copy; 2018 inception.digital.</span>
    </app-footer>
  `
})
export class AppContainerComponent extends ContainerComponent {

  navItems: NavigationItem[] = [];

  constructor(private router: Router, private navigationService: NavigationService, private sessionService: SessionService) {
    super();
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

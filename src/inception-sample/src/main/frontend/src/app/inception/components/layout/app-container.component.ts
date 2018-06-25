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

        <!--
        <ul class="nav nav-tabs">
          <li class="nav-item" ng-reflect-ng-class="nav-item,"><a class="nav-link" href="javascript:void(0);" id=""><span></span><i class="icon-list"></i></a></li>
          <li class="nav-item" ng-reflect-ng-class="nav-item,"><a class="nav-link" href="javascript:void(0);" id=""><span></span><i class="icon-speech"></i></a></li>
          <li class="nav-item" ng-reflect-ng-class="nav-item,"><a class="nav-link" href="javascript:void(0);" id=""><span></span><i class="icon-settings"></i></a></li>


          <li class="nav-item ml-auto" ng-reflect-ng-class="nav-item,"><a class="nav-link" href="javascript:void(0);" id=""><span></span><i class="icon-settings"></i></a></li>
        </ul>        
        -->

        <!--
        <tabset>
          <tab>
            <ng-template tabHeading><i class="icon-list"></i></ng-template>
            <div class="list-group list-group-accent">
              <div class="list-group-item list-group-item-accent-secondary bg-light text-center font-weight-bold text-muted text-uppercase small">Today</div>
              <div class="list-group-item list-group-item-accent-warning list-group-item-divider">
                <div class="avatar float-right">
                  <img class="img-avatar" src="assets/img/avatars/7.jpg" alt="admin@bootstrapmaster.com">
                </div>
                <div>Meeting with
                  <strong>Lucas</strong>
                </div>
                <small class="text-muted mr-3">
                  <i class="icon-calendar"></i>  1 - 3pm</small>
                <small class="text-muted">
                  <i class="icon-location-pin"></i>  Palo Alto, CA</small>
              </div>
              <div class="list-group-item list-group-item-accent-info">
                <div class="avatar float-right">
                  <img class="img-avatar" src="assets/img/avatars/4.jpg" alt="admin@bootstrapmaster.com">
                </div>
                <div>Skype with
                  <strong>Megan</strong>
                </div>
                <small class="text-muted mr-3">
                  <i class="icon-calendar"></i>  4 - 5pm</small>
                <small class="text-muted">
                  <i class="icon-social-skype"></i>  On-line</small>
              </div>
              <div class="list-group-item list-group-item-accent-secondary bg-light text-center font-weight-bold text-muted text-uppercase small">Tomorrow</div>
              <div class="list-group-item list-group-item-accent-danger list-group-item-divider">
                <div>New UI Project -
                  <strong>deadline</strong>
                </div>
                <small class="text-muted mr-3">
                  <i class="icon-calendar"></i>  10 - 11pm</small>
                <small class="text-muted">
                  <i class="icon-home"></i>  creativeLabs HQ</small>
                <div class="avatars-stack mt-2">
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/2.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/3.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/4.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/5.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/6.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                </div>
              </div>
              <div class="list-group-item list-group-item-accent-success list-group-item-divider">
                <div>
                  <strong>#10 Startups.Garden</strong> Meetup</div>
                <small class="text-muted mr-3">
                  <i class="icon-calendar"></i>  1 - 3pm</small>
                <small class="text-muted">
                  <i class="icon-location-pin"></i>  Palo Alto, CA</small>
              </div>
              <div class="list-group-item list-group-item-accent-primary list-group-item-divider">
                <div>
                  <strong>Team meeting</strong>
                </div>
                <small class="text-muted mr-3">
                  <i class="icon-calendar"></i>  4 - 6pm</small>
                <small class="text-muted">
                  <i class="icon-home"></i>  creativeLabs HQ</small>
                <div class="avatars-stack mt-2">
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/2.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/3.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/4.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/5.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/6.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/7.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                  <div class="avatar avatar-xs">
                    <img class="img-avatar" src="assets/img/avatars/8.jpg" alt="admin@bootstrapmaster.com">
                  </div>
                </div>
              </div>
            </div>
          </tab>
          <tab>
            <ng-template tabHeading><i class="icon-speech"></i></ng-template>
            <div class="p-3">
              <div class="message">
                <div class="py-3 pb-5 mr-3 float-left">
                  <div class="avatar">
                    <img src="assets/img/avatars/7.jpg" class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span class="avatar-status badge-success"></span>
                  </div>
                </div>
                <div>
                  <small class="text-muted">Lukasz Holeczek</small>
                  <small class="text-muted float-right mt-1">1:52 PM</small>
                </div>
                <div class="text-truncate font-weight-bold">Lorem ipsum dolor sit amet</div>
                <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt...</small>
              </div>
              <hr>
              <div class="message">
                <div class="py-3 pb-5 mr-3 float-left">
                  <div class="avatar">
                    <img src="assets/img/avatars/7.jpg" class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span class="avatar-status badge-success"></span>
                  </div>
                </div>
                <div>
                  <small class="text-muted">Lukasz Holeczek</small>
                  <small class="text-muted float-right mt-1">1:52 PM</small>
                </div>
                <div class="text-truncate font-weight-bold">Lorem ipsum dolor sit amet</div>
                <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt...</small>
              </div>
              <hr>
              <div class="message">
                <div class="py-3 pb-5 mr-3 float-left">
                  <div class="avatar">
                    <img src="assets/img/avatars/7.jpg" class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span class="avatar-status badge-success"></span>
                  </div>
                </div>
                <div>
                  <small class="text-muted">Lukasz Holeczek</small>
                  <small class="text-muted float-right mt-1">1:52 PM</small>
                </div>
                <div class="text-truncate font-weight-bold">Lorem ipsum dolor sit amet</div>
                <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt...</small>
              </div>
              <hr>
              <div class="message">
                <div class="py-3 pb-5 mr-3 float-left">
                  <div class="avatar">
                    <img src="assets/img/avatars/7.jpg" class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span class="avatar-status badge-success"></span>
                  </div>
                </div>
                <div>
                  <small class="text-muted">Lukasz Holeczek</small>
                  <small class="text-muted float-right mt-1">1:52 PM</small>
                </div>
                <div class="text-truncate font-weight-bold">Lorem ipsum dolor sit amet</div>
                <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt...</small>
              </div>
              <hr>
              <div class="message">
                <div class="py-3 pb-5 mr-3 float-left">
                  <div class="avatar">
                    <img src="assets/img/avatars/7.jpg" class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span class="avatar-status badge-success"></span>
                  </div>
                </div>
                <div>
                  <small class="text-muted">Lukasz Holeczek</small>
                  <small class="text-muted float-right mt-1">1:52 PM</small>
                </div>
                <div class="text-truncate font-weight-bold">Lorem ipsum dolor sit amet</div>
                <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt...</small>
              </div>
            </div>
          </tab>
          <tab>
            <ng-template tabHeading><i class="icon-settings"></i></ng-template>
            <div class="p-3">
              <h6>Settings</h6>
              <div class="aside-options">
                <div class="clearfix mt-4">
                  <small><b>Option 1</b></small>
                  <label class="switch switch-label switch-pill switch-success switch-sm float-right">
                    <input type="checkbox" class="switch-input" checked>
                    <span class="switch-slider" data-checked="On" data-unchecked="Off"></span>
                  </label>
                </div>
                <div>
                  <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</small>
                </div>
              </div>
              <div class="aside-options">
                <div class="clearfix mt-3">
                  <small><b>Option 2</b></small>
                  <label class="switch switch-label switch-pill switch-success switch-sm float-right">
                    <input type="checkbox" class="switch-input">
                    <span class="switch-slider" data-checked="On" data-unchecked="Off"></span>
                  </label>
                </div>
                <div>
                  <small class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</small>
                </div>
              </div>
              <div class="aside-options">
                <div class="clearfix mt-3">
                  <small><b>Option 3</b></small>
                  <label class="switch switch-label switch-pill switch-success switch-sm float-right">
                    <input type="checkbox" class="switch-input">
                    <span class="switch-slider" data-checked="On" data-unchecked="Off"></span>
                    <span class="switch-handle"></span>
                  </label>
                </div>
              </div>
              <div class="aside-options">
                <div class="clearfix mt-3">
                  <small><b>Option 4</b></small>
                  <label class="switch switch-label switch-pill switch-success switch-sm float-right">
                    <input type="checkbox" class="switch-input" checked>
                    <span class="switch-slider" data-checked="On" data-unchecked="Off"></span>
                  </label>
                </div>
              </div>
              <hr>
              <h6>System Utilization</h6>
              <div class="text-uppercase mb-1 mt-4"><small><b>CPU Usage</b></small></div>
              <div class="progress progress-xs">
                <div class="progress-bar bg-info" role="progressbar" style="width: 25%" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
              </div>
              <small class="text-muted">348 Processes. 1/4 Cores.</small>
              <div class="text-uppercase mb-1 mt-2"><small><b>Memory Usage</b></small></div>
              <div class="progress progress-xs">
                <div class="progress-bar bg-warning" role="progressbar" style="width: 70%" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100"></div>
              </div>
              <small class="text-muted">11444GB/16384MB</small>
              <div class="text-uppercase mb-1 mt-2"><small><b>SSD 1 Usage</b></small></div>
              <div class="progress progress-xs">
                <div class="progress-bar bg-danger" role="progressbar" style="width: 95%" aria-valuenow="95" aria-valuemin="0" aria-valuemax="100"></div>
              </div>
              <small class="text-muted">243GB/256GB</small>
              <div class="text-uppercase mb-1 mt-2"><small><b>SSD 2 Usage</b></small></div>
              <div class="progress progress-xs">
                <div class="progress-bar bg-success" role="progressbar" style="width: 10%" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100"></div>
              </div>
              <small class="text-muted">25GB/256GB</small>
            </div>
          </tab>
        </tabset>        
        -->
        
        
        
        
      </app-aside>
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

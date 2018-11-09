import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input, NgZone,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {Router} from "@angular/router";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";
import {NavigationService} from "../../services/navigation/navigation.service";
import {PerfectScrollbarConfigInterface} from "ngx-perfect-scrollbar";

import {BehaviorSubject, Observable, of, Subscription} from "rxjs";

import {map} from "rxjs/operators";
import {SidebarNavComponent} from "./sidebar-nav.component";


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
        <sidebar-nav [perfectScrollbar] [disabled]="sidebarMinimized"></sidebar-nav>
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
export class AdminContainerComponent implements OnInit, OnDestroy{

  element: HTMLElement = document.body;

  sidebarMinimized = true;

  private _changes: MutationObserver;

  constructor(private router: Router, private navigationService: NavigationService, private sessionService: SessionService, private changeDetectorRef: ChangeDetectorRef, private zone: NgZone) {
    this._changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = document.body.classList.contains('sidebar-minimized')
    });

    this._changes.observe(<Element>this.element, {
      attributes: true
    });
  }

  ngOnInit() {
  }

  ngOnDestroy() {
  }
}

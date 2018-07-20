import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { Replace } from './../../shared';
import {SessionService} from "../../services/session/session.service";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {Session} from "../../services/session/session";

@Component({
  selector: 'admin-header',
  template: `
    <header class="admin-header">
      <ng-template [ngIf]="mobileSidebarToggler != false">
        <button class="toggler d-lg-none" type="button" sidebarToggler>
          <span class="toggler-icon"></span>
        </button>
      </ng-template>
      
      <ng-template [ngIf]="navbarBrandFull || navbarBrandMinimized">
        <a class="brand" href="#">
          <img *ngIf="navbarBrandFull"
               [src]="imgSrc(navbarBrandFull)"
               [attr.width]="imgWidth(navbarBrandFull)"
               [attr.height]="imgHeight(navbarBrandFull)"
               [attr.alt]="imgAlt(navbarBrandFull)"
               class="brand-full">
          <img *ngIf="navbarBrandMinimized"
               [src]="imgSrc(navbarBrandMinimized)"
               [attr.width]="imgWidth(navbarBrandMinimized)"
               [attr.height]="imgHeight(navbarBrandMinimized)"
               [attr.alt]="imgAlt(navbarBrandMinimized)"
               class="brand-minimized">
        </a>
      </ng-template>
      <ng-template [ngIf]="sidebarToggler != false">
        <button class="toggler d-md-down-none" type="button" [sidebarToggler]="sidebarToggler">
          <span class="toggler-icon"></span>
        </button>
      </ng-template>
      
      <ul class="nav ml-auto">
        <!--
        <li class="nav-item d-md-down-none">
          <a href="#" class="nav-link"><i class="icon-bell"></i><span class="badge badge-danger">5</span></a>
        </li>
        -->
        <li *ngIf="isLoggedIn() | async" class="nav-item" [matMenuTriggerFor]="userMenu">
          <a href="#" class="nav-link" (click)="false">
            <span class="user-icon"></span>
            <span class="user-full-name d-md-down-none">{{ userFullName() | async}}</span>
          </a>
        </li>

        <mat-menu #userMenu="matMenu" yPosition="below" overlapTrigger="false" class="user-menu">
          <a mat-menu-item href="#"><i class="fas fa-user-circle"></i> Profile</a>
          <a mat-menu-item href="#"><i class="fas fa-cogs"></i> Settings</a>
          <a mat-menu-item href="#" (click)="logout()"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </mat-menu>
        
        <li *ngIf="!(isLoggedIn() | async)" class="nav-item">
          <a class="nav-link" (click)="login()">
            <span class="login-icon"></span>
            <span class="login d-md-down-none">Login</span>
          </a>
        </li>
      </ul>
    </header>
  `
})
export class AdminHeaderComponent implements OnInit {

  @Input() fixed: boolean;

  @Input() navbarBrandFull: any;
  @Input() navbarBrandMinimized: any;

  @Input() sidebarToggler: any;
  @Input() mobileSidebarToggler: any;

  constructor(private el: ElementRef, private router: Router, private sessionService: SessionService) {}

  ngOnInit() {
    Replace(this.el);
    this.isFixed(this.fixed);
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('admin-header-fixed'); }
  }

  imgSrc(brand: any): void {
    return brand.src ? brand.src : '';
  }

  imgWidth(brand: any): void {
    return brand.width ? brand.width : 'auto';
  }

  imgHeight(brand: any): void {
    return brand.height ? brand.height : 'auto';
  }

  imgAlt(brand: any): void {
    return brand.alt ? brand.alt : '';
  }

  breakpoint(breakpoint: any): void {
    console.log(breakpoint);
    return breakpoint ? breakpoint : '';
  }

  isLoggedIn(): Observable<boolean> {

    return this.sessionService.getSession().map((session : (Session | null)) => {

      if (session) {
        return true;
      }
      else {
        return false;
      }
    });
  }

  login() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.sessionService.logout();

    this.router.navigate(['/']);
  }

  userFullName(): Observable<string> {
    return this.sessionService.getSession().map((session: Session) => {

      if (session) {
        return session.username;
      }
      else {
        return '';
      }
    });
  }
}

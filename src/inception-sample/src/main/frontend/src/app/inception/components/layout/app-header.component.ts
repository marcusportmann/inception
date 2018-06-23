import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { Replace } from './../../shared';

@Component({
  selector: 'app-header',
  template: `
    <header class="app-header navbar">
      <ng-template [ngIf]="mobileSidebarToggler != false">
        <button class="navbar-toggler d-lg-none" type="button" appSidebarToggler>
          <span class="navbar-toggler-icon"></span>
        </button>
      </ng-template>
      <ng-template [ngIf]="navbarBrand || navbarBrandFull || navbarBrandMinimized">
        <a class="navbar-brand" href="#">
          <img *ngIf="navbarBrand"
               [src]="imgSrc(navbarBrand)"
               [attr.width]="imgWidth(navbarBrand)"
               [attr.height]="imgHeight(navbarBrand)"
               [attr.alt]="imgAlt(navbarBrand)"
               class="navbar-brand">
          <img *ngIf="navbarBrandFull"
               [src]="imgSrc(navbarBrandFull)"
               [attr.width]="imgWidth(navbarBrandFull)"
               [attr.height]="imgHeight(navbarBrandFull)"
               [attr.alt]="imgAlt(navbarBrandFull)"
               class="navbar-brand-full">
          <img *ngIf="navbarBrandMinimized"
               [src]="imgSrc(navbarBrandMinimized)"
               [attr.width]="imgWidth(navbarBrandMinimized)"
               [attr.height]="imgHeight(navbarBrandMinimized)"
               [attr.alt]="imgAlt(navbarBrandMinimized)"
               class="navbar-brand-minimized">
        </a>
      </ng-template>
      <ng-template [ngIf]="sidebarToggler != false">
        <button class="navbar-toggler d-md-down-none" type="button" [appSidebarToggler]="sidebarToggler">
          <span class="navbar-toggler-icon"></span>
        </button>
      </ng-template>
      <ng-content></ng-content>
      <ng-template [ngIf]="asideToggler != false">
        <button class="navbar-toggler d-md-down-none" type="button" [appAsideToggler]="asideToggler">
          <span class="navbar-toggler-icon"></span>
        </button>
      </ng-template>
      <ng-template [ngIf]="mobileAppAsideToggler != false">
        <button class="navbar-toggler d-lg-none" type="button" appAsideMenuToggler>
          <span class="navbar-toggler-icon"></span>
        </button>
      </ng-template>
    </header>
  `
})
export class AppHeaderComponent implements OnInit {

  @Input() fixed: boolean;

  @Input() navbarBrand: any;
  @Input() navbarBrandFull: any;
  @Input() navbarBrandMinimized: any;

  @Input() sidebarToggler: any;
  @Input() mobileSidebarToggler: any;

  @Input() asideToggler: any;
  @Input() mobileAsideToggler: any;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    Replace(this.el);
    this.isFixed(this.fixed);
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('app-header-fixed'); }
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
}















// import {Component, OnInit} from '@angular/core';
// import {SessionService} from "../../../services/session/session.service";
// import {Observable} from "rxjs/Observable";
//
//
// @Component({
//   selector: 'app-header',
//   templateUrl: './app-header.component.html'
// })
// export class AppHeaderComponent implements OnInit {
//
//   public showLogin = false;
//
//
//   public constructor(private sessionService: SessionService) {
//
//   }
//
//   ngOnInit(): void {
//   }
//
//
//   public isLoggedIn(): Observable<boolean> {
//
//     return Observable.of(true);
//
//
//     //console.log('Invoking AppHeaderComponent::isLoggedIn()');
//
//     //return Session.getSession().isLoggedIn();
//
//     //return false;
//   }
//
//
//
//
// }

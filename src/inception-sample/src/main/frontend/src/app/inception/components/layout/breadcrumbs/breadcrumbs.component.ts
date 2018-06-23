import { Component, ElementRef, Input, OnInit  } from '@angular/core';
import { Replace } from './../../../shared';
import {BreadcrumbsService} from "../../../services/breadcrumbs/breadcrumbs.service";


@Component({
  selector: 'inception-layout-breadcrumbs',
  template: `
    <ng-template ngFor let-breadcrumb [ngForOf]="breadcrumbs | async" let-last = last>
      <li class="breadcrumb-item"
          *ngIf="(breadcrumb.label.title&&(!last)) || (breadcrumb.label.title&&last)"
          [ngClass]="{active: last}">
        <a *ngIf="!last" [routerLink]="breadcrumb.url">{{breadcrumb.label.title}}</a>
        <span *ngIf="last" [routerLink]="breadcrumb.url">{{breadcrumb.label.title}}</span>
      </li>
    </ng-template>
  `
})
export class BreadcrumbsComponent implements OnInit {
  @Input() fixed: boolean;
  public breadcrumbs;

  constructor(public breadcrumbsService: BreadcrumbsService, public el: ElementRef) { }

  public ngOnInit(): void {
    Replace(this.el);
    this.isFixed(this.fixed);
    this.breadcrumbs = this.breadcrumbsService.breadcrumbs;
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('breadcrumbs-fixed'); }
  }
}













// import {Component, OnInit} from '@angular/core';
// import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
// import 'rxjs/add/operator/filter';
//
// @Component({
//   selector: 'layout-breadcrumbs',
//   template: `
//   <ng-template ngFor let-breadcrumb [ngForOf]="breadcrumbs" let-last = last>
//     <li class="breadcrumb-item"
//         *ngIf="(breadcrumb.label.title&&(!last)) || (breadcrumb.label.title&&last)"
//         [ngClass]="{active: last}">
//       <a *ngIf="!last" [routerLink]="breadcrumb.url">{{breadcrumb.label.title}}</a>
//       <span *ngIf="last" [routerLink]="breadcrumb.url">{{breadcrumb.label.title}}</span>
//     </li>
//   </ng-template>`
// })
// export class BreadcrumbsComponent implements OnInit {
//   breadcrumbs: Array<Object>;
//   constructor(
//     private router: Router,
//     private route: ActivatedRoute
//   ) {
//     this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event) => {
//       this.breadcrumbs = [];
//       let currentRoute = this.route.root,
//       url = '';
//       do {
//         const childrenRoutes = currentRoute.children;
//         currentRoute = null;
//         // tslint:disable-next-line:no-shadowed-variable
//         childrenRoutes.forEach(route => {
//           if (route.outlet === 'primary') {
//             const routeSnapshot = route.snapshot;
//
//             if (routeSnapshot.url.length > 0) {
//
//               url += '/' + routeSnapshot.url.map(segment => segment.path).join('/');
//               this.breadcrumbs.push({
//                 label: route.snapshot.data,
//                 url: url
//               });
//             }
//
//             currentRoute = route;
//           }
//         });
//       } while (currentRoute);
//     });
//   }
//
//   ngOnInit(): void {
//   }
//
// }

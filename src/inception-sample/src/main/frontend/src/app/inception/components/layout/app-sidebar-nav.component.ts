import { Component, Directive, ElementRef, HostBinding, HostListener, Input, OnInit, Renderer2, ViewEncapsulation } from '@angular/core';
import { Replace } from '../../shared/index';

@Component({
  selector: 'app-sidebar-nav',
  template: `
    <ul class="nav">
      <ng-template ngFor let-navitem [ngForOf]="navItems">
        <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
        <ng-template [ngIf]="isTitle(navitem)">
          <app-sidebar-nav-title [title]='navitem'></app-sidebar-nav-title>
        </ng-template>
        <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
          <app-sidebar-nav-item [item]='navitem'></app-sidebar-nav-item>
        </ng-template>
      </ng-template>
    </ul>`
})
export class AppSidebarNavComponent {
  @Input() navItems: any;

  @HostBinding('class.app-sidebar-nav') true;
  @HostBinding('attr.role') role = 'nav';

  constructor() {

  }

  isDivider(item) {
    return item.divider ? true : false;
  }

  isTitle(item) {
    return item.title ? true : false;
  }




}

import { Router } from '@angular/router';
import {NavigationService} from "../../services/navigation/navigation.service";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";

@Component({
  selector: 'app-sidebar-nav-item',
  template: `
    <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.class : 'nav-item'">
      <app-sidebar-nav-link [link]='item'></app-sidebar-nav-link>
    </li>
    <ng-template #dropdown>
      <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.class : 'nav-item nav-dropdown'"
          [class.open]="isActive()"
          routerLinkActive="open"
          appSidebarNavDropdown>
        <app-sidebar-nav-dropdown [link]='item'></app-sidebar-nav-dropdown>
      </li>
    </ng-template>
    `
})
export class AppSidebarNavItemComponent implements OnInit {
  @Input() item: any;

  public hasClass() {
    return this.item.class ? true : false;
  }

  public isDropdown() {
    return this.item.children ? true : false;
  }

  public thisUrl() {
    return this.item.url;
  }

  public isActive() {
    return this.router.isActive(this.thisUrl(), false);
  }

  constructor( private router: Router, private el: ElementRef ) { }

  ngOnInit() {
    Replace(this.el);
  }

}

@Component({
  selector: 'app-sidebar-nav-link',
  template: `
    <a *ngIf="!isExternalLink(); else external"
      [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'"
      routerLinkActive="active"
      [routerLink]="[link.url]"
      (click)="hideMobile()">
      <i *ngIf="isIcon()" class="nav-icon {{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ng-template #external>
      <a [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'" href="{{link.url}}">
        <i *ngIf="isIcon()" class="nav-icon {{ link.icon }}"></i>
        {{ link.name }}
        <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
      </a>
    </ng-template>
  `
})
export class AppSidebarNavLinkComponent implements OnInit {
  @Input() link: any;

  public hasVariant() {
    return this.link.variant ? true : false;
  }

  public isBadge() {
    return this.link.badge ? true : false;
  }

  public isExternalLink() {
    return this.link.url.substring(0, 4) === 'http' ? true : false;
  }

  public isIcon() {
    return this.link.icon ? true : false;
  }

  public hideMobile() {
    if (document.body.classList.contains('app-sidebar-show')) {
      document.body.classList.toggle('app-sidebar-show');
    }
  }

  constructor( private router: Router, private el: ElementRef ) { }

  ngOnInit() {
    Replace(this.el);
  }
}

@Component({
  selector: 'app-sidebar-nav-dropdown',
  template: `
    <a class="nav-link nav-dropdown-toggle" appSidebarNavDropdownToggler>
      <i *ngIf="isIcon()" class="nav-icon {{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ul class="nav-dropdown-items">
      <ng-template ngFor let-child [ngForOf]="link.children">
        <app-sidebar-nav-item [item]='child'></app-sidebar-nav-item>
      </ng-template>
    </ul>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }']
})
export class AppSidebarNavDropdownComponent implements OnInit {
  @Input() link: any;

  public isBadge() {
    return this.link.badge ? true : false;
  }

  public isIcon() {
    return this.link.icon ? true : false;
  }

  constructor( private router: Router, private el: ElementRef ) { }

  ngOnInit() {
    Replace(this.el);
  }
}

@Component({
  selector: 'app-sidebar-nav-title',
  template: ''
})
export class AppSidebarNavTitleComponent implements OnInit {
  @Input() title: any;

  constructor(private el: ElementRef, private renderer: Renderer2) { }

  ngOnInit() {
    const nativeElement: HTMLElement = this.el.nativeElement;
    const li = this.renderer.createElement('li');
    const name = this.renderer.createText(this.title.name);

    this.renderer.addClass(li, 'nav-title');

    if ( this.title.class ) {
      const classes = this.title.class;
      this.renderer.addClass(li, classes);
    }

    if ( this.title.wrapper ) {
      const wrapper = this.renderer.createElement(this.title.wrapper.element);

      this.renderer.appendChild(wrapper, name);
      this.renderer.appendChild(li, wrapper);
    } else {
      this.renderer.appendChild(li, name);
    }
    this.renderer.appendChild(nativeElement, li);
    Replace(this.el);
  }
}















// import {Component, ElementRef, Input, OnInit, Renderer2} from '@angular/core';
//
// // Import navigation elements
//
// import {Route, Router, RouterState, RouterStateSnapshot} from '@angular/router';
// import {NavigationService} from "../../../services/navigation/navigation.service";
// import {NavigationItem} from "../../../services/navigation/navigation-item";
// import {Observable} from "rxjs/Observable";
// import {Session} from "../../../services/session/session";
// import {SessionService} from "../../../services/session/session.service";
// import {NavigationBadge} from "../../../services/navigation/navigation-badge";
//
//
// //import { navigation } from '../../../../navigation';
//
// @Component({
//   selector: 'app-sidebar-nav',
//   template: `
//     <nav class="app-sidebar-nav">
//       <ul class="nav">
//         <ng-template ngFor let-navitem [ngForOf]="navigation">
//           <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
//           <ng-template [ngIf]="isTitle(navitem)">
//             <app-sidebar-nav-title [title]='navitem'></app-sidebar-nav-title>
//           </ng-template>
//           <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
//             <app-sidebar-nav-item [item]='navitem'></app-sidebar-nav-item>
//           </ng-template>
//         </ng-template>
//       </ul>
//     </nav>`
// })
// export class AppSidebarNavComponent implements OnInit {
//

//
//   isDivider(item) {
//     return !!item.divider;
//   }
//
//   isTitle(item) {
//     return !!item.title;
//   }
//
// }
//
// @Component({
//   selector: 'app-sidebar-nav-item',
//   template: `
//     <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.class : 'nav-item'">
//       <app-sidebar-nav-link [link]='item'></app-sidebar-nav-link>
//     </li>
//     <ng-template #dropdown>
//       <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.class : 'nav-item nav-dropdown'"
//           [class.open]="isActive()"
//           routerLinkActive="open"
//           inceptionNavDropdown>
//         <app-sidebar-nav-dropdown [link]='item'></app-sidebar-nav-dropdown>
//       </li>
//     </ng-template>
//   `
// })
// export class AppSidebarNavItemComponent implements OnInit {
//   @Input() item: any;
//
//   public hasClass() {
//     return !!this.item.class;
//   }
//
//   public isDropdown() {
//     return !!this.item.children;
//   }
//
//   public thisUrl() {
//     return this.item.url;
//   }
//
//   public isActive() {
//     return this.router.isActive(this.thisUrl(), false);
//   }
//
//   constructor(private router: Router) {
//   }
//
//   ngOnInit(): void {
//   }
//
// }
//
// @Component({
//   selector: 'app-sidebar-nav-link',
//   template: `
//     <a *ngIf="!isExternalLink(); else external"
//        [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'"
//        routerLinkActive="active"
//        [routerLink]="[link.url]">
//       <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
//       {{ link.name }}
//       <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
//     </a>
//     <ng-template #external>
//       <a [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'" href="{{link.url}}">
//         <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
//         {{ link.name }}
//         <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
//       </a>
//     </ng-template>
//   `
// })
// export class AppSidebarNavLinkComponent implements OnInit {
//   @Input() link: any;
//
//   public hasVariant() {
//     return !!this.link.variant;
//   }
//
//   public isBadge() {
//     return !!this.link.badge;
//   }
//
//   public isExternalLink() {
//     return this.link.url.substring(0, 4) === 'http';
//   }
//
//   public isIcon() {
//     return !!this.link.icon;
//   }
//
//   constructor() {
//   }
//
//
//   ngOnInit(): void {
//   }
//
// }
//
// @Component({
//   selector: 'app-sidebar-nav-dropdown',
//   template: `
//     <a class="nav-link nav-dropdown-toggle" inceptionNavDropdownToggle>
//       <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
//       {{ link.name }}
//       <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
//     </a>
//     <ul class="nav-dropdown-items">
//       <ng-template ngFor let-child [ngForOf]="link.children">
//         <app-sidebar-nav-item [item]='child'></app-sidebar-nav-item>
//       </ng-template>
//     </ul>
//   `
// })
// export class AppSidebarNavDropdownComponent implements OnInit {
//   @Input() link: any;
//
//   public isBadge() {
//     return !!this.link.badge;
//   }
//
//   public isIcon() {
//     return !!this.link.icon;
//   }
//
//   constructor() {
//   }
//
//   ngOnInit(): void {
//   }
//
// }
//
// @Component({
//   selector: 'app-sidebar-nav-title',
//   template: ''
// })
// export class AppSidebarNavTitleComponent implements OnInit {
//   @Input() title: any;
//
//   constructor(private el: ElementRef, private renderer: Renderer2) {
//   }
//
//   ngOnInit() {
//     const nativeElement: HTMLElement = this.el.nativeElement;
//     const li = this.renderer.createElement('li');
//     const name = this.renderer.createText(this.title.name);
//
//     this.renderer.addClass(li, 'nav-title');
//
//     if (this.title.class) {
//       const classes = this.title.class;
//       this.renderer.addClass(li, classes);
//     }
//
//     if (this.title.wrapper) {
//       const wrapper = this.renderer.createElement(this.title.wrapper.element);
//
//       this.renderer.appendChild(wrapper, name);
//       this.renderer.appendChild(li, wrapper);
//     } else {
//       this.renderer.appendChild(li, name);
//     }
//     this.renderer.appendChild(nativeElement, li);
//   }
// }
//

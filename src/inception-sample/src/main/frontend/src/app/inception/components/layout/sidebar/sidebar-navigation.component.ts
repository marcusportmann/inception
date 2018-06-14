import { Component, Directive, ElementRef, HostBinding, HostListener, Input, OnInit, Renderer2, ViewEncapsulation } from '@angular/core';
import { Replace } from './../../../shared';

@Directive({
  selector: '[inceptionNavigationDropdown]'
})
export class NavigationDropdownDirective {

  constructor(private el: ElementRef) { }

  toggle() {
    this.el.nativeElement.classList.toggle('open');
  }
}

/**
 * Allows the dropdown to be toggled via click.
 */
@Directive({
  selector: '[inceptionNavigationDropdownToggle]'
})
export class NavigationDropdownToggleDirective {
  constructor(private dropdown: NavigationDropdownDirective) {}

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    this.dropdown.toggle();
  }
}

@Component({
  selector: 'inception-layout-sidebar-navigation',
  template: `
    <ul class="nav">
      <ng-template ngFor let-navitem [ngForOf]="navItems">
        <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
        <ng-template [ngIf]="isTitle(navitem)">
          <inception-layout-sidebar-navigation-title [title]='navitem'></inception-layout-sidebar-navigation-title>
        </ng-template>
        <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
          <inception-layout-sidebar-navigation-item [item]='navitem'></inception-layout-sidebar-navigation-item>
        </ng-template>
      </ng-template>
    </ul>`
})
export class SidebarNavigationComponent {
  @Input() navItems: any;

  @HostBinding('class.sidebar-nav') true;
  @HostBinding('attr.role') role = 'nav';

  public isDivider(item) {
    return item.divider ? true : false;
  }

  public isTitle(item) {
    return item.title ? true : false;
  }

  constructor() { }
}

import { Router } from '@angular/router';

@Component({
  selector: 'inception-layout-sidebar-navigation-item',
  template: `
    <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.class : 'nav-item'">
      <inception-layout-sidebar-navigation-link [link]='item'></inception-layout-sidebar-navigation-link>
    </li>
    <ng-template #dropdown>
      <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.class : 'nav-item nav-dropdown'"
          [class.open]="isActive()"
          routerLinkActive="open"
          inceptionNavigationDropdown>
        <inception-layout-sidebar-navigation-dropdown [link]='item'></inception-layout-sidebar-navigation-dropdown>
      </li>
    </ng-template>
    `
})
export class SidebarNavigationItemComponent implements OnInit {
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
  selector: 'inception-layout-sidebar-navigation-link',
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
export class SidebarNavigationLinkComponent implements OnInit {
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
    if (document.body.classList.contains('sidebar-mobile-show')) {
      document.body.classList.toggle('sidebar-mobile-show');
    }
  }

  constructor( private router: Router, private el: ElementRef ) { }

  ngOnInit() {
    Replace(this.el);
  }
}

@Component({
  selector: 'inception-layout-sidebar-navigation-dropdown',
  template: `
    <a class="nav-link nav-dropdown-toggle" inceptionNavigationDropdownToggle>
      <i *ngIf="isIcon()" class="nav-icon {{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ul class="nav-dropdown-items">
      <ng-template ngFor let-child [ngForOf]="link.children">
        <inception-layout-sidebar-navigation-item [item]='child'></inception-layout-sidebar-navigation-item>
      </ng-template>
    </ul>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }']
})
export class SidebarNavigationDropdownComponent implements OnInit {
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
  selector: 'inception-layout-sidebar-navigation-title',
  template: ''
})
export class SidebarNavigationTitleComponent implements OnInit {
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
//   selector: 'inception-layout-sidebar-navigation',
//   template: `
//     <nav class="sidebar-nav">
//       <ul class="nav">
//         <ng-template ngFor let-navitem [ngForOf]="navigation">
//           <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
//           <ng-template [ngIf]="isTitle(navitem)">
//             <inception-layout-sidebar-navigation-title [title]='navitem'></inception-layout-sidebar-navigation-title>
//           </ng-template>
//           <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
//             <inception-layout-sidebar-navigation-item [item]='navitem'></inception-layout-sidebar-navigation-item>
//           </ng-template>
//         </ng-template>
//       </ul>
//     </nav>`
// })
// export class SidebarNavigationComponent implements OnInit {
//
//   navigation: NavigationItem[] = [];
//
//   constructor(private navigationService: NavigationService, private sessionService: SessionService) {
//   }
//
//   filterNavigationItems(navigationItems: NavigationItem[], session: Session): NavigationItem[] {
//
//     if (!navigationItems) {
//       return navigationItems;
//     }
//
//     var filteredNavigationItems: NavigationItem[] = [];
//
//     for (var i = 0; i < navigationItems.length; i++) {
//
//       var navigationItem: NavigationItem = navigationItems[i];
//
//       var functionCodes = (navigationItem.functionCodes == null) ? [] : navigationItem.functionCodes;
//
//       if (functionCodes.length > 0) {
//
//         if (session) {
//
//           for (var j = 0; j < functionCodes.length; j++) {
//             for (var k = 0; k < session.functionCodes.length; k++) {
//               if (functionCodes[j] == session.functionCodes[k]) {
//
//                 var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);
//
//                 filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
//                   navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems,
//                   navigationItem.badge));
//               }
//             }
//           }
//         }
//       }
//       else {
//
//         var filteredChildNavigationItems: NavigationItem[] =  this.filterNavigationItems(navigationItem.children, session);
//
//         filteredNavigationItems.push(new NavigationItem(navigationItem.icon, navigationItem.name,
//           navigationItem.url, navigationItem.functionCodes, filteredChildNavigationItems,
//           navigationItem.badge));
//       }
//     }
//
//     return filteredNavigationItems;
//   }
//
//   isDivider(item) {
//     return !!item.divider;
//   }
//
//   isTitle(item) {
//     return !!item.title;
//   }
//
//   ngOnInit(): void {
//
//     // Retrieve and filter the navigation items
//     this.sessionService.getSession().map((session: Session) => {
//       this.navigation = this.filterNavigationItems(this.navigationService.getNavigation(), session);
//     }).subscribe();
//   }
// }
//
// @Component({
//   selector: 'inception-layout-sidebar-navigation-item',
//   template: `
//     <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.class : 'nav-item'">
//       <inception-layout-sidebar-navigation-link [link]='item'></inception-layout-sidebar-navigation-link>
//     </li>
//     <ng-template #dropdown>
//       <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.class : 'nav-item nav-dropdown'"
//           [class.open]="isActive()"
//           routerLinkActive="open"
//           inceptionNavDropdown>
//         <inception-layout-sidebar-navigation-dropdown [link]='item'></inception-layout-sidebar-navigation-dropdown>
//       </li>
//     </ng-template>
//   `
// })
// export class SidebarNavigationItemComponent implements OnInit {
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
//   selector: 'inception-layout-sidebar-navigation-link',
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
// export class SidebarNavigationLinkComponent implements OnInit {
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
//   selector: 'inception-layout-sidebar-navigation-dropdown',
//   template: `
//     <a class="nav-link nav-dropdown-toggle" inceptionNavDropdownToggle>
//       <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
//       {{ link.name }}
//       <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
//     </a>
//     <ul class="nav-dropdown-items">
//       <ng-template ngFor let-child [ngForOf]="link.children">
//         <inception-layout-sidebar-navigation-item [item]='child'></inception-layout-sidebar-navigation-item>
//       </ng-template>
//     </ul>
//   `
// })
// export class SidebarNavigationDropdownComponent implements OnInit {
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
//   selector: 'inception-layout-sidebar-navigation-title',
//   template: ''
// })
// export class SidebarNavigationTitleComponent implements OnInit {
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

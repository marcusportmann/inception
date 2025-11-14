/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { NgClass, NgForOf, NgIf, NgTemplateOutlet } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input
} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import {
  SidebarNavDropdownTogglerDirective
} from '../directives/sidebar-nav-dropdown-toggler.directive';
import { SidebarNavDropdownDirective } from '../directives/sidebar-nav-dropdown.directive';
import { NavigationItem } from '../services/navigation-item';

/**
 * The SidebarNavItemComponent class implements the sidebar nav item component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-core-sidebar-nav-item',
  standalone: true,
  template: `
    <ng-container *ngIf="navItem as item">
      <!-- Divider -->
      <li *ngIf="item.divider" class="nav-divider"></li>

      <!-- Title -->
      <li *ngIf="!item.divider && item.title" class="nav-title">
        {{ item.name }}
      </li>

      <!-- Dropdown -->
      <li
        *ngIf="!item.divider && !item.title && item.children?.length; else sidebarNavLink"
        [ngClass]="['nav-item', 'nav-dropdown', item.cssClass || '']"
        [class.open]="isActive()"
        sidebarNavDropdown>
        <div class="sidebar-nav-dropdown">
          <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
            <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
            </ng-container>
          </a>
          <ul class="nav-dropdown-items">
            <inception-core-sidebar-nav-item *ngFor="let child of item.children" [navItem]="child">
            </inception-core-sidebar-nav-item>
          </ul>
        </div>
      </li>

      <!-- Simple link (internal / external) -->
      <ng-template #sidebarNavLink>
        <li [ngClass]="['nav-item', item.cssClass || '']">
          <!-- Internal route -->
          <a
            *ngIf="!isExternalLink; else externalLink"
            [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
            [routerLink]="[item.url]"
            routerLinkActive="active"
            (click)="hideMobile()">
            <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
            </ng-container>
          </a>

          <!-- External link -->
          <ng-template #externalLink>
            <a
              [ngClass]="['nav-link', item.variant ? 'nav-link-' + item.variant : '']"
              [href]="item.url">
              <ng-container *ngTemplateOutlet="navContent; context: { $implicit: item }">
              </ng-container>
            </a>
          </ng-template>
        </li>
      </ng-template>

      <!-- Shared icon / text / badge template -->
      <ng-template #navContent let-item>
        <i *ngIf="item.icon" class="nav-icon {{ item.icon }}"></i>
        <span class="nav-item-name">{{ item.name }}</span>
        <span *ngIf="item.badge" class="badge" [ngClass]="'badge-' + item.badge.variant">
          {{ item.badge.text }}
        </span>
      </ng-template>
    </ng-container>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }'],
  imports: [
    SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective,
    NgClass,
    RouterLink,
    RouterLinkActive,
    NgIf,
    NgTemplateOutlet,
    NgForOf
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarNavItemComponent {
  @Input() navItem?: NavigationItem;

  constructor(private router: Router) {}

  hideMobile(): void {
    document.body.classList.remove('sidebar-show');
  }

  isActive(): boolean {
    const url = this.navItem?.url;
    return !!url && this.router.isActive(url, false);
  }

  get isExternalLink(): boolean {
    const url = this.navItem?.url ?? '';
    // Handles http and https, case-insensitive
    return /^https?:\/\//i.test(url);
  }
}


// import {
//   ChangeDetectionStrategy,
//   Component,
//   ElementRef,
//   Input
// } from '@angular/core';
// import { Router } from '@angular/router';
// import {
//   SidebarNavDropdownTogglerDirective
// } from '../directives/sidebar-nav-dropdown-toggler.directive';
// import { SidebarNavDropdownDirective } from '../directives/sidebar-nav-dropdown.directive';
// import { NavigationItem } from '../services/navigation-item';
//
// /**
//  * The SidebarNavItemComponent class implements the sidebar nav item component.
//  *
//  * @author Marcus Portmann
//  */
// @Component({
//   selector: 'inception-core-sidebar-nav-item',
//   template: `
//     <ng-container *ngIf="this.navItem">
//       <ng-container *ngIf="isDivider(); else checkForTitle">
//         <li class="nav-divider"></li>
//       </ng-container>
//       <ng-template #checkForTitle>
//         <ng-container *ngIf="isTitle(); else checkForDropdown">
//           <li class="nav-title">{{ navItem?.name }}</li>
//         </ng-container>
//       </ng-template>
//       <ng-template #checkForDropdown>
//         <ng-container *ngIf="isDropdown(); else sidebarNavLink">
//           <li
//             class="{{
//               !!this.navItem.cssClass
//                 ? 'nav-item nav-dropdown ' + navItem?.cssClass
//                 : 'nav-item nav-dropdown'
//             }}"
//             [class.open]="isActive()"
//             sidebarNavDropdown>
//             <div class="sidebar-nav-dropdown">
//               <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
//                 <i
//                   *ngIf="this.navItem.icon && !!this.navItem.icon"
//                   class="nav-icon {{ navItem?.icon }}"></i>
//                 <span class="nav-item-name">{{ navItem?.name }}</span>
//                 <span
//                   *ngIf="this.navItem.badge && !!this.navItem.badge"
//                   [ngClass]="'badge badge-' + navItem?.badge?.variant">
//                   {{ navItem?.badge?.text }}
//                 </span>
//               </a>
//               <ul class="nav-dropdown-items">
//                 <sidebar-nav-item
//                   *ngFor="let child of navItem?.children"
//                   [navItem]="child"></sidebar-nav-item>
//               </ul>
//             </div>
//           </li>
//         </ng-container>
//       </ng-template>
//       <ng-template #sidebarNavLink>
//         <li [ngClass]="!!this.navItem.cssClass ? 'nav-item ' + navItem?.cssClass : 'nav-item'">
//           <a
//             *ngIf="!isExternalLink(); else externalLink"
//             [ngClass]="
//               !!this.navItem.variant ? 'nav-link nav-link-' + navItem?.variant : 'nav-link'
//             "
//             routerLinkActive="active"
//             [routerLink]="[navItem?.url]"
//             (click)="hideMobile()">
//             <i *ngIf="!!this.navItem.icon" class="nav-icon {{ navItem?.icon }}"></i>
//             <span class="nav-item-name">{{ navItem?.name }}</span>
//             <span
//               *ngIf="this.navItem.badge && !!this.navItem.badge"
//               [ngClass]="'badge badge-' + navItem?.badge?.variant">
//               {{ navItem?.badge?.text }}
//             </span>
//           </a>
//           <ng-template #externalLink>
//             <a
//               [ngClass]="
//                 !!this.navItem.variant ? 'nav-link nav-link-' + navItem?.variant : 'nav-link'
//               "
//               href="{{ navItem?.url }}">
//               <i *ngIf="!!this.navItem.icon" class="nav-icon {{ navItem?.icon }}"></i>
//               <span class="nav-item-name">{{ navItem?.name }}</span>
//               <span
//                 *ngIf="this.navItem.badge && !!this.navItem.badge"
//                 [ngClass]="'badge badge-' + navItem?.badge?.variant">
//                 {{ navItem?.badge?.text }}
//               </span>
//             </a>
//           </ng-template>
//         </li>
//       </ng-template>
//     </ng-container>
//   `,
//   styles: ['.nav-dropdown-toggle { cursor: pointer; }'],
//   imports: [SidebarNavDropdownDirective, SidebarNavDropdownTogglerDirective],
//   changeDetection: ChangeDetectionStrategy.OnPush
// })
// export class SidebarNavItemComponent {
//   @Input() navItem?: NavigationItem;
//
//   /**
//    * Constructs a new SidebarNavItemComponent.
//    *
//    * @param elementRef        The element reference.
//    * @param router            The router.
//    */
//   constructor(
//     private elementRef: ElementRef,
//     private router: Router
//   ) {}
//
//   hideMobile() {
//     if (document.body.classList.contains('sidebar-show')) {
//       document.body.classList.toggle('sidebar-show');
//     }
//   }
//
//   isActive(): boolean {
//     return this.navItem
//       ? !!this.navItem.url && this.router.isActive(this.navItem.url, false)
//       : false;
//   }
//
//   isDivider(): boolean {
//     return this.navItem ? this.navItem.divider : false;
//   }
//
//   isDropdown(): boolean {
//     return this.navItem ? this.navItem.children.length > 0 : false;
//   }
//
//   isExternalLink(): boolean {
//     return this.navItem ? !!this.navItem.url && this.navItem.url.substring(0, 4) === 'http' : false;
//   }
//
//   isTitle(): boolean {
//     return this.navItem ? this.navItem.title : false;
//   }
// }

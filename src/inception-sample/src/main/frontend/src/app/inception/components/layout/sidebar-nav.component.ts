/*
 * Copyright 2018 Marcus Portmann
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

import {
  Component,
  ElementRef,
  HostBinding,
  Input,
  OnInit,
  Renderer2,
} from '@angular/core';
import {Replace} from '../../shared/index';

@Component({
  selector: 'sidebar-nav',
  template: `
    <ul class="nav">
      <ng-template ngFor let-navitem [ngForOf]="navItems">
        <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
        <ng-template [ngIf]="isTitle(navitem)">
          <sidebar-nav-title [title]='navitem'></sidebar-nav-title>
        </ng-template>
        <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
          <sidebar-nav-item [item]='navitem'></sidebar-nav-item>
        </ng-template>
      </ng-template>
    </ul>`
})
export class SidebarNavComponent {
  @Input() navItems: NavigationItem[];

  @HostBinding('class.sidebar-nav') true;
  @HostBinding('attr.role') role = 'nav';

  constructor() {
  }

  isDivider(item): boolean {
    return item.divider ? true : false;
  }

  isTitle(item): boolean {
    return item.title ? true : false;
  }
}

import {Router} from '@angular/router';
import {NavigationService} from "../../services/navigation/navigation.service";
import {SessionService} from "../../services/session/session.service";
import {NavigationItem} from "../../services/navigation/navigation-item";
import {Session} from "../../services/session/session";

@Component({
  selector: 'sidebar-nav-item',
  template: `
    <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.cssClass : 'nav-item'">
      <sidebar-nav-link [link]='item'></sidebar-nav-link>
    </li>
    <ng-template #dropdown>
      <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.cssClass : 'nav-item nav-dropdown'"
          [class.open]="isActive()"
          routerLinkActive="open"
          sidebarNavDropdown>
        <sidebar-nav-dropdown [link]='item'></sidebar-nav-dropdown>
      </li>
    </ng-template>
  `
})
export class SidebarNavItemComponent implements OnInit {
  @Input() item: NavigationItem;

  constructor(private router: Router, private el: ElementRef) {
  }

  hasClass(): boolean {
    return this.item.cssClass ? true : false;
  }

  isActive(): boolean {
    if (this.item.url) {
      return this.router.isActive(this.thisUrl(), false);
    }
    else {
      return false;
    }
  }

  isDropdown(): boolean {
    return this.item.children ? true : false;
  }

  ngOnInit() {
    Replace(this.el);
  }

  thisUrl(): string {
    return this.item.url;
  }
}

@Component({
  selector: 'sidebar-nav-link',
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
export class SidebarNavLinkComponent implements OnInit {
  @Input() link: any;

  public hasVariant() {
    return this.link.variant ? true : false;
  }

  public isBadge() {
    return this.link.badge ? true : false;
  }

  public isExternalLink() {

    if (this.link) {
      if (this.link.url) {
        return this.link.url.substring(0, 4) === 'http' ? true : false;
      }
    }

    return false;
  }

  public isIcon() {
    return this.link.icon ? true : false;
  }

  public hideMobile() {
    if (document.body.classList.contains('sidebar-show')) {
      document.body.classList.toggle('sidebar-show');
    }
  }

  constructor(private router: Router, private el: ElementRef) {
  }

  ngOnInit() {
    Replace(this.el);
  }
}

@Component({
  selector: 'sidebar-nav-dropdown',
  template: `
    <a class="nav-link nav-dropdown-toggle" sidebarNavDropdownToggler>
      <i *ngIf="isIcon()" class="nav-icon {{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ul class="nav-dropdown-items">
      <ng-template ngFor let-child [ngForOf]="link.children">
        <sidebar-nav-item [item]='child'></sidebar-nav-item>
      </ng-template>
    </ul>
  `,
  styles: ['.nav-dropdown-toggle { cursor: pointer; }']
})
export class SidebarNavDropdownComponent implements OnInit {
  @Input() link: any;

  public isBadge() {
    return this.link.badge ? true : false;
  }

  public isIcon() {
    return this.link.icon ? true : false;
  }

  constructor(private router: Router, private el: ElementRef) {
  }

  ngOnInit() {
    Replace(this.el);
  }
}

@Component({
  selector: 'sidebar-nav-title',
  template: ''
})
export class SidebarNavTitleComponent implements OnInit {
  @Input() title: any;

  constructor(private el: ElementRef, private renderer: Renderer2) {
  }

  ngOnInit() {
    const nativeElement: HTMLElement = this.el.nativeElement;
    const li = this.renderer.createElement('li');
    const name = this.renderer.createText(this.title.name);

    this.renderer.addClass(li, 'nav-title');

    if (this.title.class) {
      const classes = this.title.class;
      this.renderer.addClass(li, classes);
    }

    if (this.title.wrapper) {
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

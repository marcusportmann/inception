import { Component, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';

// Import navigation elements

import { Router } from '@angular/router';


//import { navigation } from '../../../../navigation';

@Component({
  selector: 'inception-layout-sidebar-nav',
  template: `
    <nav class="sidebar-nav">
      <ul class="nav">
        <ng-template ngFor let-navitem [ngForOf]="navigation">
          <li *ngIf="isDivider(navitem)" class="nav-divider"></li>
          <ng-template [ngIf]="isTitle(navitem)">
            <inception-layout-sidebar-nav-title [title]='navitem'></inception-layout-sidebar-nav-title>
          </ng-template>
          <ng-template [ngIf]="!isDivider(navitem)&&!isTitle(navitem)">
            <inception-layout-sidebar-nav-item [item]='navitem'></inception-layout-sidebar-nav-item>
          </ng-template>
        </ng-template>
      </ul>
    </nav>`
})
export class SidebarNavComponent implements OnInit {

  public navigation = [];

  constructor(private router: Router) {

    for (var i = 0; i < router.config.length; i++) {

      var route:any = router.config[i];

      if (route.data) {
        if (route.data.sidebarNav) {
          if (route.data.title) {
            this.navigation.push({
              name: route.data.title,
              url: '/' + route.path,
              icon: route.data.icon
            });
          }
        }
      }
    }
  }

  public isDivider(item) {
    return !!item.divider;
  }

  public isTitle(item) {
    return !!item.title;
  }

  ngOnInit(): void {
  }

}

@Component({
  selector: 'inception-layout-sidebar-nav-item',
  template: `
    <li *ngIf="!isDropdown(); else dropdown" [ngClass]="hasClass() ? 'nav-item ' + item.class : 'nav-item'">
      <inception-layout-sidebar-nav-link [link]='item'></inception-layout-sidebar-nav-link>
    </li>
    <ng-template #dropdown>
      <li [ngClass]="hasClass() ? 'nav-item nav-dropdown ' + item.class : 'nav-item nav-dropdown'"
          [class.open]="isActive()"
          routerLinkActive="open"
          inceptionNavDropdown>
        <inception-layout-sidebar-nav-dropdown [link]='item'></inception-layout-sidebar-nav-dropdown>
      </li>
    </ng-template>
    `
})
export class SidebarNavItemComponent implements OnInit {
  @Input() item: any;

  public hasClass() {
    return !!this.item.class;
  }

  public isDropdown() {
    return !!this.item.children;
  }

  public thisUrl() {
    return this.item.url;
  }

  public isActive() {
    return this.router.isActive(this.thisUrl(), false);
  }

  constructor( private router: Router )  { }

  ngOnInit(): void {
  }

}

@Component({
  selector: 'inception-layout-sidebar-nav-link',
  template: `
    <a *ngIf="!isExternalLink(); else external"
      [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'"
      routerLinkActive="active"
      [routerLink]="[link.url]">
      <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ng-template #external>
      <a [ngClass]="hasVariant() ? 'nav-link nav-link-' + link.variant : 'nav-link'" href="{{link.url}}">
        <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
        {{ link.name }}
        <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
      </a>
    </ng-template>
  `
})
export class SidebarNavLinkComponent implements  OnInit {
  @Input() link: any;

  public hasVariant() {
    return !!this.link.variant;
  }

  public isBadge() {
    return !!this.link.badge;
  }

  public isExternalLink() {
    return this.link.url.substring(0, 4) === 'http';
  }

  public isIcon() {
    return !!this.link.icon;
  }

  constructor() { }


  ngOnInit(): void {
  }

}

@Component({
  selector: 'inception-layout-sidebar-nav-dropdown',
  template: `
    <a class="nav-link nav-dropdown-toggle" inceptionNavDropdownToggle>
      <i *ngIf="isIcon()" class="{{ link.icon }}"></i>
      {{ link.name }}
      <span *ngIf="isBadge()" [ngClass]="'badge badge-' + link.badge.variant">{{ link.badge.text }}</span>
    </a>
    <ul class="nav-dropdown-items">
      <ng-template ngFor let-child [ngForOf]="link.children">
        <inception-layout-sidebar-nav-item [item]='child'></inception-layout-sidebar-nav-item>
      </ng-template>
    </ul>
  `
})
export class SidebarNavDropdownComponent implements OnInit {
  @Input() link: any;

  public isBadge() {
    return !!this.link.badge;
  }

  public isIcon() {
    return !!this.link.icon;
  }

  constructor() { }

  ngOnInit(): void {
  }

}

@Component({
  selector: 'inception-layout-sidebar-nav-title',
  template: ''
})
export class SidebarNavTitleComponent implements OnInit {
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
  }
}


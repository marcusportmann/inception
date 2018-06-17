import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { asideMenuCssClasses, Replace } from './../../../shared/index';

@Component({
  selector: 'inception-layout-aside',
  template: `
    <aside class="aside">
      <ng-content></ng-content>
    </aside>
  `
})
export class AsideComponent implements OnInit {
  @Input() display: any;
  @Input() fixed: boolean;
  @Input() offCanvas: boolean;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    Replace(this.el);
    this.isFixed(this.fixed);
    this.displayBreakpoint(this.display);
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('aside-fixed'); }
  }

  isOffCanvas(offCanvas: boolean): void {
    if (this.offCanvas) { document.querySelector('body').classList.add('aside-off-canvas'); }
  }

  displayBreakpoint(display: any): void {
    if (this.display !== false ) {
      let cssClass;
      this.display ? cssClass = `aside-${this.display}-show` : cssClass = asideMenuCssClasses[0];
      document.querySelector('body').classList.add(cssClass);
    }
  }
}

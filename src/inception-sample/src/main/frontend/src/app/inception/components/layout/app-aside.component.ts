import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { appAsideCssClasses, Replace } from './../../shared/index';

@Component({
  selector: 'app-aside',
  template: `
    <aside class="app-aside">
      <ng-content></ng-content>
    </aside>
  `
})
export class AppAsideComponent implements OnInit {
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
    if (this.fixed) { document.querySelector('body').classList.add('app-aside-fixed'); }
  }

  isOffCanvas(offCanvas: boolean): void {
    if (this.offCanvas) { document.querySelector('body').classList.add('app-aside-off-canvas'); }
  }

  displayBreakpoint(display: any): void {
    if (this.display !== false ) {
      let cssClass;
      this.display ? cssClass = `app-aside-${this.display}-show` : cssClass = appAsideCssClasses[0];
      document.querySelector('body').classList.add(cssClass);
    }
  }
}

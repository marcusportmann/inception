import {Directive, HostListener, Input, OnInit} from '@angular/core';
import {ToggleClasses} from "../shared/toggle-classes";
import {appAsideCssClasses} from "../shared/classes";

/**
 * Allows the application aside to be toggled via click.
 */
@Directive({
  selector: '[appAsideToggle]',
})
export class AppAsideToggleDirective implements OnInit {
  @Input('appAsideToggle') breakpoint: string;
  public bp;
  constructor() {}
  ngOnInit(): void {
    this.bp = this.breakpoint;
  }
  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `app-aside-${this.bp}-show` : cssClass = appAsideCssClasses[0];
    ToggleClasses(cssClass, appAsideCssClasses);
  }
}

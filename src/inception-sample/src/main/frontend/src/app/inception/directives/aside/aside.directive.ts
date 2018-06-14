import {Directive, HostListener, Input, OnInit} from '@angular/core';
import {ToggleClasses} from "../../shared/toggle-classes";
import {asideMenuCssClasses} from "../../shared/classes";

/**
 * Allows the aside to be toggled via click.
 */
@Directive({
  selector: '[inceptionAsideMenuToggler]',
})
export class AsideToggleDirective implements OnInit {
  @Input('inceptionAsideMenuToggler') breakpoint: string;
  public bp;
  constructor() {}
  ngOnInit(): void {
    this.bp = this.breakpoint;
  }
  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `aside-menu-${this.bp}-show` : cssClass = asideMenuCssClasses[0];
    ToggleClasses(cssClass, asideMenuCssClasses);
  }
}

import {Directive, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {appSidebarCssClasses} from "../../shared/classes";
import {ToggleClasses} from "../../shared/toggle-classes";

@Directive({
  selector: '[appBrandMinimizer]'
})
export class AppBrandMinimizerDirective {
  constructor() { }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('app-brand-minimized');
  }
}









/**
 * Allows the sidebar to be toggled via click.
 */
@Directive({
  selector: '[appSidebarToggler]'
})
export class AppSidebarTogglerDirective implements OnInit {
  @Input('appSidebarToggler') breakpoint: string;
  public bp;
  constructor() {}
  ngOnInit(): void {
    this.bp = this.breakpoint;
  }
  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `app-sidebar-${this.bp}-show` : cssClass = appSidebarCssClasses[0];
    ToggleClasses(cssClass, appSidebarCssClasses);
  }
}

@Directive({
  selector: '[appSidebarMinimizer]'
})
export class AppSidebarMinimizerDirective {
  constructor() { }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('app-sidebar-minimized');
  }
}

@Directive({
  selector: '[appMobileSidebarToggler]'
})
export class AppMobileSidebarTogglerDirective {
  constructor() { }

  // Check if element has class
  private hasClass(target: any, elementClassName: string) {
    return new RegExp('(\\s|^)' + elementClassName + '(\\s|$)').test(target.className);
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('app-sidebar-mobile-show');
  }
}

/**
 * Allows the off-canvas sidebar to be closed via click.
 */
@Directive({
  selector: '[appSidebarOffCanvasClose]'
})
export class AppSidebarOffCanvasCloseDirective {
  constructor() { }

  // Check if element has class
  private hasClass(target: any, elementClassName: string) {
    return new RegExp('(\\s|^)' + elementClassName + '(\\s|$)').test(target.className);
  }

  // Toggle element class
  private toggleClass(elem: any, elementClassName: string) {
    let newClass = ' ' + elem.className.replace( /[\t\r\n]/g, ' ' ) + ' ';
    if (this.hasClass(elem, elementClassName)) {
      while (newClass.indexOf(' ' + elementClassName + ' ') >= 0 ) {
        newClass = newClass.replace( ' ' + elementClassName + ' ' , ' ' );
      }
      elem.className = newClass.replace(/^\s+|\s+$/g, '');
    } else {
      elem.className += ' ' + elementClassName;
    }
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();

    if (this.hasClass(document.querySelector('body'), 'app-sidebar-off-canvas')) {
      this.toggleClass(document.querySelector('body'), 'app-sidebar-opened');
    }
  }
}





@Directive({
  selector: '[appSidebarNavDropdown]'
})
export class AppSidebarNavDropdownDirective {

  constructor(private el: ElementRef) { }

  toggle() {
    this.el.nativeElement.classList.toggle('open');
  }
}

/**
 * Allows the dropdown to be toggled via click.
 */
@Directive({
  selector: '[appSidebarNavDropdownToggler]'
})
export class AppSidebarNavDropdownTogglerDirective {
  constructor(private dropdown: AppSidebarNavDropdownDirective) {}

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    this.dropdown.toggle();
  }
}
















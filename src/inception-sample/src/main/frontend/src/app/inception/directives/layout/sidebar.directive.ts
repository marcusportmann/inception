import {Directive, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {sidebarCssClasses} from "../../shared/classes";
import {ToggleClasses} from "../../shared/toggle-classes";

@Directive({
  selector: '[appBrandMinimizer]'
})
export class AppBrandMinimizerDirective {
  constructor() { }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('admin-brand-minimized');
  }
}









/**
 * Allows the sidebar to be toggled via click.
 */
@Directive({
  selector: '[sidebarToggler]'
})
export class SidebarTogglerDirective implements OnInit {
  @Input('sidebarToggler') breakpoint: string;
  public bp;
  constructor() {}
  ngOnInit(): void {
    this.bp = this.breakpoint;
  }
  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `sidebar-${this.bp}-show` : cssClass = sidebarCssClasses[0];
    ToggleClasses(cssClass, sidebarCssClasses);
  }
}

@Directive({
  selector: '[sidebarMinimizer]'
})
export class SidebarMinimizerDirective {
  constructor() { }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('sidebar-minimized');
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
    document.querySelector('body').classList.toggle('sidebar-mobile-show');
  }
}

/**
 * Allows the off-canvas sidebar to be closed via click.
 */
@Directive({
  selector: '[sidebarOffCanvasClose]'
})
export class SidebarOffCanvasCloseDirective {
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

    if (this.hasClass(document.querySelector('body'), 'sidebar-off-canvas')) {
      this.toggleClass(document.querySelector('body'), 'sidebar-opened');
    }
  }
}





@Directive({
  selector: '[sidebarNavDropdown]'
})
export class SidebarNavDropdownDirective {

  constructor(private el: ElementRef) { }

  toggle() {
    this.el.nativeElement.classList.toggle('open');
  }
}

/**
 * Allows the dropdown to be toggled via click.
 */
@Directive({
  selector: '[sidebarNavDropdownToggler]'
})
export class SidebarNavDropdownTogglerDirective {
  constructor(private dropdown: SidebarNavDropdownDirective) {}

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    this.dropdown.toggle();
  }
}
















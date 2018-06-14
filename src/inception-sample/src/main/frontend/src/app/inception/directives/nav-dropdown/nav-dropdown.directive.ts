// TO DELETE -- MARCUS

// import { Directive, HostListener, ElementRef } from '@angular/core';
//
// @Directive({
//   selector: '[inceptionNavDropdown]'
// })
// export class NavigationDropdownDirective {
//
//   constructor(private el: ElementRef) { }
//
//   toggle() {
//     this.el.nativeElement.classList.toggle('open');
//   }
// }
//
// /**
// * Allows the dropdown to be toggled via click.
// */
// @Directive({
//   selector: '[inceptionNavDropdownToggle]'
// })
// export class NavigationDropdownToggleDirective {
//   constructor(private dropdown: NavigationDropdownDirective) {}
//
//   @HostListener('click', ['$event'])
//   toggleOpen($event: any) {
//     $event.preventDefault();
//     this.dropdown.toggle();
//   }
// }

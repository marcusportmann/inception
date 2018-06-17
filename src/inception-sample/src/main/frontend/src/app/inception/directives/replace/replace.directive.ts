// TO DELETE -- MARCUS

// import { Directive, ElementRef, OnInit } from '@angular/core';
//
// @Directive({
//   // tslint:disable-next-line:max-line-length
//   selector: '[inceptionHostReplace], layout-aside, layout-breadcrumbs, inception-layout-footer, inception-layout-header, inception-layout-sidebar, inception-layout-sidebar-footer, inception-layout-sidebar-form, inception-layout-sidebar-header, inception-layout-sidebar-minimizer, inception-layout-sidebar-navigation, inception-layout-sidebar-navigation-dropdown, inception-layout-sidebar-navigation-item, inception-layout-sidebar-navigation-link, inception-layout-sidebar-navigation-title'
// })
// export class ReplaceDirective implements OnInit {
//
//   constructor(private el: ElementRef) { }
//
//   // wait for the component to render completely
//   ngOnInit() {
//     const nativeElement: HTMLElement = this.el.nativeElement;
//     const parentElement: HTMLElement = nativeElement.parentElement;
//     // move all children out of the element
//     while (nativeElement.firstChild) {
//       parentElement.insertBefore(nativeElement.firstChild, nativeElement);
//     }
//     // remove the empty element(the host)
//     parentElement.removeChild(nativeElement);
//   }
// }

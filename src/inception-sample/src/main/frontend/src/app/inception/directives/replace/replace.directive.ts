import { Directive, ElementRef, OnInit } from '@angular/core';

@Directive({
  // tslint:disable-next-line:max-line-length
  selector: '[appHostReplace], inception-layout-aside, inception-layout-breadcrumbs, inception-layout-footer, inception-layout-header, inception-layout-sidebar, inception-layout-sidebar-footer, inception-layout-sidebar-form, inception-layout-sidebar-header, inception-layout-sidebar-minimizer, inception-layout-sidebar-nav, inception-layout-sidebar-nav-dropdown, inception-layout-sidebar-nav-item, inception-layout-sidebar-nav-link, inception-layout-sidebar-nav-title'
})
export class ReplaceDirective implements OnInit {

  constructor(private el: ElementRef) { }

  // wait for the component to render completely
  ngOnInit() {
    const nativeElement: HTMLElement = this.el.nativeElement;
    const parentElement: HTMLElement = nativeElement.parentElement;
    // move all children out of the element
    while (nativeElement.firstChild) {
      parentElement.insertBefore(nativeElement.firstChild, nativeElement);
    }
    // remove the empty element(the host)
    parentElement.removeChild(nativeElement);
  }
}

import { Directive, ElementRef, OnInit } from '@angular/core';

@Directive({
  // tslint:disable-next-line:max-line-length
  selector: '[appHostReplace], inception-aside, inception-breadcrumbs, inception-footer, inception-header, inception-sidebar, inception-sidebar-footer, inception-sidebar-form, inception-sidebar-header, inception-sidebar-minimizer, inception-sidebar-nav, inception-sidebar-nav-dropdown, inception-sidebar-nav-item, inception-sidebar-nav-link, inception-sidebar-nav-title'
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

import {Directive, ElementRef, AfterContentInit} from '@angular/core';

@Directive({
  selector: '[autofocus]'
})
export class AutofocusDirective implements AfterContentInit {

  constructor(private elementRef: ElementRef) {
  }

  ngAfterContentInit() {
    this.elementRef.nativeElement.focus();
  }
}

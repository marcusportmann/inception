import { Directive, AfterViewInit, ElementRef } from '@angular/core';

@Directive({
  selector: '[inceptionAutofocus]'
})
export class AutofocusDirective implements AfterViewInit {

  constructor(private el: ElementRef) {
    console.log('Here 1...');
  }

  ngAfterViewInit() {
    console.log('Here 2...');
    this.el.nativeElement.focus();
  }

}

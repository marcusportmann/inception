import { Component, ElementRef, Input, OnInit  } from '@angular/core';
import { Replace } from './../../../shared';

@Component({
  selector: 'inception-layout-footer',
  template: `
    <footer class="inception-layout-footer">
      <ng-content></ng-content>
    </footer>
  `
})
export class FooterComponent implements OnInit {
  @Input() fixed: boolean;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    Replace(this.el);
    this.isFixed(this.fixed);
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('footer-fixed'); }
  }
}


// import {Component, OnInit} from '@angular/core';
//
// @Component({
//   selector: 'inception-layout-footer',
//   templateUrl: './footer.component.html'
// })
// export class FooterComponent implements OnInit {
//
//   ngOnInit(): void {
//   }
//
//
// }

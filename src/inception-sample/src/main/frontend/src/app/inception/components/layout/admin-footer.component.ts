import { Component, ElementRef, Input, OnInit  } from '@angular/core';
import { Replace } from '../../shared/index';

@Component({
  selector: 'admin-footer',
  template: `
    <footer class="admin-footer">
      <ng-content></ng-content>
    </footer>
  `
})
export class AdminFooterComponent implements OnInit {
  @Input() fixed: boolean;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    Replace(this.el);
    this.isFixed(this.fixed);
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('admin-footer-fixed'); }
  }
}

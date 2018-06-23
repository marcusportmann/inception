import { Component, ElementRef, OnInit  } from '@angular/core';
import { Replace } from '../../shared/index';

@Component({
  selector: 'app-sidebar-footer',
  template: `
    <div class="app-sidebar-footer">
      <ng-content></ng-content>
    </div>`
})
export class AppSidebarFooterComponent implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);
  }
}

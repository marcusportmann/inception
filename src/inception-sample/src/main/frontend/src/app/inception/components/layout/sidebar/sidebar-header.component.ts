import { Component, ElementRef, OnInit  } from '@angular/core';
import { Replace } from './../../../shared';

@Component({
  selector: 'inception-layout-sidebar-header',
  template: `
    <div class="sidebar-header">
      <ng-content></ng-content>
    </div>
  `
})
export class SidebarHeaderComponent implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);
  }
}

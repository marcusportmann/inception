import { Component, ElementRef, OnInit  } from '@angular/core';
import { Replace } from '../../shared/index';

@Component({
  selector: 'app-sidebar-form',
  template: `
    <form class="app-sidebar-form">
      <ng-content></ng-content>
    </form>
  `
})
export class AppSidebarFormComponent implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);
  }
}

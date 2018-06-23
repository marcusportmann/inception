import { Component, ElementRef, OnInit  } from '@angular/core';
import { Replace } from '../../shared/index';

@Component({
  selector: 'app-sidebar-minimizer',
  template: `
    <button class="app-sidebar-minimizer" type="button" appSidebarMinimizer appBrandMinimizer></button>
  `
})
export class AppSidebarMinimizerComponent implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);
  }
}

import { Component, ElementRef, OnInit  } from '@angular/core';
import { Replace } from './../../../shared';

@Component({
  selector: 'inception-layout-sidebar-minimizer',
  template: `
    <button class="sidebar-minimizer" type="button" inceptionSidebarMinimizer inceptionBrandMinimizer></button>
  `
})
export class SidebarMinimizerComponent implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);
  }
}

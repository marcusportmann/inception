import {Component, OnInit} from '@angular/core';
import {ContainerComponent} from "../container";

@Component({
  selector: 'inception-layout-simple-container',
  template: '<router-outlet></router-outlet>',
})
export class SimpleContainerComponent extends ContainerComponent {

  constructor() {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
  }
}

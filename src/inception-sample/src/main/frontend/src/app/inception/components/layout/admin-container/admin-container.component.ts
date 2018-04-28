import { Component, OnInit } from '@angular/core';
import {ContainerComponent} from "../container";

@Component({
  selector: 'inception-layout-admin-container',
  templateUrl: './admin-container.component.html',
  styleUrls: ['./admin-container.component.css']
})
export class AdminContainerComponent extends ContainerComponent implements OnInit {

  constructor() {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
  }
}

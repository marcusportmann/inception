import { Component, OnInit } from '@angular/core';
import {ContainerComponent} from "../container";
import {Router} from "@angular/router";

@Component({
  selector: 'inception-layout-admin-container',
  templateUrl: './admin-container.component.html',
  styleUrls: ['./admin-container.component.css']
})
export class AdminContainerComponent extends ContainerComponent {

  constructor(private router: Router) {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
  }
}

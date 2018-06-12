import { Component, OnInit } from '@angular/core';
import {ContainerComponent} from "../container";
import {Router} from "@angular/router";

@Component({
  selector: 'inception-layout-standard-container',
  templateUrl: './standard-container.component.html',
  styleUrls: ['./standard-container.component.css']
})
export class StandardContainerComponent extends ContainerComponent {

  constructor(private router: Router) {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
  }
}

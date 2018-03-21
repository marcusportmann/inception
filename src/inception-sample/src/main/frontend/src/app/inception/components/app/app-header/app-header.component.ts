import {Component, OnInit} from '@angular/core';
import {AppSession} from "../../../models/app/app-session";


@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html'
})
export class AppHeaderComponent implements OnInit {

  public showLogin: boolean = false;


  public constructor() {

  }

  ngOnInit(): void {
  }


  public isLoggedIn() : boolean {

    console.log("Invoking AppHeaderComponent::isLoggedIn()");

    return AppSession.getAppSession().isLoggedIn();
  }


}

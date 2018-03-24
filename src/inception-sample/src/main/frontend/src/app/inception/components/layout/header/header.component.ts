import {Component, OnInit} from '@angular/core';
import {Session} from '../../../models/session';


@Component({
  selector: 'inception-layout-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  public showLogin = false;


  public constructor() {

  }

  ngOnInit(): void {
  }


  public isLoggedIn(): boolean {

    console.log('Invoking HeaderComponent::isLoggedIn()');

    return Session.getSession().isLoggedIn();
  }


}

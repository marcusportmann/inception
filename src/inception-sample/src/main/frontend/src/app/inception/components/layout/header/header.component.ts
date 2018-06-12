import {Component, OnInit} from '@angular/core';
import {SessionService} from "../../../services/session/session.service";
import {Observable} from "rxjs/Observable";


@Component({
  selector: 'inception-layout-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  public showLogin = false;


  public constructor(private sessionService: SessionService) {

  }

  ngOnInit(): void {
  }


  public isLoggedIn(): Observable<boolean> {

    return Observable.of(true);


    //console.log('Invoking HeaderComponent::isLoggedIn()');

    //return Session.getSession().isLoggedIn();

    //return false;
  }




}

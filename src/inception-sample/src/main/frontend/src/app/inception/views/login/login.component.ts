import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';

import {InceptionModule} from '../../inception.module';
import {Session} from '../../models/session';

import {patternValidator} from "../../validators/pattern-validator";

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

  private loginForm: FormGroup;
  private username: string;
  private password: string;

  constructor() {
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      // tslint:disable-next-line
      username: new FormControl('', [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]),
      password: new FormControl('', Validators.required),
    });
  }

  public isForgottenPasswordEnabled(): boolean {

    return InceptionModule.forgottenPasswordEnabled;
  }

  public isRegistrationEnabled(): boolean {

    return InceptionModule.registrationEnabled;
  }

  public onCancel() {
    console.log('Cancel clicked!');
  }

  public onSubmit() {
    console.log('Form submitted (' + this.loginForm.valid + ')!');

    if (this.loginForm.valid)
    {
      console.log('username = ' + this.username);
      console.log('password = ' + this.password);
    }
    else
    {
      //this.loginForm.reset();
    }
  }
}

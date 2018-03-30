import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';
import {Session} from '../../models/session';

import {patternValidator} from "../../validators/pattern-validator";

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {

    this.loginForm = this.formBuilder.group({
      // tslint:disable-next-line
      username: ['', [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]],
      password: ['', Validators.required],
    });
  }

  public isForgottenPasswordEnabled(): boolean {

    return InceptionModule.forgottenPasswordEnabled;
  }

  public isRegistrationEnabled(): boolean {

    return InceptionModule.registrationEnabled;
  }

  public onCancel() {
    //console.log('Cancel clicked!');
    // let control = this.loginForm.get('username')
    // control.disabled ? control.enable() : control.disable()
  }

  public onSubmit() {

    if (this.loginForm.valid) {
      console.log('username = ' + this.loginForm.get('username').value);
      console.log('password = ' + this.loginForm.get('password').value);
    }
  }
}

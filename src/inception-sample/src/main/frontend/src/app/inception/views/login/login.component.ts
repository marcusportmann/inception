import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';
import {JSONP_ERR_WRONG_RESPONSE_TYPE} from "@angular/common/http/src/jsonp";

import { decode } from "jsonwebtoken";
import {Session} from "../../services/security/session";
import {HttpErrorResponse} from "@angular/common/http";
import {LoginError} from "../../services/security/security.service.errors";
import {ErrorService} from "../../services/error/error.service";


@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private errorService: ErrorService, private securityService: SecurityService) {

    this.loginForm = this.formBuilder.group({
      // tslint:disable-next-line
      username: ['test@test.com', [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]],
      password: ['Password1', Validators.required],
    });
  }

  public isForgottenPasswordEnabled(): boolean {

    return InceptionModule.forgottenPasswordEnabled;
  }

  public isRegistrationEnabled(): boolean {

    return InceptionModule.registrationEnabled;
  }

  public onCancel() {

    this.errorService.showConfirm('This is a title', 'This is a message');

    //console.log('Cancel clicked!');
    // let control = this.loginForm.get('username')
    // control.disabled ? control.enable() : control.disable()
  }

  public onSubmit() {

    if (this.loginForm.valid) {

      this.securityService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(session => {

        console.log('session = ', session);

        /*
        if (result instanceof Session) {
          console.log('session = ', result);
        }
        else if (result instanceof HttpErrorResponse) {
          console.log('error = ', result);
        }
        */

      },
        error => {

        if (error instanceof LoginError ) {
          console.log('Login error = ', error);
        }
        else {
          console.log('Unknown error = ', error);
        }

        });
    }
  }
}

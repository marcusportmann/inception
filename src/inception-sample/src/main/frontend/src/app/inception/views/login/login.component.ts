import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';
import {JSONP_ERR_WRONG_RESPONSE_TYPE} from "@angular/common/http/src/jsonp";

import { decode } from "jsonwebtoken";
import {Session} from "../../services/security/session";
import {HttpErrorResponse} from "@angular/common/http";
import {TestError} from "../../services/security/security.service.errors";


@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private securityService: SecurityService) {

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

      this.securityService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(result => {

        if (result instanceof Session) {
          console.log('session = ', result);
        }
        else if (result instanceof HttpErrorResponse) {
          console.log('error = ', result);
        }


      },
        error => {

        if (error instanceof TestError ) {
          console.log('Test error = ', error);
        }
        else {
          console.log('Unknown error = ', error);
        }

        });
    }
  }
}

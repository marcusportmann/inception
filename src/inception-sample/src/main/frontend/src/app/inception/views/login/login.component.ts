import {Component, Inject, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';
import {JSONP_ERR_WRONG_RESPONSE_TYPE} from "@angular/common/http/src/jsonp";

import { decode } from "jsonwebtoken";
import {Session} from "../../services/security/session";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {TestError} from "../../services/security/security.service.errors";
import {SESSION_STORAGE, WebStorageService} from "angular-webstorage-service";
import {TokenResponse} from "../../services/security/token-response";
import {catchError, map} from "rxjs/operators";
import {Observable} from "rxjs/Rx";


@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient, private securityService: SecurityService,) {

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


  public onGetCodes() {

    this.httpClient.get<any>('http://localhost:8080/api/codeCategories').pipe(
      map(data => {

        console.log('data = ' + data);


      }), catchError((error: HttpErrorResponse) => {

        console.log('catchError = ', error);

        // TODO: Map different HTTP error codes to specific error types -- MARCUS


        return Observable.throw(new TestError(error.status));

      })).subscribe(data => {

        console.log('data = ', data);


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

  public onSubmit() {

    if (this.loginForm.valid) {

      sessionStorage.removeItem("session");

      this.securityService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(session => {

        sessionStorage.setItem("session", JSON.stringify(session));

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

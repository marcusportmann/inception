/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';
import {JSONP_ERR_WRONG_RESPONSE_TYPE} from "@angular/common/http/src/jsonp";

import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {ErrorService} from "../../services/error/error.service";
import {catchError, map} from "rxjs/operators";
import {Observable} from "../../../../../node_modules/rxjs";

import {Organization} from "../../services/security/organization";
import {SessionService} from "../../services/session/session.service";
import {Router} from "@angular/router";


@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient, private errorService: ErrorService, private securityService: SecurityService, private sessionService: SessionService, private router: Router) {

    this.loginForm = this.formBuilder.group({
      // tslint:disable-next-line
      username: ['Administrator', Validators.required],
      //username: ['test@test.com', [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]],
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

      this.sessionService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(session => {

          console.log('session = ', session);


          this.securityService.getOrganizations().subscribe(organizations => {

            console.log('organizations = ', organizations);

            this.router.navigate(['/']);

          }, error => {

            console.log('error = ', error);

          });





        },error => {

          console.log('error = ', error);

        });
    }
  }
}

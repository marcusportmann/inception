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

import {Component, ViewContainerRef} from '@angular/core';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';


import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {ErrorReportingService} from "../../services/error-reporting/error-reporting.service";
import {catchError, map} from "rxjs/operators";
import {Observable} from "../../../../../node_modules/rxjs";

import {Organization} from "../../services/security/organization";
import {SessionService} from "../../services/session/session.service";
import {Router} from "@angular/router";
import {Error} from "../../errors/error";
import {SpinnerService} from "../../services/layout/spinner.service";
import {LoginError} from "../../services/session/session.service.errors";
import {DialogService} from "../../services/dialog/dialog.service";
import {CommunicationError} from "../../errors/communication-error";



import {I18n} from "@ngx-translate/i18n-polyfill";




@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private dialogService: DialogService, private spinnerService: SpinnerService, private i18n: I18n, private securityService: SecurityService, private sessionService: SessionService, private router: Router) {

    this.loginForm = this.formBuilder.group({
      // tslint:disable-next-line
      username: ['Administrator', Validators.required],
      //username: ['test@test.com', [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]],
      password: ['Password1', Validators.required]
    });
  }

  public isForgottenPasswordEnabled(): boolean {

    return InceptionModule.forgottenPasswordEnabled;
  }

  public isRegistrationEnabled(): boolean {

    return InceptionModule.registrationEnabled;
  }

  public onForgotPassword() {

    //this.router.navigate(['/']);

    //let error: Error = new Error(new Date(), 'This is the error message', 'This is the error detail', 'This is the error stack trace');

    this.dialogService.showInformationDialog({message: 'This is an information message.'});

    //this.dialogService.showWarningDialog({message: 'This is a warning message.'});

    //this.dialogService.showErrorDialog(new Error('This is an error message.'));




    //this.dialogService.showInformationDialog({message: this.i18n({id: '@@xxx', value: 'This is a test {{myVar}} !'}, {myVar: '^_^'})});


    //this.errorService.showConfirm('This is a title', 'This is a message');

    //console.log('Cancel clicked!');
    // let control = this.loginForm.get('username')
    // control.disabled ? control.enable() : control.disable()
  }

  public onSubmit() {

    if (this.loginForm.valid) {

      this.spinnerService.show();

      this.sessionService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(session => {

          console.log('session = ', session);

          this.securityService.getOrganizations().subscribe(organizations => {

            console.log('organizations = ', organizations);

            this.spinnerService.hide();

            this.router.navigate(['/']);

          }, error => {

            this.spinnerService.hide();

            this.dialogService.showErrorDialog(error);

          });

        },(error: Error) => {

          this.spinnerService.hide();

          this.dialogService.showErrorDialog(error);
       });
    }
  }
}

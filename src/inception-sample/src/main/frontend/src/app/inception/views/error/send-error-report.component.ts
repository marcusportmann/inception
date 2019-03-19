/*
 * Copyright 2019 Marcus Portmann
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

import {Component, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';


import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {ErrorService} from "../../services/error/error.service";
import {catchError, map, first, takeUntil} from "rxjs/operators";
import {Observable, pipe, Subject} from "../../../../../node_modules/rxjs";

import {Organization} from "../../services/security/organization";
import {SessionService} from "../../services/session/session.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Error} from "../../errors/error";
import {SpinnerService} from "../../services/layout/spinner.service";
import {LoginError} from "../../services/session/session.service.errors";
import {DialogService} from "../../services/dialog/dialog.service";
import {CommunicationError} from "../../errors/communication-error";



import {I18n} from "@ngx-translate/i18n-polyfill";
import {MatDialogRef} from "@angular/material";
import {ConfirmationDialog} from "../../components/dialogs";

/**
 * The SendErrorReportComponent class implements the send error report component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'send-error-report.component.html'
})
export class SendErrorReportComponent implements OnInit {

  errorForm: FormGroup;

  error: Error;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private dialogService: DialogService, private layoutService: SpinnerService) {

    this.errorForm = this.formBuilder.group({
      // tslint:disable-next-line
      message: [''],
      email: ['', Validators.email],
      description: ['']
    });
  }

  ngOnInit(): void {

    this.activatedRoute.paramMap.pipe(first(),
      map((state: any) => window.history.state))   .subscribe((state: any) => {

        if (state.error) {
          this.error = state.error;

          this.errorForm.get('message').setValue(this.error.message);

        } else {

          console.log('No error found, redirecting to the application root')

          this.router.navigate(['/']);
        }

    });
  }

  onSendErrorReport(): void {

    if (this.errorForm.valid) {

      // this.layoutService.showSpinner();
      //
      // this.sessionService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).pipe(
      //   first()).subscribe(session => {
      //
      //   this.layoutService.hideSpinner();
      //
      //   if (session.organizations.length == 1) {
      //     this.router.navigate(['/']);
      //   }
      //   else {
      //     this.router.navigate(['select-organization'], {relativeTo: this.route});
      //   }
      // },(error: Error) => {
      //
      //   this.layoutService.hideSpinner();
      //
      //   this.dialogService.showErrorDialog(error);
      // });
    }
  }
}

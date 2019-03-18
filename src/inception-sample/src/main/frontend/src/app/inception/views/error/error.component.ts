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

import {Component, OnDestroy, ViewContainerRef} from '@angular/core';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';


import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {ErrorReportingService} from "../../services/error-reporting/error-reporting.service";
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
 * The ErrorComponent class implements the error component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'error.component.html'
})
export class ErrorComponent {

  errorForm: FormGroup;

  constructor(private router: Router, private route: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private dialogService: DialogService, private layoutService: SpinnerService) {

    this.errorForm = this.formBuilder.group({
      // tslint:disable-next-line
      email: ['', Validators.email],
      description: ['']
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

/*
 * Copyright 2020 Marcus Portmann
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

import {Component} from '@angular/core';
import {Error} from 'ngx-inception';
import {Router} from '@angular/router';
import {DialogService} from "../../../../../ngx-inception/src/lib/dialog/services/dialog.service";
import {SpinnerService} from 'ngx-inception';

/**
 * The Menu21Component class implements the menu 2.1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `
  <mat-card class="flex-grow-1">
    <mat-card-content>
      <div class="row">
        <div class="col m-1">
          <button mat-flat-button color="primary" (click)="testErrorReporting()">Test Error Reporting</button>
        </div>
      </div>
      <div class="row">
        <div class="col m-1">
          <button mat-flat-button color="primary" (click)="testSpinner()">Test Spinner</button>
        </div>
      </div>
    </mat-card-content>
  </mat-card>
  `
})
export class Menu21Component {

  constructor(private router: Router, private spinnerService: SpinnerService) {
  }

  testErrorReporting(): void {

    const error: Error = new Error('Testing 1.. 2.. 3..');

    this.router.navigateByUrl('/error/send-error-report', {state: {error}});
  }

  testSpinner():  void {
    this.spinnerService.showSpinner();

    setTimeout(() => {
      this.spinnerService.hideSpinner();
    }, 600000);
  }
}

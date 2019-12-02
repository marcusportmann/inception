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

import {AfterViewInit, Component} from '@angular/core';
import {finalize, first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {CodesService} from '../services/codes.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {CodesServiceError} from '../services/codes.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {Error} from '../../core/errors/error';
import {Code} from '../services/code';
import {BackNavigation} from '../../layout/components/back-navigation';
import {FormControl, FormGroup, Validators} from '@angular/forms';

/**
 * The NewCodeComponent class implements the new code component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-code.component.html',
  styleUrls: ['new-code.component.css'],
})
export class NewCodeComponent extends AdminContainerView implements AfterViewInit {

  code?: Code;

  codeCategoryId: string;

  idFormControl: FormControl;

  nameFormControl: FormControl;

  newCodeForm: FormGroup;

  valueFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private codesService: CodesService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');

    if (!codeCategoryId) {
      throw(new Error('No codeCategoryId route parameter found'));
    }

    this.codeCategoryId = decodeURIComponent(codeCategoryId);

    // Initialise the form controls
    this.idFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.nameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.valueFormControl = new FormControl('', Validators.required);

    // Initialise the form
    this.newCodeForm = new FormGroup({
      id: this.idFormControl,
      name: this.nameFormControl,
      value: this.valueFormControl,
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation('Codes', ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return 'New Code';
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Create the new code
    this.code = new Code('', this.codeCategoryId, '', '');
  }

  ok(): void {
    if (this.code && this.newCodeForm.valid) {
      this.code.id = this.idFormControl.value;
      this.code.name = this.nameFormControl.value;
      this.code.value = this.valueFormControl.value;

      this.spinnerService.showSpinner();

      this.codesService.createCode(this.code)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}

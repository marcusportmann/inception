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

import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {CodesService} from "../../services/codes/codes.service";
import {Error} from "../../errors/error";
import {Code} from "../../services/codes/code";
import {finalize, first} from "rxjs/operators";
import {CodesServiceError} from "../../services/codes/codes.service.errors";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {AccessDeniedError} from "../../errors/access-denied-error";
import {AdminContainerView} from "../../components/layout/admin-container-view";

/**
 * The EditCodeComponent class implements the edit code category component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-code.component.html',
  styleUrls: ['edit-code.component.css'],
})
export class EditCodeComponent extends AdminContainerView implements OnInit {

  codeCategoryId: string;

  editCodeForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.editCodeForm = this.formBuilder.group({
      // tslint:disable-next-line
      id: [{
        value: '',
        disabled: true
      }, [Validators.required, Validators.maxLength(100)]
      ],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      value: ['', Validators.required]
    });
  }

  get idFormControl(): AbstractControl {
    return this.editCodeForm.get('id');
  }

  get nameFormControl(): AbstractControl {
    return this.editCodeForm.get('name');
  }

  get valueFormControl(): AbstractControl {
    return this.editCodeForm.get('value');
  }

  ngOnInit(): void {
    this.codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    let codeId: string = this.activatedRoute.snapshot.paramMap.get('codeId');

    this.spinnerService.showSpinner();

    this.codesService.getCode(this.codeCategoryId, codeId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((codeCategory: Code) => {
        this.idFormControl.setValue(codeCategory.id);
        this.nameFormControl.setValue(codeCategory.name);
        this.valueFormControl.setValue(codeCategory.value);
      }, (error: Error) => {
        if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../../codes'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.editCodeForm.valid) {
      let code: Code = new Code(this.idFormControl.value, this.codeCategoryId,
        this.nameFormControl.value, this.valueFormControl.value);

      this.spinnerService.showSpinner();

      this.codesService.updateCode(code)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../../codes'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}

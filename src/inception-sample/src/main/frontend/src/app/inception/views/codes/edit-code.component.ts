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
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {CodesService} from "../../services/codes/codes.service";
import {Error} from "../../errors/error";
import {Code} from "../../services/codes/code";
import {first} from "rxjs/operators";
import {CodesServiceError} from "../../services/codes/codes.service.errors";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";

/**
 * The EditCodeComponent class implements the edit code category component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-code.component.html',
  styleUrls: ['edit-code.component.css'],
})
export class EditCodeComponent implements OnInit {

  codeCategoryId: string;

  editCodeForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {

    this.editCodeForm = this.formBuilder.group({
      // tslint:disable-next-line
      id: [{value: '', disabled: true}, Validators.required],
      name: ['', Validators.required],
      value: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    let codeId:string = this.activatedRoute.snapshot.paramMap.get('codeId');

    this.spinnerService.showSpinner();

    this.codesService.getCode(this.codeCategoryId, codeId).pipe(first()).subscribe((codeCategory: Code) => {
      this.spinnerService.hideSpinner();

      this.editCodeForm.get('id').setValue(codeCategory.id);
      this.editCodeForm.get('name').setValue(codeCategory.name);
      this.editCodeForm.get('value').setValue(codeCategory.value);
    }, (error: Error) => {
      this.spinnerService.hideSpinner();

      if ((error instanceof CodesServiceError) || (error instanceof SystemUnavailableError)) {
        this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
      }
      else {
        this.dialogService.showErrorDialog(error);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['../../codes'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.editCodeForm.valid) {
      let code: Code = new Code(this.editCodeForm.get('id').value, this.codeCategoryId,
        this.editCodeForm.get('name').value, this.editCodeForm.get('value').value);

      this.spinnerService.showSpinner();

      this.codesService.updateCode(code).pipe(first()).subscribe((result: boolean) => {
        this.spinnerService.hideSpinner();

        this.router.navigate(['../../codes'], {relativeTo: this.activatedRoute});
      }, (error: Error) => {
        this.spinnerService.hideSpinner();

        if ((error instanceof CodesServiceError) || (error instanceof SystemUnavailableError)) {
          this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
        }
        else {
          this.dialogService.showErrorDialog(error);
        }
      });
    }
  }
}

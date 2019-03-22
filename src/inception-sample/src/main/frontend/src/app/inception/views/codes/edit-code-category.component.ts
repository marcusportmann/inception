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
import {CodeCategory} from "../../services/codes/code-category";
import {first} from "rxjs/operators";

/**
 * The EditCodeCategoryComponent class implements the edit code category component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-code-category.component.html',
  styleUrls: ['edit-code-category.component.css'],
})
export class EditCodeCategoryComponent implements OnInit {

  editCodeCategoryForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {

    this.editCodeCategoryForm = this.formBuilder.group({
      // tslint:disable-next-line
      id: [{value: '', disabled: true}, Validators.required],
      name: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    let codeCategoryId = this.activatedRoute.snapshot.paramMap.get('id');

    this.editCodeCategoryForm.get('id').setValue(codeCategoryId);
  }

  onCancel(): void {
    this.router.navigate(['../../../../../code-categories'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.editCodeCategoryForm.valid) {
      let codeCategory: CodeCategory = new CodeCategory(this.editCodeCategoryForm.get('id').value,
        this.editCodeCategoryForm.get('name').value, null);

      this.spinnerService.showSpinner();

      this.codesService.updateCodeCategory(codeCategory).pipe(first()).subscribe((result: boolean) => {
        this.spinnerService.hideSpinner();

        this.router.navigate(['../code-categories'], {relativeTo: this.activatedRoute});
      }, (error: Error) => {
        this.spinnerService.hideSpinner();

        this.dialogService.showErrorDialog(error);
      });
    }
  }
}

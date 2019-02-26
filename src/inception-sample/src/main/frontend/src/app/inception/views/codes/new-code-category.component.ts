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
import {v4 as uuid} from "uuid";

/**
 * The NewCodeCategoryComponent class implements the new code category component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-code-category.component.html',
  styleUrls: ['new-code-category.component.css'],
})
export class NewCodeCategoryComponent implements OnInit {

  newCodeCategoryForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private layoutService: SpinnerService) {

    this.newCodeCategoryForm = this.formBuilder.group({
      //hideRequired: false,
      //floatLabel: 'auto',
      // tslint:disable-next-line
      id: [uuid(), Validators.required],
      name: ['', Validators.required],
      //title: ['', Validators.required],
      //dateOfBirth: [moment(), Validators.required],
    });
  }

  ngOnInit(): void {
  }

  onCancel(): void {
    this.router.navigate(['../code-categories'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {

    let codeCategory: CodeCategory = new CodeCategory(this.newCodeCategoryForm.get('id').value,
      this.newCodeCategoryForm.get('name').value, null);

    this.layoutService.showSpinner();

    this.codesService.createCodeCategory(codeCategory).subscribe((result: boolean) => {

      this.layoutService.hideSpinner();

      this.router.navigate(['../code-categories'], {relativeTo: this.activatedRoute});

    }, (error: Error) => {

      this.layoutService.hideSpinner();

      this.dialogService.showErrorDialog(error);
    });
  }
}

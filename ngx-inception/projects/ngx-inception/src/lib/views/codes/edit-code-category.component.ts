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
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {CodesService} from '../../services/codes/codes.service';
import {Error} from '../../errors/error';
import {CodeCategory} from '../../services/codes/code-category';
import {finalize, first} from 'rxjs/operators';
import {CodesServiceError} from '../../services/codes/codes.service.errors';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';

/**
 * The EditCodeCategoryComponent class implements the edit code category component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-code-category.component.html',
  styleUrls: ['edit-code-category.component.css'],
})
export class EditCodeCategoryComponent extends AdminContainerView implements AfterViewInit {

  codeCategory?: CodeCategory;

  dataFormControl: FormControl;

  editCodeCategoryForm: FormGroup;

  idFormControl: FormControl;

  nameFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private codesService: CodesService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form controls
    this.dataFormControl = new FormControl('');
    this.idFormControl = new FormControl({
      value: '',
      disabled: true
    }, [Validators.required, Validators.maxLength(100)]);
    this.nameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);

    // Initialise the form
    this.editCodeCategoryForm = new FormGroup({
      data: this.dataFormControl,
      id: this.idFormControl,
      name: this.nameFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@codes_edit_code_category_component_back_title',
      value: 'Code Categories'
    }), ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@codes_edit_code_category_component_title',
      value: 'Edit Code Category'
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Retrieve the route parameters
    let codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');

    if (!codeCategoryId) {
      throw(new Error('No codeCategoryId route parameter found'));
    }

    codeCategoryId = decodeURIComponent(codeCategoryId);

    // Retrieve the existing code category and initialise the form controls
    this.spinnerService.showSpinner();

    this.codesService.getCodeCategory(codeCategoryId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((codeCategory: CodeCategory) => {
        this.codeCategory = codeCategory;
        this.idFormControl.setValue(codeCategory.id);
        this.nameFormControl.setValue(codeCategory.name);
        this.dataFormControl.setValue(codeCategory.data);
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  ok(): void {
    if (this.codeCategory && this.editCodeCategoryForm.valid) {
      const data = this.dataFormControl.value;

      this.codeCategory.name = this.nameFormControl.value;
      this.codeCategory.data = (!!data) ? data : null;

      this.spinnerService.showSpinner();

      this.codesService.updateCodeCategory(this.codeCategory)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}

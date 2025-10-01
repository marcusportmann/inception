/*
 * Copyright Marcus Portmann
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

import { AfterViewInit, Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError,
  AdminContainerView,
  BackNavigation,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { CodeCategory } from '../services/code-category';
import { CodesService } from '../services/codes.service';

/**
 * The EditCodeCategoryComponent class implements the edit code category component.
 *
 * @author Marcus
 */
@Component({
  templateUrl: 'edit-code-category.component.html',
  styleUrls: ['edit-code-category.component.css'],
  standalone: false
})
export class EditCodeCategoryComponent
  extends AdminContainerView
  implements AfterViewInit
{
  codeCategory: CodeCategory | null = null;

  codeCategoryId: string;

  dataControl: FormControl;

  editCodeCategoryForm: FormGroup;

  idControl: FormControl;

  nameControl: FormControl;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private codesService: CodesService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Retrieve the route parameter
    const codeCategoryId =
      this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    if (!codeCategoryId) {
      throw new Error('No codeCategoryId route parameter found');
    }
    this.codeCategoryId = decodeURIComponent(codeCategoryId);

    // Initialize form controls
    this.dataControl = new FormControl('');
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.nameControl = new FormControl('', [
      Validators.required,
      Validators.maxLength(100)
    ]);

    // Initialize the form group
    this.editCodeCategoryForm = new FormGroup({
      data: this.dataControl,
      id: this.idControl,
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@codes_edit_code_category_back_navigation:Code Categories`,
      ['../..'],
      {
        relativeTo: this.activatedRoute
      }
    );
  }

  get title(): string {
    return $localize`:@@codes_edit_code_category_title:Edit Code Category`;
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    this.codesService
      .getCodeCategory(this.codeCategoryId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (codeCategory: CodeCategory) => {
          this.codeCategory = codeCategory;
          this.idControl.setValue(codeCategory.id);
          this.nameControl.setValue(codeCategory.name);
          this.dataControl.setValue(codeCategory.data);
        },
        error: (error: Error) => this.handleError(error, true)
      });
  }

  ok(): void {
    if (this.codeCategory && this.editCodeCategoryForm.valid) {
      this.codeCategory.name = this.nameControl.value ?? '';
      this.codeCategory.data = this.dataControl.value?.trim() || null;

      this.spinnerService.showSpinner();

      this.codesService
        .updateCodeCategory(this.codeCategory)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () =>
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            }),
          error: (error: Error) => this.handleError(error, false)
        });
    }
  }

  private handleError(error: Error, navigateOnClose: boolean): void {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService
        .showErrorDialog(error)
        .afterClosed()
        .pipe(first())
        .subscribe(() => {
          if (navigateOnClose) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            });
          }
        });
    }
  }
}
